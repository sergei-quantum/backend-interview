package org.deblock.exercise.application.web

import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
class ExceptionsHandler {

    /**
     * Handles exceptions thrown by controllers.
     *
     * @param ex The thrown exception.
     * @return A [ResponseEntity] containing a map with an error message and the corresponding HTTP status.
     */
    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ResponseEntity<Map<String, String>> {
        return when (ex) {
            is MethodArgumentNotValidException -> {
                val message = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
                    .firstNotNullOfOrNull { it }
                    ?.value
                ex.toResponse(BAD_REQUEST, message)
            }
            is ValidationException -> ex.toResponse(BAD_REQUEST)
            else -> ex.toResponse(INTERNAL_SERVER_ERROR)
        }
    }

    private fun Exception.toResponse(httpStatus: HttpStatus, customMessage: String? = null): ResponseEntity<Map<String, String>> {
        val errorMessage = customMessage ?: message ?: "Something went wrong"
        return ResponseEntity(mapOf("message" to errorMessage), httpStatus)
    }
}