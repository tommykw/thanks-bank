package com.tommykw.auth

import com.google.api.services.calendar.model.Event
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.apache.commons.codec.digest.DigestUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.FieldDescriptor

class Messaging {

    private val dateFormat = SimpleDateFormat("HH:mm").apply {
        timeZone = TimeZone.getTimeZone("Asia/Tokyo")
    }

    fun createJson(events: List<Event>): String {
        if (events.isEmpty()) {
            return Json.stringify(
                Message.serializer(),
                Message(text = "予定はありません")
            )
        }

        val attachments = events.map {
            Attachment(
                text = "${dateFormat.format(it.start.dateTime.value)}~${dateFormat.format(it.end.dateTime.value)}",
                fields = listOf(
                    Field(true, "場所", it.location),
                    Field(true, "作成者", it.creator.email.substringBefore("@"))
                ),
                color = createColor(it.location),
                title_link = it.htmlLink,
                title = it.summary,
                footer = it.attendees.fold("参加者") { s1, s2 -> "$s1 ${s2.email.substringBefore("@")}" }
            )
        }

        val message = Message(
            attachments,
            text = "予定"
        )

        return Json.stringify(Message.serializer(), message)
    }

    private fun createColor(input: String): String {
        val digest = DigestUtils.md5Hex(input)
        val r = digest.substring(1, 3).toInt(16)
        val g = digest.substring(3, 5).toInt(16)
        val b = digest.substring(5, 7).toInt(16)

        return "#" + "$r$b$g".substring(1, 7)
    }
}

@Serializable
data class Message(
    val attachements: List<Attachment>? = null,
    val channel: String? = null,
    val icon_emoji: String? = null,
    val text: String,
    val username: String? = null
)

@Serializable
data class Attachment(
    val author_icon: String? = null,
    val author_link: String? = null,
    val author_name: String? = null,
    val color: String? = null,
    val fallback: String? = null,
    val fields: List<Field>? = null,
    val footer: String? = null,
    val footer_icon: String? = null,
    val image_url: String? = null,
    val pretext: String? = null,
    val text: String? = null,
    val thumb_url: String? = null,
    val title: String? = null,
    val title_link: String? = null,
    val ts: Int? = null
)

@Serializable
data class Field(
    val short: Boolean? = null,
    val title: String? = null,
    val value: String? = null
)