package com.tommykw

import com.slack.api.Slack
import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.util.SlackRequestParser
import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.model.kotlin_extension.view.blocks
import com.slack.api.model.view.View
import com.slack.api.model.view.Views.*
import com.tommykw.api.workerThankApi
import com.tommykw.model.ThankRequest
import com.tommykw.repository.DatabaseFactory
import com.tommykw.repository.ThankRepository
import com.tommykw.route.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.routing
import kotlinx.coroutines.launch
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.kodein

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val appConfig = AppConfig()
val requestParser = SlackRequestParser(appConfig)
val app = App(appConfig)

val slack = Slack.getInstance()
val apiClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    kodein {
        bind<ThankRepository>() with singleton { ThankRepository() }
    }

    install(DefaultHeaders) {}

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(
                e.localizedMessage,
                ContentType.Text.Plain, HttpStatusCode.InternalServerError
            )
        }
    }

    install(ContentNegotiation) {
        gson()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    DatabaseFactory.init()

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

    app.command("/thanks") { req, ctx ->
        val res = ctx.client().viewsOpen {
            it.triggerId(ctx.triggerId)
            it.view(buildView(ctx))
        }

        if (res.isOk) {
            val ackRes = ctx.ack()

            val res = ctx.client().chatPostEphemeral {
                it.token(ctx.botToken)
                it.channel("#general")
                it.text("メッセージが送信されました")
            }
            // TODO res.isOk

            ackRes
        } else {
            Response.builder().statusCode(500).body(res.error).build()
        }
    }

    app.viewSubmission("thanks-message") { req, ctx ->
        val stateValues = req.payload.view.state.values
        val message = stateValues["message-block"]?.get("message-action")?.value
        val targetUsers = stateValues["user-block"]?.get("user-action")?.selectedUsers

        if (message?.isNotEmpty() == true && targetUsers?.isNotEmpty() == true) {
            try {
                launch {
                    val repository by kodein().instance<ThankRepository>()

                    targetUsers.forEach { targetUser ->
                        repository.createThank(
                            ThankRequest(
                                slackUserId = req.payload.user.id,
                                targetSlackUserId = targetUser,
                                body = message
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
            }
        }

        ctx.ack()
    }

    routing {
        static("/static") {
            resources("images")
            resources("css")
        }

        letter()
        letterDetail()
        slackEvent()

        workerThankApi(apiClient)
    }
}

fun buildView(ctx: SlashCommandContext): View {
    return view { view ->
        view.callbackId("thanks-message")
        view.type("modal")
        view.notifyOnClose(true)
        view.title(viewTitle { title -> title.type("plain_text").text("あなたのありがと〜〜！を教えて!!").emoji(true) })
        view.submit(viewSubmit { submit -> submit.type("plain_text").text("送信").emoji(true) } )
        view.close(viewClose { close -> close.type("plain_text").text("キャンセル").emoji(true) } )
        view.privateMetadata("{\"response_url\":\"https://hooks.slack.com/actions/T1ABCD2E12/330361579271/0dAEyLY19ofpLwxqozy3firz\"}")
        view.blocks {
            input {
                blockId("user-block")
                label(text = "🔛 誰に届けますか？", emoji = true)
                element {
                    multiUsersSelect {
                        actionId("user-action")
                        placeholder("選択してみよう")
                    }
                }
            }
            input {
                blockId("message-block")
                element {
                    plainTextInput {
                        actionId("message-action")
                        multiline(true)
                    }
                }
                label(text = "メッセージをどうぞ", emoji = true)
            }
        }
    }
}
