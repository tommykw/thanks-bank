package com.tommykw.thanks_bank.util

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TaskScheduler(private val task: Runnable) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun start(every: Every) {
        val task = Runnable {
            task.run()
        }

        executor.scheduleWithFixedDelay(task, every.next, every.next, every.unit)
    }

    fun stop() {
        executor.shutdown()

        try {
            executor.awaitTermination(1, TimeUnit.HOURS)
        } catch (e: InterruptedException) {
        }
    }
}

data class Every(val next: Long, val unit: TimeUnit)