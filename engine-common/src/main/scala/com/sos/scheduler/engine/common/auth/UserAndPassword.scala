package com.sos.scheduler.engine.common.auth

import com.sos.scheduler.engine.base.generic.SecretString
import scala.language.implicitConversions

/**
  * @author Joacim Zschimmer
  */
final case class UserAndPassword(user: String, password: SecretString)

object UserAndPassword {
  implicit def apply(userAndPassword: (String, SecretString)): UserAndPassword =
    new UserAndPassword(userAndPassword._1, userAndPassword._2) }
