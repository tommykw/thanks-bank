package com.tommykw.thanks_bank.repository

import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.thanks_bank.model.*
import com.tommykw.thanks_bank.model.ThankReactionsTable.toThankReaction
import com.tommykw.thanks_bank.model.ThanksTable.toThank
import com.tommykw.thanks_bank.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ThankRepository {
    suspend fun getThanks(): List<Thank> {
        return dbQuery {
            ThanksTable.selectAll().map { toThank(it) }
        }
    }

    suspend fun getPostThanks(): List<Thank> {
        return dbQuery {
            ThanksTable.select {
                ThanksTable.slackPostId.isNull()
            }.map { toThank(it) }
        }
    }

    suspend fun getThank(id: Int): Thank {
        return dbQuery {
            ThanksTable.select {
                ThanksTable.id eq id
            }.map { toThank(it) }.single()
        }
    }

    suspend fun createThank(thanks: ThankRequest) {
        transaction {
            ThanksTable.insert {
                it[slackUserId] = thanks.slackUserId
                it[body] = thanks.body
                it[targetSlackUserId] = thanks.targetSlackUserId
            }
        }
    }

    suspend fun createThankReply(event: MessageEvent) {
        transaction {
            ThanksTable.insert {
                it[slackUserId] = event.user
                it[body] = event.text
                it[slackPostId] = event.ts
                it[parentSlackPostId] = event.threadTs
            }
        }
    }

    suspend fun createReaction(event: ReactionAddedEvent) {
        transaction {
            ThankReactionsTable.insert {
                it[slackUserId] = event.user
                it[slackPostId] = event.item.ts
                it[reactionName] = event.reaction
            }
        }
    }

    suspend fun updateSlackPostId(ts: String, thank: Thank) {
        transaction {
            ThanksTable.update({
                ThanksTable.id eq thank.id
            }) {
                it[slackPostId] = ts
            }
        }
    }

    suspend fun getThreads(slackPostId: String): List<Thank> {
        return dbQuery {
            ThanksTable.select {
                ThanksTable.parentSlackPostId eq slackPostId
            }.mapNotNull { toThank(it) }
        }
    }

    suspend fun getReactions(slackPostId: String): List<ThankReaction> {
        return dbQuery {
            ThankReactionsTable.select {
                ThankReactionsTable.slackPostId eq slackPostId
            }.mapNotNull { toThankReaction(it) }
        }
    }

    suspend fun removeReaction(event: ReactionRemovedEvent): Boolean {
        return dbQuery {
            ThankReactionsTable.deleteWhere {
                ThankReactionsTable.slackUserId eq event.user and
                (ThankReactionsTable.reactionName eq event.reaction) and
                (ThankReactionsTable.slackPostId eq event.item.ts)
            } > 0
        }
    }
}