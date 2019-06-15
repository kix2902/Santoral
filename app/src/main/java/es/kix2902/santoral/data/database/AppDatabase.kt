package es.kix2902.santoral.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.kix2902.santoral.data.Model
import es.kix2902.santoral.helpers.SingletonHolder

@Database(entities = arrayOf(Model.Saint::class, Model.QueryInfo::class), version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saintsDao(): SaintsDao

    abstract fun infoDao(): InfoDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java,
            "Santoral.db"
        ).build()
    })
}
