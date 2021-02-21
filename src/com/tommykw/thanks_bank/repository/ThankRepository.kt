package com.tommykw.thanks_bank.repository

import com.slack.api.Slack
import com.slack.api.methods.request.users.UsersListRequest
import com.slack.api.methods.response.users.UsersListResponse
import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.thanks_bank.model.*
import com.tommykw.thanks_bank.model.ThankReactions.toThankReaction
import com.tommykw.thanks_bank.model.Thanks.toThank
import com.tommykw.thanks_bank.repository.DatabaseFactory.dbQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ThankRepository : Repository {
    override suspend fun getThanks(): List<Thank> {
        return dbQuery {
            Thanks.selectAll().map { toThank(it) }
        }
    }

    override suspend fun getThank(id: Int): Thank {
        return dbQuery {
            Thanks.select {
                Thanks.id eq id
            }.map { toThank(it) }.single()
        }
    }

    override suspend fun createThank(thanks: ThankRequest) {
        transaction {
            Thanks.insert {
                it[slackUserId] = thanks.slackUserId
                it[body] = thanks.body
                it[targetSlackUserId] = thanks.targetSlackUserId
            }
        }
    }

    override suspend fun createThankReply(event: MessageEvent) {
        transaction {
            Thanks.insert {
                it[slackUserId] = event.user
                it[body] = event.text
                it[slackPostId] = event.ts
                it[parentSlackPostId] = event.threadTs
            }
        }
    }

    override suspend fun createReaction(event: ReactionAddedEvent) {
        transaction {
            ThankReactions.insert {
                it[slackUserId] = event.user
                it[slackPostId] = event.item.ts
                it[reactionName] = event.reaction
            }
        }
    }

    override suspend fun getSlackMembers(): UsersListResponse {
        val slack = Slack.getInstance()
        val apiClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))

        val request = UsersListRequest
            .builder()
            .token(System.getenv("SLACK_BOT_TOKEN"))
            .build()

        return withContext(Dispatchers.IO) {
            apiClient.usersList(request)
        }
    }

    override suspend fun updateSlackPostId(ts: String, thank: Thank) {
        transaction {
            Thanks.update({
                Thanks.id eq thank.id
            }) {
                it[slackPostId] = ts
            }
        }
    }

    override suspend fun getThreads(slackPostId: String): List<Thank> {
        return dbQuery {
            Thanks.select {
                Thanks.parentSlackPostId eq slackPostId
            }.mapNotNull { toThank(it) }
        }
    }

    override suspend fun getReactions(slackPostId: String): List<ThankReaction> {
        return dbQuery {
            ThankReactions.select {
                ThankReactions.slackPostId eq slackPostId
            }.mapNotNull { toThankReaction(it) }
        }
    }

    override suspend fun removeReaction(event: ReactionRemovedEvent): Boolean {
        return dbQuery {
            ThankReactions.deleteWhere {
                ThankReactions.slackUserId eq event.user and
                (ThankReactions.reactionName eq event.reaction) and
                (ThankReactions.slackPostId eq event.item.ts)
            } > 0
        }
    }
}