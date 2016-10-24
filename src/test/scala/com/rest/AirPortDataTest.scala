package com.rest

import akka.testkit.TestActorRef
import com.airportstat._
import org.json4s.DefaultFormats
import org.scalatest.{FlatSpec, Matchers}
import spray.routing._
import spray.testkit.ScalatestRouteTest
import org.json4s.jackson.JsonMethods._
import spray.http.StatusCodes._
import scala.concurrent.duration._

class AirPortStatsServiceTest extends FlatSpec with Matchers with ScalatestRouteTest with HttpService {
  def actorRefFactory = system

  val inData = DataLoader.initStatisticsData()
  val requestProcessor = RequestProcessor(inData.get._1, inData.get._2, inData.get._3)

  def restRouting = TestActorRef(new RoutingAirportStatistics(requestProcessor))

  implicit val formats = DefaultFormats
  private implicit val timeout = RouteTestTimeout(10 second span)
  "The airport service" should
    "Top ten Requests" in {

    Get("/airports/getHighestTopTen") ~> restRouting.underlyingActor.route ~> check {
      assert(responseAs[String].contains("{\"United States\":21476}"))
      status === OK
      val resp = parse(responseAs[String]).extract[List[(String, Int)]]
      assert(resp.length == 10)
      assert(resp == requestProcessor.getTopHighTenCountires)
    }
  }
    it should
      "return Gibraltar in top ten lowest Requests" in {
      Get("/airports/getLowestTopTen") ~> restRouting.underlyingActor.route ~> check {
        assert(responseAs[String].contains("{\"Gibraltar\":1}"))
        status === OK
        val resp = parse(responseAs[String]).extract[List[(String, Int)]]
        assert(resp.length == 10)
        assert(resp == requestProcessor.getTopLowTenCountires)
      }
    }
  it should
    "return runways by country" in {
    Get("/airports/getRunwayTypeByCountry") ~> restRouting.underlyingActor.route ~> check {
      assert(responseAs[String].contains("\"Croatia\":[\"\",\"GRE\",\"grass\",\"UNK\",\"CON\",\"ASP\",\"PEM\"]"))
      status === OK
    }
  }

  it should
    "return Ukraine in get by name Ukr request" in {
    Get("/airports/getInfoByName?name=Ukr") ~> restRouting.underlyingActor.route ~> check {
      status === OK
      val resp = parse(responseAs[String]).extract[CountryWithAirPorts]
      assert(resp.country.name == "Ukraine")
      assert(resp.airPortList.exists(_.airport.name == "Boryspil International Airport"))
    }
  }
  it should
    "returnnot 404 error request Uk" in {
    Get("/airports/getInfoByName?name=Uk") ~> restRouting.underlyingActor.route ~> check {
      status === NotFound
    }
  }
  it should
    "return Ukraine in get by id 302719" in {
    Get("/airports/getInfoById?id=302719") ~> restRouting.underlyingActor.route ~> check {
      status === OK
      val resp = parse(responseAs[String]).extract[CountryWithAirPorts]
      assert(resp.country.name == "Ukraine")
    }
  }
  }
