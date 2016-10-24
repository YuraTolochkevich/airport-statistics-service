package com.airportstat

import scala.io.Source
import scala.util.Try

trait DataObject[T] {
  def get(args: List[String]): T
}

object Maker {
  implicit object CountryMaker extends DataObject[Country] {
    override def get(args: List[String]) = Country(args)
  }

  implicit object AirPortMaker extends DataObject[AirPort] {
    override def get(args: List[String]) = AirPort(args)
  }

  implicit object RunwayMaker extends DataObject[Runway] {
    override def get(args: List[String]) = Runway(args)
  }
}

object DataLoader {
  val splitParameter = ","

  def loadStatData[T](inputFile: String) (implicit f: DataObject[T]): Try[List[T]] = {
    val lines: Try[List[String]] = loadCSVFile(inputFile)
    lines.flatMap { records => Try { records map {  record =>

      val splits = record.replaceAll("\"", "").split(splitParameter, -1).toList
      implicitly[DataObject[T]].get(splits)}
      }
    }
  }
  def loadCSVFile(fileName: String): Try[List[String]] = {
    Try {
      val source = Source.fromURL(getClass.getResource(fileName))
      source.getLines().toList drop 1
    }
  }

  def initStatisticsData(): Try[(List[Country], List[AirPort], List[Runway])] = {
    import Maker._
    for {
      cn <- loadStatData[Country]("/countries.csv")
      ap <- loadStatData[AirPort]("/airports.csv")
      rw <- loadStatData[Runway]("/runways.csv")
    } yield (cn, ap, rw)
  }
}

