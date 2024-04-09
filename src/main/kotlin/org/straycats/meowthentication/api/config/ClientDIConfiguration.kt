package org.straycats.meowthentication.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.straycats.meowthentication.api.domain.social.provider.DummySocialClient
import org.straycats.meowthentication.api.domain.social.provider.SocialClient
import org.straycats.meowthentication.api.domain.social.provider.apple.StableAppleClient
import org.straycats.meowthentication.api.domain.social.provider.google.StableGoogleClient
import org.straycats.meowthentication.api.domain.social.provider.kakao.StableKakaoClient
import org.straycats.meowthentication.api.domain.social.provider.naver.StableNaverClient
import org.straycats.meowthentication.utils.RestClient

@Configuration
class ClientDIConfiguration(
    appEnvironment: AppEnvironment
) {
    private val env = appEnvironment.client

    @Bean
    fun googleClient(): SocialClient {
        val env = env.google
        return if (env.useDummy) {
            DummySocialClient()
        } else {
            StableGoogleClient(env)
        }
    }

    @Bean
    fun kakaoClient(): SocialClient {
        val env = env.kakao
        return if (env.useDummy) {
            DummySocialClient()
        } else {
            val httpClient = RestClient.new(env)
            StableKakaoClient(env, httpClient)
        }
    }

    @Bean
    fun naverClient(): SocialClient {
        val env = env.naver
        return if (env.useDummy) {
            DummySocialClient()
        } else {
            val httpClient = RestClient.new(env)
            StableNaverClient(env, httpClient)
        }
    }

    @Bean
    fun appleClient(): SocialClient {
        val env = env.apple
        return if (env.useDummy) {
            DummySocialClient()
        } else {
            val httpClient = RestClient.new(env)
            StableAppleClient(env, httpClient)
        }
    }
}
