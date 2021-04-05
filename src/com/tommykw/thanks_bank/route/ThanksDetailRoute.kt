package com.tommykw.thanks_bank.route

import com.tommykw.thanks_bank.model.Thank
import com.tommykw.thanks_bank.model.ThankReaction
import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/thanks/{thankId}")
class ThanksDetailRoute(val thankId: Int)

fun Route.thanksDetailRouting(thankRepository: ThankRepository) {
    get<ThanksDetailRoute> { listing ->
        val thank = thankRepository.getThank(listing.thankId)
        val reactions: List<ThankReaction>
        val threads: List<Thank>

        if (thank.slackPostId == null) {
            reactions = emptyList()
            threads = emptyList()
        } else {
            reactions = thankRepository.getReactions(thank.slackPostId)
            threads = thankRepository.getThreads(thank.slackPostId)
        }

        call.respond(
            FreeMarkerContent(
                "thanks_detail.ftl",
                mapOf(
                    "thank" to thank,
                    "reactions" to reactions,
                    "threads" to threads,
                )
            )
        )
    }
}
