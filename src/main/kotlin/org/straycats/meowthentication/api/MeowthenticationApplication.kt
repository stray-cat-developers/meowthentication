package org.straycats.meowthentication.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.straycats.meowthentication.api.config.AppEnvironment

@SpringBootApplication
@EnableConfigurationProperties(AppEnvironment::class)
class MeowthenticationApplication

fun main(args: Array<String>) {
    runApplication<MeowthenticationApplication>(*args)
}
