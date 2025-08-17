package org.deblock.exercise.domain.port

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery

/**
 * Represents a supplier of flight information (e.g., external airline APIs or aggregators).
 * Implementations of this interface are responsible for fetching flight offers
 * from their specific data source and mapping them into the unified [Flight] model.
 */
interface FlightSupplier {

    /**
     * Finds available flights for the given search query.
     *
     * @param query the [FlightSearchQuery] containing the search criteria,
     *              such as origin, destination, departure and return dates,
     *              and the number of passengers.
     * @return a list of [Flight] objects matching the query.
     *         The list may be empty if no flights are found.
     * @throws RuntimeException if the supplier cannot process the request
     *         (e.g., remote API errors or invalid query parameters).
     */
    fun findFlights(query: FlightSearchQuery): List<Flight>
}