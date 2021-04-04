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

fun Route.thanksRouting(repository: ThankRepository) {
    get<ThanksRoute> {
        val thanks = repository.getThanks()

        thanks.map { thank ->
            val user = repository.getUser(thank.slackUserId)

            user?.let {
                thank.realName = user.realName
                thank.userImage = user.userImage
            }

            thank.targetSlackUserId?.let {
                repository.getUser(thank.targetSlackUserId)?.let {
                    thank.targetRealName = it.realName
                    thank.targetUserImage = it.userImage
                }
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
