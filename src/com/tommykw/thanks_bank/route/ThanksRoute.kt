package com.tommykw.thanks_bank.route

import com.tommykw.thanks_bank.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/thanks")
class ThanksRoute

fun Route.thanks(repository: Repository) {
    get<ThanksRoute> {
        val members = repository.getSlackMembers().members
        val thanks = repository.getThanks()

        fun idToRealName(slackId: String): String {
            val res = members.find { it.id == slackId }
            return res?.realName ?: ""
        }

        fun idToProfileImage(slackId: String): String {
            val res = members.find { it.id == slackId }
            return res?.profile?.image512 ?: ""
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
                "thanks.ftl",
                mapOf(
                    "thanks" to thanks,
                )
            )
        )
    }
}
