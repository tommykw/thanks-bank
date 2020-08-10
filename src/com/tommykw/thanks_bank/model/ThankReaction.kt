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
    val createdAt: DateTime?,
    val updatedAt: DateTime?
) : Serializable

object ThankReactions: IntIdTable() {
    val slackUserId = varchar("slack_user_id", 255)
    val reactionName = varchar("reaction_name", 255)
    val slackPostId = varchar("slack_post_id", 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    fun toThankReaction(row: ResultRow): ThankReaction {
        return ThankReaction(
            id = 1,
            slackPostId = row[slackPostId],
            reactionName = row[reactionName],
            slackUserId = row[slackUserId],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt]
        )
    }
}