package com.tommykw.thanks_bank.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.io.Serializable

data class ThankReaction(
    val id: Int,
    val slackUserId: String,
    val reactionName: String,
    val slackPostId: String,
    val createdAt: DateTime,
    val updatedAt: DateTime
) : Serializable

object ThankReactionsTable: IntIdTable(name = "thank_reactions") {
    val slackUserId = varchar(name = "slack_user_id", length = 255)
    val reactionName = varchar(name = "reaction_name", length = 255)
    val slackPostId = varchar(name = "slack_post_id", length = 255)
    val createdAt = datetime(name = "created_at").default(DateTime.now())
    val updatedAt = datetime(name = "updated_at").default(DateTime.now())

    fun toThankReaction(row: ResultRow): ThankReaction {
        return ThankReaction(
            id = row[id].value,
            slackPostId = row[slackPostId],
            reactionName = row[reactionName],
            slackUserId = row[slackUserId],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt],
        )
    }
}