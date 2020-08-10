package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.slack.api.model.event.MessageEvent
import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackMessageEvent(app: App, repository: ThankRepository) {
    app.event(MessageEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            launch {
                repository.createThankReply(event)
            }
        }

        ctx.ack()
    }
}