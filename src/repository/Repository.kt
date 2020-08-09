package com.tommykw.repository

import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.model.*

interface Repository {
    suspend fun getThanks(): List<Thank>
    suspend fun getThank(id: Int): Thank
    suspend fun createThank(thanks: ThankRequest)
    suspend fun getSlackMembers(): SlackUserRes
    suspend fun saveReaction(event: ReactionAddedEvent)
    suspend fun removeReaction(event: ReactionRemovedEvent)
    suspend fun saveThankReply(event: MessageEvent)
    suspend fun updateSlackPostId(ts: String, thank: Thank)
    suspend fun getThreads(slackPostId: String): List<Thank>
    suspend fun getReactions(slackPostId: String): List<ThankReaction>
}