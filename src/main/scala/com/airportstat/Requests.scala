package com.airportstat

trait BaseRequest

case class GetAirPortInfoByIdRequest(id: String) extends BaseRequest
case class GetAirPortInfoByNameRequest(name: String) extends BaseRequest
case object GetTopHighRequest extends BaseRequest
case object GetTopLowestRequest extends BaseRequest
case object GetRunwayTypeByCountry extends BaseRequest

case object CountryNotFoundById