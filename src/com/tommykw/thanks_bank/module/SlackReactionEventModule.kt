package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.thanks_bank.repository.Repository
import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackReactionEvent(app: App, repository: ThankRepository) {

    app.event(ReactionAddedEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.item.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            launch {
                repository.createReaction(event)
            }
        }

        ctx.ack()
    }

    app.event(ReactionRemovedEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.item.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            launch {
                repository.removeReaction(event)
            }
        }

        ctx.ack()
    }
}