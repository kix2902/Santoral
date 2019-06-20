package es.kix2902.santoral.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import es.kix2902.santoral.data.Model
import es.kix2902.santoral.helpers.SingletonHolder

@Database(entities = arrayOf(Model.Saint::class, Model.QueryInfo::class), version = 2)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saintsDao(): SaintsDao

    abstract fun infoDao(): InfoDao

    companion object : SingletonHolder<AppDatabase, Context>({
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE queryinfo ADD COLUMN language TEXT")
            }
        }

        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java,
            "Santoral.db"
        ).addMigrations(MIGRATION_1_2)
            .build()
    })
}
