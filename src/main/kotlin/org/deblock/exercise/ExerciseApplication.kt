package org.deblock.exercise

import org.deblock.exercise.infrastructure.config.CrazyAirConfig
import org.deblock.exercise.infrastructure.config.ToughJetConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    CrazyAirConfig::class,
    ToughJetConfig::class
)
class ExerciseApplication

fun main(args: Array<String>) {
    runApplication<ExerciseApplication>(*args)
}
