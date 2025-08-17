package org.deblock.exercise.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Configuration
    class AsyncConfig {
        @Bean("customTaskExecutor")
        fun customTaskExecutor(): TaskExecutor = ThreadPoolTaskExecutor().apply {
            corePoolSize = Runtime.getRuntime().availableProcessors() * 2
            maxPoolSize = 50
            queueCapacity = 1000
            setThreadNamePrefix("custom-task-executor-")
            initialize()
        }
    }
}