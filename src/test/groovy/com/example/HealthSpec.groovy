package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.management.health.indicator.DefaultHealthResult
import io.micronaut.management.health.indicator.HealthResult
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class HealthSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient

    void "/health endpoint is exposed"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

		when:
		HttpResponse exchange = client.exchange(HttpRequest.GET('/health'), DefaultHealthResult.class)

        then:
        noExceptionThrown()
		exchange.code() == 200

    }
}
