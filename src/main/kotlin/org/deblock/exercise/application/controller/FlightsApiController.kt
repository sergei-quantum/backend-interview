package org.deblock.exercise.application.controller

import org.deblock.exercise.application.service.FlightService
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightQuery
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/flights")
class FlightsApiController(private val flightService: FlightService) {

    @PostMapping("/find")
    fun findFlights(@RequestBody query: FlightQuery): List<Flight> {
//        TODO add validation
        return flightService.findFlights(query)
    }
}