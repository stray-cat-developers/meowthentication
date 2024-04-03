package org.straycats.meowthentication.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppEnvironment {

    val client = Client()
    val token = Token()

    class Token {
        val jwt = JWT()
        class JWT {
            var ttlInSeconds: Int = 600
            var refreshableInSeconds: Int = 3600 * 24 * 30
            var leewayInMils: Int = 1000
            lateinit var issuer: String
            lateinit var secret: String
        }
    }

    class Client {
        var kakao: Kakao = Kakao()
        var naver: Naver = Naver()
        var apple: Apple = Apple()
        var google: Google = Google()

        class Naver : ConnInfo() {
            lateinit var authHost: String
            lateinit var clientId: String
            lateinit var clientSecret: String
        }

        class Kakao : ConnInfo() {
            lateinit var authHost: String
            lateinit var clientId: String
            lateinit var clientSecret: String
        }

        class Apple : ConnInfo() {
            lateinit var clientId: String
            lateinit var developerTeamId: String
            lateinit var developerKeyId: String
            lateinit var clientSecretPublicKey: String
            lateinit var clientSecretPrivateKey: String
        }

        class Google : ConnInfo() {
            lateinit var clientId: String
            lateinit var clientSecret: String
        }
    }

    open class ConnInfo {
        var host: String = "localhost"
        var connTimeout: Int = 1000
        var readTimeout: Int = 2000
        var logging: Boolean = false
        var useDummy: Boolean = false
        var perRoute: Int = 50
        var connTotal: Int = 500
        var connLiveDuration: Long = 60
    }
}
