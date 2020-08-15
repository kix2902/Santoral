package es.kix2902.santoral.data

import android.content.Context
import es.kix2902.santoral.data.database.AppDatabase
import es.kix2902.santoral.helpers.SingletonHolder
import es.kix2902.santoral.pad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DatabaseRepository private constructor(context: Context) {

    companion object : SingletonHolder<DatabaseRepository, Context>(::DatabaseRepository)

    private val db = AppDatabase.getInstance(context)

    fun insertDateSaved(month: Int, day: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val info = Model.QueryInfo(month, day, Date())
            db.infoDao().insert(info)
        }
    }

    fun getDateSavedForDay(month: Int, day: Int, onResult: (Date?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = db.infoDao().getQueryInfoForDate(month, day)

            val dateSaved = when {
                data.isNotEmpty() -> data[0].dateSaved
                else -> null
            }
            withContext(Dispatchers.Main) { onResult(dateSaved) }
        }
    }

    fun insertSaints(saints: List<Model.Saint>) {
        GlobalScope.launch(Dispatchers.IO) { db.saintsDao().insert(saints.toTypedArray()) }
    }

    fun getSaintsForDay(month: Int, day: Int, onResult: (List<Model.Saint>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = db.saintsDao().getAllSaintsForDate("${month.pad}-${day.pad}")
            withContext(Dispatchers.Main) { onResult(data) }
        }
    }

}