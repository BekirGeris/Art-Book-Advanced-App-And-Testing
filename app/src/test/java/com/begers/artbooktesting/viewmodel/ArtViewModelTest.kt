package com.begers.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.begers.artbooktesting.MainCoroutineRule
import com.begers.artbooktesting.getOrAwaitValueTest
import com.begers.artbooktesting.repo.FakeArtRepostory
import com.begers.artbooktesting.util.Status
import com.google.common.truth.ExpectFailure.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup() {
        //Test Doubles -> kopyasını test ederiz demek
        viewModel = ArtViewModel(FakeArtRepostory())
    }

    @Test
    fun `insert art without year returns error`() {
        viewModel.makeArt("Mona Lisa", "Da Vinci", "")
        val value = viewModel.insertArtMsg.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error`() {
        viewModel.makeArt("", "Da Vinci", "1999")
        val value = viewModel.insertArtMsg.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artist name returns error`() {
        viewModel.makeArt("Mona Lisa", "", "1999")
        val value = viewModel.insertArtMsg.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}