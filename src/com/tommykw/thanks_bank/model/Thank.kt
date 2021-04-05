package com.tommykw.thanks_bank.model

import com.tommykw.thanks_bank.model.UsersTable.getUserBySlackUserId
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import java.io.Serializable

data class Thank(
    val id: Int,
    val slackUserId: String,
    val body: String,
    val targetSlackUserId: String?,
    val slackPostId: String?,
    val parentSlackPostId: String?,
    val realName: String,
    val targetRealName: String,
    val userImage: String,
    val targetUserImage: String,
    val threadCount: Int,
    val createdAt: DateTime,
    val updatedAt: DateTime
) : Serializable

object ThanksTable: IntIdTable(name = "thanks") {
    val slackUserId = varchar(name = "slack_user_id", length = 255)
    val body = text(name = "body")
    val targetSlackUserId = varchar(name = "target_slack_user_id", length = 255).nullable()
    val slackPostId = varchar(name = "slack_post_id", length = 255).nullable()
    val parentSlackPostId = varchar(name = "parent_slack_post_id", length = 255).nullable()
    val createdAt = datetime(name = "created_at").default(DateTime.now())
    val updatedAt = datetime(name = "updated_at").default(DateTime.now())

    fun getThreadCountBySlackPostId(slackPostId: String): Int {
        return ThanksTable.select {
            parentSlackPostId eq slackPostId
        }.count()
    }

    fun toThank(row: ResultRow): Thank {
        val user = getUserBySlackUserId(row[slackUserId])

        var targetRealName = ""
        var targetUserImage = ""

        row[targetSlackUserId]?.let {
            val targetUser = getUserBySlackUserId(row[targetSlackUserId]!!)

            targetRealName = targetUser.realName
            targetUserImage = targetUser.userImage
        }

        val threadCount = if (row[slackPostId] != null) {
            getThreadCountBySlackPostId(row[slackPostId]!!)
        } else {
            0
        }

        return Thank(
            id = row[id].value,
            slackUserId = row[slackUserId],
            body = row[body],
            targetSlackUserId = row[targetSlackUserId],
            realName = user.realName,
            targetRealName = targetRealName,
            userImage = user.userImage,
            targetUserImage = targetUserImage,
            slackPostId = row[slackPostId],
            parentSlackPostId = row[parentSlackPostId],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt],
            threadCount = threadCount,
        )
    }
}

data class ThankRequest(
    val slackUserId: String,
    val body: String,
    val targetSlackUserId: String,
)