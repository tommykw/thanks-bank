package com.tommykw.module

import com.slack.api.bolt.App
import com.slack.api.model.event.MessageEvent
import com.tommykw.repository.ThankRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackMessageEvent(app: App) {
    app.event(MessageEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            val repository = ThankRepository()
            launch {
                repository.saveThankReply(event)
            }
        }

        ctx.ack()
    }
}