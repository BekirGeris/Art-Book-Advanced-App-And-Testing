package com.begers.artbooktesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.begers.artbooktesting.model.Art

@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun deleteArt(art: Art)

    @Query("SELECT * FROM arts")
    fun observeArts() : LiveData<List<Art>>

}