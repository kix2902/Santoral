package es.kix2902.santoral.data

import android.content.Context
import androidx.room.Room
import es.kix2902.santoral.data.database.AppDatabase
import es.kix2902.santoral.data.threads.DefaultExecutorSupplier
import es.kix2902.santoral.helpers.SingletonHolder
import es.kix2902.santoral.pad
import java.util.*

class DatabaseRepository private constructor(context: Context) {

    companion object : SingletonHolder<DatabaseRepository, Context>(::DatabaseRepository)

    private val executor = DefaultExecutorSupplier
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "santoral.db"
    ).build()

    fun insertDateSaved(month: Int, day: Int) {
        executor.forBackgroundTasks().execute {
            val info = Model.QueryInfo(month, day, Date())
            db.infoDao().insert(info)
        }
    }

    fun getDateSavedForDay(month: Int, day: Int, onResult: (Date?) -> Unit) {
        executor.forBackgroundTasks().execute {
            val data = db.infoDao().getQueryInfoForDate(month, day)

            val dateSaved = when {
                data.isNotEmpty() -> data[0].dateSaved
                else -> null
            }
            executor.forMainThreadTasks().execute {
                onResult(dateSaved)
            }
        }
    }

    fun insertSaints(saints: List<Model.Saint>) {
        executor.forBackgroundTasks().execute {
            db.saintsDao().insert(saints.toTypedArray())
        }
    }

    fun getSaintsForDay(month: Int, day: Int, onResult: (List<Model.Saint>) -> Unit) {
        executor.forBackgroundTasks().execute {
            val list = db.saintsDao().getAllSaintsForDate("${month.pad}-${day.pad}")
            executor.forMainThreadTasks().execute {
                onResult(list)
            }
        }
    }

}