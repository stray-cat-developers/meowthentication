package org.straycats.meowthentication.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.hc.client5.http.classic.methods.HttpDelete
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPatch
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.classic.methods.HttpPut
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.message.BasicNameValuePair
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.straycats.meowthentication.api.common.ErrorCode
import org.straycats.meowthentication.api.common.NormalError
import org.straycats.meowthentication.api.config.CommunicationException
import org.straycats.meowthentication.api.config.GlobalErrorFormat
import java.nio.charset.Charset

open class RestClientSupport(
    private val objectMapper: ObjectMapper,
    private val writeLog: Boolean,
    val log: Logger,
) {

    private fun <T> T.toJson(): String = objectMapper.writeValueAsString(this)

    fun CloseableHttpClient.post(
        url: String,
        headers: List<Pair<String, Any>>,
        body: Any? = null,
    ): CloseableHttpResponse {
        val post = HttpPost(url).apply {
            body?.let { entity = StringEntity(it.toJson()) }
            headers.forEach { addHeader(it.first, it.second) }
        }

        return this.execute(post) {
            it as CloseableHttpResponse
        }
    }

    fun CloseableHttpClient.post(
        url: String,
        headers: List<Pair<String, Any>>,
        params: List<Pair<String, String>>? = null,
    ): CloseableHttpResponse {
        val post = HttpPost(url).apply {
            params?.map {
                BasicNameValuePair(it.first, it.second)
            }?.let {
                entity = UrlEncodedFormEntity(it)
            }

            headers.forEach {
                addHeader(it.first, it.second)
            }
        }

        return this.execute(post) {
            it as CloseableHttpResponse
        }
    }

    fun CloseableHttpClient.put(
        url: String,
        headers: List<Pair<String, Any>>,
        body: Any? = null,
    ): CloseableHttpResponse {
        val put = HttpPut(url).apply {
            body?.let {
                entity = StringEntity(it.toJson())
            }

            headers.forEach {
                addHeader(it.first, it.second)
            }
        }
        return this.execute(put) {
            it as CloseableHttpResponse
        }
    }

    fun CloseableHttpClient.patch(
        url: String,
        headers: List<Pair<String, Any>>,
        body: Any? = null,
    ): CloseableHttpResponse {
        val patch = HttpPatch(url).apply {
            body?.let {
                entity = StringEntity(it.toJson())
            }

            headers.forEach {
                addHeader(it.first, it.second)
            }
        }

        return this.execute(patch) {
            it as CloseableHttpResponse
        }
    }

    fun CloseableHttpClient.delete(
        url: String,
        headers: List<Pair<String, Any>>,
        params: List<Pair<String, Any?>>? = null,
    ): CloseableHttpResponse {
        val queryString = params?.joinToString("&") { "${it.first}=${it.second}" }
        val uri = if (queryString.isNullOrBlank().not()) url + queryString?.let { "?$it" } else url
        val delete = HttpDelete(uri).apply {
            headers.forEach {
                addHeader(it.first, it.second)
            }
        }

        return this.execute(delete) {
            it as CloseableHttpResponse
        }
    }

    fun CloseableHttpClient.get(
        url: String,
        headers: List<Pair<String, Any>>,
        params: List<Pair<String, Any?>>? = null,
    ): CloseableHttpResponse {
        val queryString = params?.joinToString("&") { "${it.first}=${it.second}" }
        val uri = if (queryString.isNullOrBlank().not()) url + queryString?.let { "?$it" } else url
        val get = HttpGet(uri).apply {
            headers.forEach {
                addHeader(it.first, it.second)
            }
        }

        return this.execute(get) {
            it as CloseableHttpResponse
        }
    }

    fun CloseableHttpResponse.orElseThrow(): String {
        val response = EntityUtils.toString(this.entity, Charset.defaultCharset())
        writeLog(response)

        if (this.isOK().not()) {
            val error = if (response.isNullOrEmpty()) {
                NormalError(ErrorCode.CT03, this.reasonPhrase)
            } else {
                try {
                    val globalErrorFormat = objectMapper.readValue<GlobalErrorFormat>(response)
                    NormalError(ErrorCode.C000, globalErrorFormat.message).apply {
                        refCode = globalErrorFormat.refCode
                        causeBy = mapOf(
                            "type" to globalErrorFormat.type,
                            "description" to globalErrorFormat.description,
                        )
                    }
                } catch (ex: Exception) {
                    NormalError(ErrorCode.CT03, response)
                }
            }

            throw CommunicationException(error)
        }

        return response
    }

    fun <T : ExternalServiceError> CloseableHttpResponse.orElseThrow(clazz: Class<T>): String {
        val response = EntityUtils.toString(this.entity, Charset.defaultCharset())
        writeLog(response)

        if (this.isOK().not()) {
            val error = if (response.isNullOrEmpty()) {
                NormalError(ErrorCode.C000, this.reasonPhrase)
            } else {
                val externalError = objectMapper.readValue(response, clazz)
                NormalError(ErrorCode.C000, externalError.message).apply {
                    refCode = externalError.code
                }
            }

            throw CommunicationException(error)
        }

        return response
    }

    private fun CloseableHttpResponse.isOK(): Boolean {
        return (HttpStatus.valueOf(this.code).is2xxSuccessful)
    }

    private fun writeLog(response: String) {
        if (writeLog) {
            log.info("-- response --\n$response")
        }
    }

    open class ExternalServiceError(
        val code: String,
        val message: String,
    )
}
