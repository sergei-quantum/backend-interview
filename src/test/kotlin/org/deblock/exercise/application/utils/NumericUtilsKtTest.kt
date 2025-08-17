package org.deblock.exercise.application.utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class NumericUtilsKtTest {
    @Test
    fun `should apply scale`() {
        BigDecimal("93.777").twoDigitsScale() shouldBe BigDecimal("93.78")
        BigDecimal("93.001").twoDigitsScale() shouldBe BigDecimal("93.00")
        BigDecimal("92").twoDigitsScale() shouldBe BigDecimal("92.00")
    }
}