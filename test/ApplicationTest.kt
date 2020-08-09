package com.tommykw.test

import com.tommykw.module
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRequests() = withTestApplication({ module(testing = true)}) {
        with(handleRequest(HttpMethod.Post, "/api/thank/daily")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("""{"status":"OK"}""", response.content)
        }
    }
}