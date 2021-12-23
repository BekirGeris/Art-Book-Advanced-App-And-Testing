package com.begers.artbooktesting.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.begers.artbooktesting.model.Art

@Database(entities = [Art::class], version = 1)
abstract class ArtDatabase : RoomDatabase(){

    abstract fun artDao() : ArtDao
}