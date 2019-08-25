package es.kix2902.santoral.data

import android.content.Context
import es.kix2902.santoral.data.database.AppDatabase
import es.kix2902.santoral.helpers.SingletonHolder
import es.kix2902.santoral.pad
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class DatabaseRepository private constructor(context: Context) {

    companion object : SingletonHolder<DatabaseRepository, Context>(::DatabaseRepository)

    private val db = AppDatabase.getInstance(context)

    fun insertDateSaved(month: Int, day: Int) {
        doAsync {
            val info = Model.QueryInfo(month, day, Date())
            db.infoDao().insert(info)
        }
    }

    fun getDateSavedForDay(month: Int, day: Int, onResult: (Date?) -> Unit) {
        doAsync {
            val data = db.infoDao().getQueryInfoForDate(month, day)

            val dateSaved = when {
                data.isNotEmpty() -> data[0].dateSaved
                else -> null
            }
            uiThread { onResult(dateSaved) }
        }
    }

    fun insertSaints(saints: List<Model.Saint>) {
        doAsync { db.saintsDao().insert(saints.toTypedArray()) }
    }

    fun getSaintsForDay(month: Int, day: Int, onResult: (List<Model.Saint>) -> Unit) {
        doAsync {
            val data = db.saintsDao().getAllSaintsForDate("${month.pad}-${day.pad}")
            uiThread { onResult(data) }
        }
    }

}