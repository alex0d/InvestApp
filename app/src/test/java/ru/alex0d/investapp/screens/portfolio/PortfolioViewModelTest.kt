package ru.alex0d.investapp.screens.portfolio

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.utils.MainDispatcherRule

private const val UPDATE_INTERVAL = 30000L

class PortfolioViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var portfolioRepository: PortfolioRepository
    private lateinit var portfolioViewModel: PortfolioViewModel

    @Before
    fun setup() {
        portfolioRepository = mock()
        portfolioViewModel = PortfolioViewModel(portfolioRepository)
    }

    @After
    fun tearDown() {
        portfolioViewModel.stopUpdating()
    }

    @Test
    fun `should update state when startUpdating is called`() = runTest {
        val portfolioInfo = mock<PortfolioInfo>()
        whenever(portfolioRepository.getPortfolio()).thenReturn(portfolioInfo)

        portfolioViewModel.startUpdating()

        delay(UPDATE_INTERVAL)

        assertEquals(PortfolioState.PortfolioInfoFetched(portfolioInfo), portfolioViewModel.state.value)
        portfolioViewModel.stopUpdating()
    }

    @Test
    fun `should stop updating state when stopUpdating is called`() = runTest {
        val portfolioInfo = mock<PortfolioInfo>()
        whenever(portfolioRepository.getPortfolio()).thenReturn(portfolioInfo)

        portfolioViewModel.startUpdating()

        delay(UPDATE_INTERVAL / 2)

        portfolioViewModel.stopUpdating()

        delay(UPDATE_INTERVAL * 2)

        verify(portfolioRepository, only()).getPortfolio()
    }
}