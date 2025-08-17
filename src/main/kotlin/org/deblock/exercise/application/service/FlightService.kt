package org.deblock.exercise.application.service

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.port.FlightSupplier
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync

/**
 * Service responsible for querying flight suppliers and aggregating flight results.
 */
@Service
class FlightService(
    private val suppliers: List<FlightSupplier>,
    @Qualifier("customTaskExecutor") private val taskExecutor: TaskExecutor
) {

    companion object {
        private val FARE_COMPARATOR: Comparator<Flight> = compareBy { it.fare }
    }

    /**
     * Finds flights from all suppliers concurrently based on the given [query] and returns
     * a list of [Flight] sorted by fare in ascending order.
     *
     * @param query The flight search query containing origin, destination, dates, and passenger count.
     * @return A list of [Flight] sorted by fare.
     */
    fun findFlights(query: FlightSearchQuery): List<Flight> {
        val futures = suppliers.map { supplier ->
            supplyAsync({ processQuery(supplier, query) }, taskExecutor)
        }
        return futures.awaitAll()
            .flatten()
            .sortedWith(FARE_COMPARATOR)
    }

    private fun processQuery(supplier: FlightSupplier, query: FlightSearchQuery): List<Flight> {
        return try {
            supplier.findFlights(query)
        } catch (e: Exception) {
            emptyList()
        }
    }


    private fun <T> List<CompletableFuture<T>>.awaitAll(): List<T> =
        CompletableFuture.allOf(*this.toTypedArray())
            .thenApply { _ -> this.map { it.join() } }
            .join()
}