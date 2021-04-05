package com.tommykw.thanks_bank.route

import com.tommykw.thanks_bank.model.Thank
import com.tommykw.thanks_bank.model.ThankReaction
import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.repository.UserRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/thanks/{thankId}")
class ThanksDetailRoute(val thankId: Int)

fun Route.thanksDetailRouting(
    thankRepository: ThankRepository,
    userRepository: UserRepository
) {
    get<ThanksDetailRoute> { listing ->
        val thank = thankRepository.getThank(listing.thankId)
        var reactions: List<ThankReaction>? = null
        var threads: List<Thank>? = null

        thank.slackPostId?.let { slackPostId ->
            reactions = thankRepository.getReactions(slackPostId)
            threads = thankRepository.getThreads(slackPostId)
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

            threads?.map { thread ->
                userRepository.getUser(thread.slackUserId)?.let {
                    thread.realName = it.realName
                    thread.userImage = it.userImage
                }

                thread.targetSlackUserId?.let {
                    userRepository.getUser(thread.targetSlackUserId)?.let {
                        thread.targetRealName = it.realName
                        thread.userImage = it.userImage
                    }
                }
            }
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
