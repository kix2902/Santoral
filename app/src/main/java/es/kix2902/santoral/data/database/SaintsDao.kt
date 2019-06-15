package es.kix2902.santoral.data.database

import androidx.room.*
import es.kix2902.santoral.data.Model

@Dao
interface SaintsDao : BaseDao<Model.Saint> {
    @Query("SELECT * FROM saints WHERE feast=:feast")
    fun getAllSaintsForDate(feast: String): List<Model.Saint>
}