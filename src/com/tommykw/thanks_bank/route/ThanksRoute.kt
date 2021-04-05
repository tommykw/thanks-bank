package com.tommykw.thanks_bank.route

import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.repository.UserRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/thanks")
class ThanksRoute

fun Route.thanksRouting(
    thankRepository: ThankRepository,
    userRepository: UserRepository
) {
    get<ThanksRoute> {
        val thanks = thankRepository.getThanks()

        thanks.map { thank ->
            val user = userRepository.getUser(thank.slackUserId)

            user?.let {
                thank.realName = user.realName
                thank.userImage = user.userImage
            }

            thank.targetSlackUserId?.let {
                userRepository.getUser(thank.targetSlackUserId)?.let {
                    thank.targetRealName = it.realName
                    thank.targetUserImage = it.userImage
                }
            }

            thank.slackPostId?.let { slackPostId ->
                val threads = thankRepository.getThreads(slackPostId)
                thank.threadCount = threads.size
                thank.reactions = thankRepository.getReactions(slackPostId)
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
