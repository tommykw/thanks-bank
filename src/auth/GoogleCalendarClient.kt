package com.tommykw.auth

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.FileReader
import java.util.*

object GoogleCalendarClient {

    private fun getCalendarIds(): List<String> {
        val calendarIds = System.getenv("GOOGLE_CALENDAR_IDS")

        return when (calendarIds.isNullOrBlank()) {
            true -> {
                val prop = Properties()
                val propertiesFile = System.getProperty("user.dir") + "/calendars.properties"
                val reader = FileReader(propertiesFile)
                prop.load(reader)
                prop["calendars"] as String
            }
            false -> {
                calendarIds
            }
        }.split(",")
    }

    suspend fun getEvents(): List<Event> {
        val now = DateTime(System.currentTimeMillis())
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val googleAuth = GoogleAuth()
        val credential = googleAuth.getCredential(httpTransport, jsonFactory)

        val calendar = Calendar.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName("iikoto")
            .build()

        val calendarIds = getCalendarIds()

        return withContext(Dispatchers.IO) {
            calendarIds.map { calendarId ->
                async {
                    try {
                        calendar.events().list(calendarId)
                            .setMaxResults(4)
                            .setTimeMin(now)
                            .setTimeMax(DateTime(now.value + 1000L * 60L * 60L * 2L))
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute()
                            .items
                    } catch (e: Exception) {
                        emptyList<Event>()
                    }
                }
            }.awaitAll().flatten()
        }
    }
}