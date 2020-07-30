package com.tommykw.route

import com.tommykw.repository.PlaygroundRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@Location("/letter")
class Letter

fun Route.letter() {
    get<Letter> {
        val repository by kodein().instance<PlaygroundRepository>()
        val members = repository.getSlackMembers()
        val thanks = repository.getThanks()

        println("!!!!!!!!!!! members " + members)

        fun idToRealName(slackId: String): String {
            val res = members.members.find { it.id == slackId }
            return res?.real_name ?: ""
        }

        thanks.map { thank ->
            println("!!!!!!!!! slackUserId " + thank.slackUserId)
            println("!!!!!!!!! idToRealName " + idToRealName(thank.slackUserId))

            thank.copy(
                realName = idToRealName(thank.slackUserId)
            )
        }

        call.respond(
            FreeMarkerContent(
                "letter.ftl",
                mapOf(
                    "thanks" to thanks
                )
            )
        )
    }
}
