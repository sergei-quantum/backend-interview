package org.deblock.exercise.application.web

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.validation.ValidationException
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import java.util.stream.Stream

class ExceptionsHandlerTest {

    private val handler = ExceptionsHandler()

    companion object {
        @JvmStatic
        fun exceptions(): Stream<Arguments> {
            val bindingResult = mockk<BindingResult>()
            val fieldError = FieldError("objectName", "field", "field must not be blank")
            every { bindingResult.fieldErrors } returns listOf(fieldError)
            return Stream.of(
                Arguments.of(
                    RuntimeException("unexpected exception"),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "unexpected exception"
                ),
                Arguments.of(
                    MethodArgumentNotValidException(mockk<MethodParameter>(), bindingResult),
                    HttpStatus.BAD_REQUEST,
                    "field must not be blank"
                ),
                Arguments.of(
                    ValidationException("origin and destination must not be equal"),
                    HttpStatus.BAD_REQUEST,
                    "origin and destination must not be equal"
                ),
                Arguments.of(
                    ValidationException(),
                    HttpStatus.BAD_REQUEST,
                    "Something went wrong"
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("exceptions")
    fun `should return correct error message and status`(ex: Exception, status: HttpStatus, errorMessage: String) {
        val response = handler.handle(ex)
        response.statusCode shouldBe status
        response.body!!["message"] shouldBe errorMessage
    }
}