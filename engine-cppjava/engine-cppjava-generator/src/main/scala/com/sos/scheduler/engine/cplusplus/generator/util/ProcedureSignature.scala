package com.sos.scheduler.engine.cplusplus.generator.util

import com.sos.scheduler.engine.cplusplus.generator.util.ClassOps._
import com.sos.scheduler.engine.cplusplus.generator.util.ProcedureSignature._
import com.sos.scheduler.engine.cplusplus.runtime.annotation.{CppExpression, CppField, CppThreadSafe}
import java.lang.reflect._
import scala.language.existentials

final case class ProcedureSignature(
  name: String,
  returnType: Class[_],
  parameterTypes: List[Class[_]],
  isStatic: Boolean = false,
  isThreadSafe: Boolean = false,
  access: Access = MethodAccess)
extends Ordered[ProcedureSignature] {

  val nativeJavaName = name + "__native"
  val parameters = for ((t, i) <- parameterTypes.zipWithIndex) yield Parameter(t, "p" + i)

  def hasReturnType = !isVoid(returnType)

  def classes = parameterTypes.toSet + returnType

  def compare(o: ProcedureSignature) = name compare o.name match {
    case 0 ⇒
      compareClassSeqs(parameterTypes, o.parameterTypes) match {
        case 0 ⇒ compareClasses(returnType, o.returnType)
        case c ⇒ c
      }
    case c ⇒ c
  }
}

object ProcedureSignature {
  def apply(clas: Class[_], method: Method) = {
    def error(message: String) = sys.error(s"Method ${clas.getName}.${method.getName}(): $message")
    val returnClass = method.getGenericReturnType match {
      case o: Class[_] ⇒ o
      case parameterizedType: ParameterizedType ⇒
        parameterizedType.getRawType match {
          case o: Class[_] ⇒ o
          case o ⇒ error(s"Unknown ActualTypeArgument ${o.getClass.getName}: '$o'")
        }
      case typeVariable: TypeVariable[_] ⇒
        val typeParameterIndex = typeVariable.getGenericDeclaration.getTypeParameters indexWhere { _.getName == typeVariable.getName }
        if (typeParameterIndex < 0) error(s"${typeVariable.getGenericDeclaration} has no type parameter ${typeVariable.getName}")
        val typ = typeVariable.getGenericDeclaration match {
          case declaringClass: Class[_] ⇒
            if (declaringClass == clas)
              declaringClass.getTypeParameters()(typeParameterIndex).getBounds()(0)
            else {
              val parameterizedType = (clas.getGenericInterfaces collectFirst { case p: ParameterizedType if p.getRawType == method.getDeclaringClass ⇒ p }
                getOrElse error(s"${method.getDeclaringClass.getName} is not a direct superclass of $clas"))
              parameterizedType.getActualTypeArguments()(typeParameterIndex)
            }
          case declaringMethod: Method ⇒
            declaringMethod.getTypeParameters()(typeParameterIndex).getBounds()(0)
          case o ⇒ error(s"Return type of $method is not a class: ${o.getClass.getName}: $o")
        }
        typ match {
          case o: Class[_] ⇒ o
          case o ⇒ error(s"Unknown TypeParameter ${o.getClass.getName}: '$o'")
        }
      case o ⇒ error(s"Unknown GenericDeclaration: ${o.getClass.getName}: $o")
    }
    new ProcedureSignature(
      method.getName,
      returnClass,
      method.getParameterTypes.toList,
      isStatic = Modifier.isStatic(method.getModifiers),
      isThreadSafe = method.getAnnotation(classOf[CppThreadSafe]) != null,
      access = methodToAccess(method))
  }

  private def methodToAccess(method: Method): Access = {
    val isField = method.getAnnotation(classOf[CppField]) != null
    val expressionOption = Option(method.getAnnotation(classOf[CppExpression])) map { _.value }
    (isField, expressionOption) match {
      case (false, None) ⇒ MethodAccess
      case (true, None) ⇒
        require(method.getParameterTypes.length == 0, "@CppField requires no method parameters")
        FieldAccess
      case (false, Some(expression)) ⇒
        require(method.getParameterTypes.length == 0, "@CppExpression requires no method parameters")
        ExpressionAccess(expression)
      case _ ⇒ sys.error("Combination of @CppField and @CppExpression is not possible")
    }
  }

  def apply(c: Constructor[_]) = ofConstructor(c.getParameterTypes)

  def ofConstructor(t: Seq[Class[_]]) = new ProcedureSignature("<init>", classOf[Void], t.toList)

  sealed trait Access
  final case object MethodAccess extends Access
  final case object FieldAccess extends Access
  final case class ExpressionAccess(cppExpression: String) extends Access
}
