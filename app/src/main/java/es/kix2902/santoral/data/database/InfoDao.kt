package es.kix2902.santoral.data.database

import androidx.room.Dao
import androidx.room.Query
import es.kix2902.santoral.data.Model

@Dao
interface InfoDao : BaseDao<Model.QueryInfo> {
    @Query("SELECT * FROM queryinfo WHERE month=:month AND day=:day AND language=:language")
    fun getQueryInfoForDate(month: Int, day: Int, language: String): List<Model.QueryInfo>
}