package com.tommykw.route

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/letter/detail")
class LetterDetail

fun Route.letterDetail() {
    get<LetterDetail> {
        call.respond(
            FreeMarkerContent(
                "letter_detail.ftl",
                mapOf<Any, Any>()
            )
        )
    }
}
