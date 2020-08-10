package com.tommykw.thanks_bank.route

import com.tommykw.thanks_bank.model.Thank
import com.tommykw.thanks_bank.model.ThankReaction
import com.tommykw.thanks_bank.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/thanks/{thankId}")
class ThanksDetailRoute(val thankId: Int)

fun Route.thanksDetail(repository: Repository) {
    get<ThanksDetailRoute> { listing ->
        val thank = repository.getThank(listing.thankId)
        val members = repository.getSlackMembers().members
        var reactions: List<ThankReaction>? = null
        var threads: List<Thank>? = null

        fun idToRealName(slackId: String): String {
            val res = members.find { it.id == slackId }
            return res?.realName ?: ""
        }

        fun idToProfileImage(slackId: String): String {
            val res = members.find { it.id == slackId }
            return res?.profile?.image512 ?: ""
        }

        thank.slackPostId?.let { slackPostId ->
            reactions = repository.getReactions(slackPostId)
            threads = repository.getThreads(slackPostId)

            thank.realName = idToRealName(thank.slackUserId)
            thank.targetSlackUserId?.let {
                thank.targetRealName = idToRealName(it)
            }
            thank.userImage = idToProfileImage(thank.slackUserId)
            thank.targetSlackUserId?.let {
                thank.targetUserImage = idToProfileImage(it)
            }

            threads?.map { thread ->
                thread.realName = idToRealName(thread.slackUserId)
                thread.targetSlackUserId?.let {
                    thread.targetRealName = idToRealName(it)
                }
                thread.userImage = idToProfileImage(thread.slackUserId)
                thread.targetSlackUserId?.let {
                    thread.targetUserImage = idToProfileImage(it)
                }
            }
        }

        call.respond(
            FreeMarkerContent(
                "thanks_detail.ftl",
                mapOf(
                    "thank" to thank,
                    "reactions" to reactions,
                    "threads" to threads
                )
            )
        )
    }
}
