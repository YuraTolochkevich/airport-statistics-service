package com.airportstat

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http


object Boot extends App {
  implicit val system = ActorSystem("airport-stats")

  val inData = DataLoader.initStatisticsData()
  if (inData.isFailure) {
    system.log.info("Couldn't load statistics data")
    sys.exit(1)
  }
  val requestProcessor = RequestProcessor(inData.get._1, inData.get._2, inData.get._3)

  val serviceActor = system.actorOf(Props(new RoutingAirportStatistics(requestProcessor)), name = "rest-airport-stats")

  system.registerOnTermination {
    system.log.info("Airport Service  shutdown.")
  }

  IO(Http) ! Http.Bind(serviceActor, "localhost", port = 10000)
}
