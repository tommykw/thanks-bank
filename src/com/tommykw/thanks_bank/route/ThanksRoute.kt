package com.tommykw.thanks_bank.route

import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/thanks")
class ThanksRoute

fun Route.thanksRouting(thankRepository: ThankRepository) {
    get<ThanksRoute> {
        val thanks = thankRepository.getThanks()

        call.respond(
            FreeMarkerContent(
                "thanks.ftl",
                mapOf(
                    "thanks" to thanks
                )
            )
        )
    }
}
