package com.tommykw.module

import com.slack.api.bolt.App
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.event.ReactionRemovedEvent
import com.tommykw.repository.ThankRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackReactionAddedEvent(app: App) {
    app.event(ReactionAddedEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.item.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            val repository = ThankRepository()
            launch {
                repository.saveReaction(event)
            }
        }

        ctx.ack()
    }

    app.event(ReactionRemovedEvent::class.java) { payload, ctx ->
        println("!!!!!!!!!! removed")
        val event = payload.event

        if (event.item.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            println("!!!!!!!!!! removed2 " + event.reaction)
            val repository = ThankRepository()
            launch {
                repository.removeReaction(event.reaction)
            }
        }

        ctx.ack()
    }
}