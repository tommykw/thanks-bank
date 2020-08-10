package com.tommykw.thanks_bank.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.io.Serializable

data class Thank(
    val id: Int,
    val slackUserId: String,
    val body: String,
    val targetSlackUserId: String?,
    val slackPostId: String?,
    val parentSlackPostId: String?,
    var realName: String,
    var targetRealName: String,
    var userImage: String,
    var targetUserImage: String,
    var reactions: List<ThankReaction>,
    var threadCount: Int,
    val createdAt: DateTime?,
    val updatedAt: DateTime?
) : Serializable

object Thanks: IntIdTable() {
    val slackUserId = varchar("slack_user_id", 255)
    val body = text("body")
    val targetSlackUserId = varchar("target_slack_user_id", 255).nullable()
    val slackPostId = varchar("slack_post_id", 255).nullable()
    val parentSlackPostId = varchar("parent_slack_post_id", 255).nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    fun toThank(row: ResultRow): Thank {
        return Thank(
            id = row[id].value,
            slackUserId = row[slackUserId],
            body = row[body],
            targetSlackUserId = row[targetSlackUserId],
            realName = "",
            targetRealName = "",
            userImage = "",
            targetUserImage = "",
            slackPostId = row[slackPostId],
            parentSlackPostId = row[parentSlackPostId],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt],
            threadCount = 0,
            reactions = emptyList()
        )
    }
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
    val real_name: String,
    val profile: SlackUserProfile
)

@kotlinx.serialization.Serializable
data class SlackUserProfile(
    val image_24: String,
    val image_32: String,
    val image_48: String,
    val image_192: String,
    val image_512: String
)