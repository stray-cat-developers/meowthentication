package org.straycats.meowthentication.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration

@Configuration
class WebConfiguration : DelegatingWebMvcConfiguration() {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(AuthorizationHeaderConverter())
        super.addFormatters(registry)
    }
}
