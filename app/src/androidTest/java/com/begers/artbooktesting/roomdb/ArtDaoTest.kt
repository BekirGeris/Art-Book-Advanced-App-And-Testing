package com.begers.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.begers.artbooktesting.getOrAwaitValue
import com.begers.artbooktesting.model.Art
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class ArtDaoTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ArtDatabase
    private lateinit var dao: ArtDao

    @Before
    fun setup() {

        //geçiçi database oluşturulur.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArtDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.artDao()
    }


    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertArtTesting() = runBlockingTest {
        val exampleArt = Art("Mona Lisa", "Da Vinci", 1999, "test.com", 1)
        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runBlockingTest {
        val exampleArt = Art("Mona Lisa", "Da Vinci", 1999, "test.com", 1)
        dao.insertArt(exampleArt)
        dao.deleteArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleArt)
    }
}