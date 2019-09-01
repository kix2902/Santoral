package es.kix2902.santoral.data.database

import androidx.room.Dao
import androidx.room.Query
import es.kix2902.santoral.data.Model

@Dao
interface SaintsDao : BaseDao<Model.Saint> {
    @Query("SELECT * FROM saints WHERE date=:date ORDER BY important DESC, name ASC")
    fun getAllSaintsForDate(date: String): List<Model.Saint>
}