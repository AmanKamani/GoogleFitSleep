package jb.production.fitsleep.models

import java.time.Instant
import java.time.ZoneId

abstract class BaseSleepData {

    fun getDateString(seconds: Long): String {
        return Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault()).toLocalDate()
            .toString()
    }

    fun getTimeString(seconds: Long): String {
        return Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault()).toLocalTime()
            .toString()
    }
}