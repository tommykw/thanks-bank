package com.tommykw.route

import com.tommykw.model.Thank
import com.tommykw.model.ThankReaction
import com.tommykw.repository.ThankRepository
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
        val repository by kodein().instance<ThankRepository>()
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
