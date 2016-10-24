package com.airportstat


case class Country(id: String, code: String ,name: String,
                   continent: String, wikipedia_link: String)


object Country {
  def apply(args: List[String]): Country = new Country(args(0), args(1), args(2),
    args(3), args(4))
}


case class AirPort(id: String, ident: String, ftype: String, name: String, latitudeDeg: String, longitudeDeg: String,
                   elevationFt: String, continent: String, isoCountry: String, isoRegion: String,
                   municipality: String, scheduledService: String, gpsCode: String, iataCode: String,
                   localCode: String, homeLink: String, wikipediaLink: String, keywords: String)

object AirPort {
  def apply(args: List[String]): AirPort = new AirPort(args(0), args(1), args(2), args(3), args(4),
    args(5), args(6), args(7), args(8), args(9), args(10), args(11), args(12),
    args(13), args(14), args(15), args(16), args(17))
}

case class Runway(id: String, airportRef: String, airportIdent: String, lengthFt: String, widthFt: String,
                  surface: String, lighted: String, closed: String, leIdent: String, leLatitudeDeg: String,
                  leLongitudeDeg: String, le_elevationFt: String, leHeadingDegT: String,
                  leDisplacedtThresholdFt: String, heIdent: String, heLatitudeDeg: String,
                  heLongitudeDeg: String, heElevationFt: String, heHeadingDegT: String,
                  heDisplacedThresholdFt: String)

object Runway {
  def apply(args: List[String]): Runway = new Runway(args(0), args(1), args(2), args(3), args(4), args(5),
    args(6), args(7), args(8), args(9), args(10), args(11), args(12), args(13), args(14), args(15), args(16),
    args(17), args(18), args(19))
}

case class AirportWithRunways(airport: AirPort, runways: List[Runway])

case class CountryWithAirPorts(country: String, airPortList: List[AirportWithRunways])
