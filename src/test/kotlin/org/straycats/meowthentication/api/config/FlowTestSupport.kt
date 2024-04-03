package org.straycats.meowthentication.api.config

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.straycats.meowthentication.api.MeowthenticationApplication

@ActiveProfiles("embedded")
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [MeowthenticationApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class FlowTestSupport {

    @Autowired
    final lateinit var mockMvc: MockMvc
}
