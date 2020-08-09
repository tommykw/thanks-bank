package com.tommykw.repository

import com.slack.api.Slack
import com.slack.api.methods.request.users.UsersListRequest
import com.slack.api.methods.response.users.UsersListResponse
import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class ThankRepository : Repository {
    override suspend fun getThanks(): List<Thank> {
        return transaction {
            Thanks.selectAll().map { toThank(it) }
        }
    }

    override suspend fun getThank(id: Int): Thank {
        return transaction {
            Thanks.select {
                Thanks.id eq id
            }.map { toThank(it) }.single()
        }
    }

    private fun toThank(row: ResultRow): Thank {
        return Thank(
            id = row[Thanks.id].value,
            slackUserId = row[Thanks.slackUserId],
            body = row[Thanks.body],
            targetSlackUserId = row[Thanks.targetSlackUserId],
            realName = "",
            targetRealName = "",
            userImage = "",
            targetUserImage = "",
            slackPostId = row[Thanks.slackPostId],
            parentSlackPostId = row[Thanks.parentSlackPostId],
            createdAt = row[Thanks.createdAt],
            updatedAt = row[Thanks.updatedAt],
            threadCount = 0,
            reactions = emptyList()
        )
    }

    override suspend fun createThank(thanks: ThankRequest) = DatabaseFactory.dbQuery {
        transaction {
            val insertStatement = Thanks.insert {
                it[slackUserId] = thanks.slackUserId
                it[body] = thanks.body
                it[targetSlackUserId] = thanks.targetSlackUserId
                it[createdAt] = DateTime()
                it[updatedAt] = DateTime()
            }
        }
        Unit
    }

    override suspend fun saveThankReply(event: MessageEvent) {
        transaction {
            val inserted = Thanks.insert {
                it[slackUserId] = event.user
                it[body] = event.text
                it[slackPostId] = event.ts
                it[parentSlackPostId] = event.threadTs
            }
        }
    }

    override suspend fun saveReaction(event: ReactionAddedEvent) {
        transaction {
            val inserted = ThankReactions.insert {
                it[slackUserId] = event.user
                it[slackPostId] = event.item.ts
                it[reactionName] = event.reaction
                it[createdAt] = DateTime()
                it[updatedAt] = DateTime()
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

        return apiClient.usersList(request)
    }

    override suspend fun updateSlackPostId(ts: String, thank: Thank) {
        transaction {
            val updated = Thanks.update({
                Thanks.id eq thank.id
            }) {
                it[slackPostId] = ts
            }
        }
    }

    override suspend fun getThreads(slackPostId: String): List<Thank> {
        return DatabaseFactory.dbQuery {
            Thanks.select {
                Thanks.parentSlackPostId eq slackPostId
            }.mapNotNull { toThank(it) }
        }
    }

    override suspend fun getReactions(slackPostId: String): List<ThankReaction> {
        return DatabaseFactory.dbQuery {
            ThankReactions.select {
                ThankReactions.slackPostId eq slackPostId
            }.mapNotNull { toThankReaction(it) }
        }
    }

    override suspend fun removeReaction(event: ReactionRemovedEvent) {
        return DatabaseFactory.dbQuery {
            ThankReactions.deleteWhere {
                ThankReactions.slackUserId eq event.user and
                (ThankReactions.reactionName eq event.reaction) and
                (ThankReactions.slackPostId eq event.item.ts)
            }
        }
    }

    private fun toThankReaction(row: ResultRow): ThankReaction {
        return ThankReaction(
            id = 1,
            slackPostId = row[ThankReactions.slackPostId],
            reactionName = row[ThankReactions.reactionName],
            slackUserId = row[ThankReactions.slackUserId],
            createdAt = row[ThankReactions.createdAt],
            updatedAt = row[ThankReactions.updatedAt]
        )
    }
}