package es.kix2902.santoral.data.database

import androidx.room.TypeConverter
import java.util.*

object DateTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toDate(timestamp: Long?) = timestamp?.let { Date(timestamp) }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(date: Date?) = date?.time

}