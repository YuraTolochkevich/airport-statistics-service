package com.airportstat

import akka.actor.{Props}
import spray.routing._

trait BaseRouting extends HttpServiceActor with PerRequestCreator {

  val requestProcessor: RequestProcessor

  def receive = runRoute(route)

  val route = {
    pathPrefix("airports") {
      path("getInfoById") {
        get {
          parameters('id) { id =>
            getAirportsByIdRequest(id)
          }
        }
      } ~
        path("getInfoByName") {
          get {
            parameters('name) { name =>
              getAirportsByNameRequest(name)
            }
          }
        } ~
        path("getHighestTopTen") {
          get {
            getReports(GetTopHighRequest)
          }
        } ~
        path("getLowestTopTen") {
          get {
            getReports(GetTopLowestRequest)
          }
        } ~
        path("getRunwayTypeByCountry") {
          get {
            getReports(GetRunwayTypeByCountry)
          }
        }
    }
  }

  def getAirportsByNameRequest(id: String): Route = ctx => {
    val act = perRequestQueryByCountry(ctx, requestProcessor,id)
    act ! GetAirPortInfoByNameRequest
  }

  def getAirportsByIdRequest(id: String): Route = ctx => {
    val act = perRequestQueryByCountry(ctx, requestProcessor,id)
    act ! GetAirPortInfoByIdRequest
  }

  def getReports(request: BaseRequest): Route = ctx => {
    val act = perRequestReports(ctx, requestProcessor)
    act ! request
  }

}


class RoutingAirportStatistics (requestProc: RequestProcessor) extends BaseRouting {

  val requestProcessor = requestProc
}


