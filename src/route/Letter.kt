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

        fun idToRealName(slackId: String): String {
            val res = members.members.find { it.id == slackId }
            return res?.real_name ?: ""
        }

        fun idToProfileImage(slackId: String): String {
            val res = members.members.find { it.id == slackId }
            return res?.profile?.image_512 ?: ""
        }

        thanks.map { thank ->
            thank.realName = idToRealName(thank.slackUserId)
            thank.targetSlackUserId?.let {
                thank.targetRealName = idToRealName(it)
            }
            thank.userImage = idToProfileImage(thank.slackUserId)
            thank.targetSlackUserId?.let {
                thank.targetUserImage = idToProfileImage(it)
            }

            thank.slackPostId?.let { slackPostId ->
                val threads = repository.getThreads(slackPostId)
                thank.threadCount = threads.size
                thank.reactions = repository.getReactions(slackPostId)
            }
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
