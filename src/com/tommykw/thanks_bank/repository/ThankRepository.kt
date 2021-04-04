package com.tommykw.thanks_bank.repository

import com.slack.api.Slack
import com.slack.api.methods.request.users.UsersListRequest
import com.slack.api.methods.response.users.UsersListResponse
import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.thanks_bank.model.*
import com.tommykw.thanks_bank.model.ThankReactionsTable.toThankReaction
import com.tommykw.thanks_bank.model.ThanksTable.toThank
import com.tommykw.thanks_bank.repository.DatabaseFactory.dbQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ThankRepository : Repository {
    override suspend fun getThanks(): List<Thank> {
        return dbQuery {
            ThanksTable.selectAll().map { toThank(it) }
        }
    }

    override suspend fun getPostThanks(): List<Thank> {
        return dbQuery {
            ThanksTable.select {
                ThanksTable.slackPostId.isNull()
            }.map { toThank(it) }
        }
    }

    override suspend fun getThank(id: Int): Thank {
        return dbQuery {
            ThanksTable.select {
                ThanksTable.id eq id
            }.map { toThank(it) }.single()
        }
    }

    override suspend fun createThank(thanks: ThankRequest) {
        transaction {
            ThanksTable.insert {
                it[slackUserId] = thanks.slackUserId
                it[body] = thanks.body
                it[targetSlackUserId] = thanks.targetSlackUserId
            }
        }
    }

    override suspend fun createThankReply(event: MessageEvent) {
        transaction {
            ThanksTable.insert {
                it[slackUserId] = event.user
                it[body] = event.text
                it[slackPostId] = event.ts
                it[parentSlackPostId] = event.threadTs
            }
        }
    }

    override suspend fun createReaction(event: ReactionAddedEvent) {
        transaction {
            ThankReactionsTable.insert {
                it[slackUserId] = event.user
                it[slackPostId] = event.item.ts
                it[reactionName] = event.reaction
            }
        }
    }

    suspend fun createUser(request: UserRequest) {
        transaction {
            UsersTable.insert {
                it[slackUserId] = request.slackUserId
                it[realName] = request.realName
                it[userImage] = request.userImage
            }
        }
    }

    suspend fun getUsers(): List<User> {
        return dbQuery {
            UsersTable.selectAll().map { UsersTable.toUser(it) }
        }
    }

    suspend fun getUser(slackUserId: String): User? {
        return dbQuery {
            UsersTable.select {
                UsersTable.slackUserId eq slackUserId
            }.map { UsersTable.toUser(it) }.singleOrNull()
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
            ThanksTable.update({
                ThanksTable.id eq thank.id
            }) {
                it[slackPostId] = ts
            }
        }
    }

    override suspend fun getThreads(slackPostId: String): List<Thank> {
        return dbQuery {
            ThanksTable.select {
                ThanksTable.parentSlackPostId eq slackPostId
            }.mapNotNull { toThank(it) }
        }
    }

    override suspend fun getReactions(slackPostId: String): List<ThankReaction> {
        return dbQuery {
            ThankReactionsTable.select {
                ThankReactionsTable.slackPostId eq slackPostId
            }.mapNotNull { toThankReaction(it) }
        }
    }

    override suspend fun removeReaction(event: ReactionRemovedEvent): Boolean {
        return dbQuery {
            ThankReactionsTable.deleteWhere {
                ThankReactionsTable.slackUserId eq event.user and
                (ThankReactionsTable.reactionName eq event.reaction) and
                (ThankReactionsTable.slackPostId eq event.item.ts)
            } > 0
        }
    }
}