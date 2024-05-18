package ru.alex0d.investapp.screens.tarot

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.alex0d.investapp.data.repositories.TarotRepository
import ru.alex0d.investapp.domain.models.TarotPrediction
import ru.alex0d.investapp.utils.MainDispatcherRule

class TarotViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var tarotRepository: TarotRepository
    private lateinit var tarotViewModel: TarotViewModel

    @Before
    fun setup() {
        tarotRepository = mock()
        tarotViewModel = TarotViewModel(tarotRepository, "stockName")
    }

    @Test
    fun `refreshTarotPrediction should update state on success`() = runTest {
        val prediction = mock<TarotPrediction> { }
        whenever(tarotRepository.refreshPrediction("stockName")).thenReturn(prediction)

        tarotViewModel.refreshTarotPrediction()

        assertEquals(TarotPredictionState.Success(prediction), tarotViewModel.state.value)
    }

    @Test
    fun `refreshTarotPrediction should update state on error`() = runTest {
        whenever(tarotRepository.refreshPrediction("stockName")).thenReturn(null)

        tarotViewModel.refreshTarotPrediction()

        assertEquals("An error occurred", (tarotViewModel.state.value as TarotPredictionState.Error).message)
    }
}