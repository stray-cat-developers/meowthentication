package org.straycats.meowthentication.api.domain.authentication.provider

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class SocialType {
    NAVER,
    KAKAO,
    APPLE,
    GOOGLE
}
