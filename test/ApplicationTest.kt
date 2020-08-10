package com.tommykw.thanks_bank.test

import com.tommykw.thanks_bank.module
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRequests() = withTestApplication({ module(testing = true) }) {
        with(handleRequest(HttpMethod.Post, "/api/thank/daily")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("""{"status":"OK"}""", response.content)
        }

        with(handleRequest(HttpMethod.Get, "/thanks")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/thanks/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}