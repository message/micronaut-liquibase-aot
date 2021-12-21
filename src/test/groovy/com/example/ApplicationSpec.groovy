package com.example

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class ApplicationSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void 'application is running'() {
        expect:
        application.running
    }

}
