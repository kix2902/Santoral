package es.kix2902.santoral.data.database

import androidx.room.Dao
import androidx.room.Query
import es.kix2902.santoral.data.Model

@Dao
interface InfoDao : BaseDao<Model.QueryInfo> {
    @Query("SELECT * FROM queryinfo WHERE month=:month AND day=:day")
    fun getQueryInfoForDate(month: Int, day: Int): List<Model.QueryInfo>
}