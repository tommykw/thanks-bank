package com.tommykw.model

import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

data class ThankReaction(
    val id: Int,
    val slackUserId: String,
    val reactionName: String,
    val slackPostId: String
    // TODO created
    // TODO updated
) : Serializable

object ThankReactions: IntIdTable() {
    val slackUserId = varchar("slack_user_id", 255)
    val reactionName = varchar("reaction_name", 255)
    val slackPostId = varchar("slack_post_id", 255)
}