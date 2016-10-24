package com.airportstat

case class RequestProcessor(countriesSource: List[Country],
                            airPortSource: List[AirPort], runwaysSource: List[Runway]) {

  var countries: Map[String, Country] = countriesSource.map( x=>(x.id, x)).toMap
  var airports: Map[String, List[AirPort]] = airPortSource  groupBy {_.isoCountry}
  var runways: Map[String, List[Runway]] = runwaysSource groupBy {x=>x.airportIdent}

  def getOptionByName(name: String): Option[CountryWithAirPorts] = {
    val filteredCountry = countries.filter(_._2.name.toLowerCase.startsWith(name.toLowerCase) ).toList
    if (filteredCountry.size!=1) None else getOptionById(filteredCountry(0)._2.id)
  }

  def getTopHighTenCountires = getTopTenCountries(_._2 > _._2)

  def getTopLowTenCountires = getTopTenCountries(_._2 < _._2)

  def getTopTenCountries(sorfFunction: ((String, Int), (String, Int))=> Boolean) = {
    val groupedTopTen = airports.toList.map(x=>(x._1, x._2.length)).sortWith(sorfFunction).take(10)
    val countryByName = countries.map(x=>(x._2.code, x._2.name))
    groupedTopTen map {x=>(countryByName.get(x._1).get, x._2)}
  }

  def getRunWayTypePerCountry = {
    countries map { x=>(x._2.name, getOptionById(x._2.id) match {
      case Some(x)=> x.airPortList.flatMap(y=>y. runways.map(_.surface)).toSet
      case None => None
    }) }
  }

  def getOptionById(id: String): Option[CountryWithAirPorts] = {
    val country = countries.get(id)
    country flatMap {x=> getAirportsRunwayInfo(x)}
  }

  def getAirportsRunwayInfo(x: Country): Option[CountryWithAirPorts] = {

    def extractRunways(air: List[AirPort]): Option[CountryWithAirPorts] = {
      val runs = air map { a =>
        val rways = runways.get(a.ident) match {
          case Some(x) => x
          case None => List()
        }
        AirportWithRunways(a, rways)
      }
      Option(CountryWithAirPorts(x.name, runs))
    }
    airports.get(x.code) flatMap { air => extractRunways(air) }
  }
}
