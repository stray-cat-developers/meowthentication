package org.straycats.meowthentication.utils

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.util.TimeValue
import org.straycats.meowthentication.api.config.AppEnvironment
import java.util.concurrent.TimeUnit

object RestClient {

    fun new(connInfo: AppEnvironment.ConnInfo): CloseableHttpClient {
        val manager = PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnPerRoute(connInfo.perRoute)
            .setMaxConnTotal(connInfo.connTotal)
            .setDefaultConnectionConfig(
                ConnectionConfig.custom()
                    .setTimeToLive(TimeValue.ofSeconds(connInfo.connLiveDuration))
                    .setConnectTimeout(connInfo.connTimeout.toLong(), TimeUnit.SECONDS)
                    .setSocketTimeout(connInfo.readTimeout, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        return HttpClients.custom()
            .setConnectionManager(manager)
            .build()
    }
}
