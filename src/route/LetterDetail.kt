package com.tommykw.route

import com.tommykw.model.Thank
import com.tommykw.model.ThankReaction
import com.tommykw.repository.PlaygroundRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@Location("/letter/{thankId}")
class LetterDetail(val thankId: Int)

fun Route.letterDetail() {
    get<LetterDetail> { listing ->
        val repository by kodein().instance<PlaygroundRepository>()
        val thank = repository.getThank(listing.thankId)
        var reactions: List<ThankReaction>? = null
        var threads: List<Thank>? = null

        thank.slackPostId?.let { slackPostId ->
            reactions = repository.getReactions(slackPostId)
            threads = repository.getThreads(slackPostId)
        }

        call.respond(
            FreeMarkerContent(
                "letter_detail.ftl",
                mapOf(
                    "thank" to thank,
                    "reactions" to reactions,
                    "threads" to threads
                )
            )
        )
    }
}
