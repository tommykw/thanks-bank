package com.tommykw.thanks_bank.test

import com.tommykw.thanks_bank.module
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRequests() = withTestApplication({ module(testing = true) }) {

        with(handleRequest(HttpMethod.Get, "/thanks")) {
            assertEquals(HttpStatusCode.OK, response.status())

            val content = response.content
            assertNotNull(content)

            val lines = content.lines()
            assertEquals(lines[4].trim(), "<title>サンクス一覧</title>")
        }

        with(handleRequest(HttpMethod.Get, "/thanks/1")) {
            assertEquals(HttpStatusCode.OK, response.status())

            val content = response.content
            assertNotNull(content)

            val lines = content.lines()
            assertEquals(lines[4].trim(), "<title>サンクス詳細</title>")
        }
    }
}