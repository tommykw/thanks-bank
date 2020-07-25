package com.tommykw.model

import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

data class ThankReaction(
    val id: Int,
    val slackUserId: String,
    val reactionName: String
    // TODO created
    // TODO updated
) : Serializable

object ThankReactions: IntIdTable() {
    val slackUserId = varchar("slack_user_id", 255)
    val reactionName = varchar("reaction_name", 255)
}