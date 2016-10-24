package com.airportstat

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{OneForOneStrategy, _}
import com.airportstat.ServicePerRequestActors.{PerRequestGetReportsActorImpl, PerRequestGetStatByCountry}
import org.json4s.DefaultFormats
import spray.http.StatusCode
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

trait PerRequestBaseActor extends Actor with Json4sSupport {

  import context._
  override val supervisorStrategy =
    OneForOneStrategy() {
      case e =>
        complete(InternalServerError, e.getMessage)
        Stop
    }
  val json4sFormats = DefaultFormats
  val requestProcessor: RequestProcessor

  def r: RequestContext

  def complete[T <: AnyRef](status: StatusCode, obj: T) = {
    r.complete(status, obj)
    stop(self)
  }
}

trait PerRequestGetStatByCountryActor extends PerRequestBaseActor {
    val ident: String
    def receive = {
    case GetAirPortInfoByIdRequest =>
      requestProcessor.getOptionById(ident) match {
        case Some(resp: CountryWithAirPorts) => complete(OK, resp)
        case None => complete(NotFound, s"Country not found by id: $ident")
      }
    case GetAirPortInfoByNameRequest =>
      requestProcessor.getOptionByName(ident) match {
        case Some(resp: CountryWithAirPorts) => complete(OK, resp)
        case None => complete(NotFound, s"Country not found by name: $ident")
      }
  }
}

trait PerRequestGetReportsActor extends PerRequestBaseActor {
  def receive = {
    case GetTopHighRequest => complete(OK, requestProcessor.getTopHighTenCountires)
    case GetTopLowestRequest => complete(OK, requestProcessor.getTopLowTenCountires)
    case GetRunwayTypeByCountry => complete(OK, requestProcessor.getRunWayTypePerCountry)
  }
}

object ServicePerRequestActors {
  case class PerRequestGetStatByCountry(r: RequestContext,requestProcessor: RequestProcessor,
                                                  ident: String) extends PerRequestGetStatByCountryActor

  case class PerRequestGetReportsActorImpl(r: RequestContext,
  requestProcessor: RequestProcessor) extends PerRequestGetReportsActor
}

trait PerRequestCreator {
  this: Actor =>

  def perRequestQueryByCountry(r: RequestContext, requestProcessor: RequestProcessor, ident: String) =
    context.actorOf(Props(new PerRequestGetStatByCountry(r, requestProcessor, ident)))

  def perRequestReports(r: RequestContext, requestProcessor: RequestProcessor) =
    context.actorOf(Props(new PerRequestGetReportsActorImpl(r, requestProcessor)))
}

