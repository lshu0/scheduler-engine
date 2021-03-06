package com.sos.scheduler.engine.client.api

import com.sos.scheduler.engine.data.compounds.OrdersFullOverview
import com.sos.scheduler.engine.data.jobchain.{JobChainDetails, JobChainOverview, JobChainPath, JobChainQuery}
import com.sos.scheduler.engine.data.order.{OrderOverview, OrderQuery}
import com.sos.scheduler.engine.data.scheduler.SchedulerOverview
import scala.collection.immutable
import scala.concurrent.Future

/**
  * @author Joacim Zschimmer
  */
trait SchedulerClient extends CommandClient {

  def overview: Future[SchedulerOverview]

  final def orderOverviews: Future[immutable.Seq[OrderOverview]] =
    orderOverviews(OrderQuery.All)

  def orderOverviews(query: OrderQuery = OrderQuery.All): Future[immutable.Seq[OrderOverview]]

  final def ordersFullOverview: Future[OrdersFullOverview] = ordersFullOverview(OrderQuery.All)

  def ordersFullOverview(query: OrderQuery = OrderQuery.All): Future[OrdersFullOverview]

  def jobChainOverview(jobChainPath: JobChainPath): Future[JobChainOverview]

  def jobChainOverviews(query: JobChainQuery): Future[immutable.Seq[JobChainOverview]]

  def jobChainDetails(jobChainPath: JobChainPath): Future[JobChainDetails]
}
