package org.straycats.meowthentication.api.domain.authentication.provider

import org.springframework.stereotype.Service

@Service
class AuthenticationProviderHandler(
    private val kakaoClient: SocialClient,
    private val naverClient: SocialClient,
    private val appleClient: SocialClient,
    private val googleClient: SocialClient,
) {
    fun getSocialClient(provider: SocialType): SocialClient {
        return when (provider) {
            SocialType.APPLE -> appleClient
            SocialType.NAVER -> naverClient
            SocialType.KAKAO -> kakaoClient
            SocialType.GOOGLE -> googleClient
        }
    }
}
