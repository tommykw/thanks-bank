package com.tommykw

import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.request.Request
import com.slack.api.bolt.request.RequestHeaders
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.util.QueryStringParser
import com.slack.api.bolt.util.SlackRequestParser
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.*
import com.slack.api.model.block.element.BlockElements.*
import com.tommykw.route.login
import com.tommykw.api.playgroundApi
import com.tommykw.model.UserSession
import com.tommykw.model.User
import com.tommykw.repository.DatabaseFactory
import com.tommykw.repository.InMemoryRepository
import com.tommykw.repository.PlaygroundRepository
import com.tommykw.route.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.features.origin
import io.ktor.freemarker.FreeMarker
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.locations
import io.ktor.request.*
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.toMap
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.kodein
import java.net.URI
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val appConfig = AppConfig()
val requestParser = SlackRequestParser(appConfig)
val app = App(appConfig)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    kodein {
        bind<PlaygroundRepository>() with singleton { PlaygroundRepository() }
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

    install(Sessions) {
        cookie<UserSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    val hashFunction = { s: String -> hash(s) }

    DatabaseFactory.init()

    val jwtService = JwtService()

    val inMemoryRepository = InMemoryRepository()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "playgrounds"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asString()
                val repository = PlaygroundRepository()
                val user = repository.userById(claimString)
                user
            }
        }
    }

    app.command("/ktor") { _, ctx ->
        ctx.ack(asBlocks(
            section { section ->
                section
                    .text(markdownText("Ktor is a framework for building asynchronous servers and clients in connected systems using the powerful Kotlin programming language."))
                    .accessory(
                        button { btn ->
                            btn.actionId("link")
                                .text(plainText("Ktor website"))
                                .url("kttps://ktor.io/")
                        }
                    )
            }
        ))
    }

    app.blockAction("link") { _, ctx ->
        ctx.ack()
    }

    app.command("/test-test") { req, ctx ->
        ctx.ack(asBlocks(
            section { section ->
                section
                    .text(markdownText("Hey ${req.payload.userName}. This is a test message with *bold* inside"))
                    .accessory(
                        staticSelect { staticSelect ->
                            staticSelect.actionId("test_action")
                            staticSelect.placeholder(plainText("Select an item"))
                            staticSelect.options(listOf(
                                option(plainText("Let's go!", true), "report_go"),
                                option(plainText("Snooze", true), "report_snooze"),
                                option(plainText("Not today", true), "report_today")
                            ))
                        }
                    )
            }
        ))
    }

    app.command("/thanks") { req, ctx ->
        ctx.ack(
            asBlocks(
//                section { section ->
//                    section.text(markdownText("あなたのありがと〜〜！を教えて!!"))
//                },
                //divider(),
                section { section ->
                    section
                        .text(markdownText("誰に届けますか？"))
                        .accessory(
                            multiUsersSelect { multiusersSelect ->
                                multiusersSelect.maxSelectedItems(10)
                                multiusersSelect.placeholder(plainText("選択してください"))
                            }
                        )
                },
                //divider(),
                section { section ->
                    section
                        .text(markdownText("メッセージをどうぞ"))
                        .accessory(
                            plainTextInput { input ->
                                input.minLength(5)
                                input.maxLength(500)
                                input.placeholder(plainText("なんでもいいよ"))
                            }
                        )
                }
                //divider()
//                actions { actions ->
//                    actions.elements(
//                        asElements(
//                            button { button ->
//                                button.text(plainText("送る"))
//                                button.value("summit")
//                            },
//                            button { button ->
//                                button.text(plainText("キャンセル"))
//                                button.value("cancel")
//                            }
//                        )
//                    )
//                }
            )
        )
    }

    app.blockAction("test_action") { _, ctx ->
        ctx.respond { r ->
            r.text("thx")
        }
        ctx.ack()
    }

    routing {
        hello()

        post("/slack/events") {
            respond(call, app.run(parseRequest(call)))
        }

        static("/static") {
            resources("images")
        }

        home(inMemoryRepository)
        about()
        playground(hashFunction)
        signin(hashFunction)
        signout()
        signup(hashFunction)
        login(jwtService)
        playgroundApi(inMemoryRepository)
        googleCalender()
    }
}

const val API_VERSION = "/api/v1"

suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}

fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

fun ApplicationCall.securityCode(date: Long, user: User, hashFunction: (String) -> String) =
    hashFunction("$date:${user.userId}:${request.host()}:${refererHost()}")

fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFunction: (String) -> String) =
    securityCode(date, user, hashFunction) == code &&
        (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }

val ApplicationCall.apiuser get() = authentication.principal<User>()

suspend fun parseRequest(call: ApplicationCall): Request<*> {
    val requestBody = call.receiveText()
    val queryString = QueryStringParser.toMap(call.request.queryString())
    val headers = RequestHeaders(call.request.headers.toMap())

    return requestParser.parse(
        SlackRequestParser.HttpRequest.builder()
            .requestUri(call.request.uri)
            .queryString(queryString)
            .requestBody(requestBody)
            .headers(headers)
            .remoteAddress(call.request.origin.remoteHost)
            .build()
    )
}

suspend fun respond(call: ApplicationCall, slackResp: Response) {
    for (header in slackResp.headers) {
        for (value in header.value) {
            call.response.header(header.key, value)
        }
    }

    call.response.status(HttpStatusCode.fromValue(slackResp.statusCode))
    if (slackResp.body != null) {
        call.respond(TextContent(slackResp.body, ContentType.parse(slackResp.contentType)))
    }
}