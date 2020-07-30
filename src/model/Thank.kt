package com.tommykw.model

import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

data class Thank(
    val id: Int,
    val slackUserId: String,
    val body: String,
    val targetSlackUserId: String,
    var realName: String
    // TODO created
    // TODO updated
) : Serializable

object Thanks: IntIdTable() {
    val slackUserId = varchar("slack_user_id", 255)
    val body = text("body")
    val targetSlackUserId = varchar("target_slack_user_id", 255)
}

@kotlinx.serialization.Serializable
data class SlackUserRes(
    val ok: Boolean,
    val members: List<SlackUser>
)

@kotlinx.serialization.Serializable
data class SlackUser(
    val id: String,
    val team_id: String,
    val real_name: String
)