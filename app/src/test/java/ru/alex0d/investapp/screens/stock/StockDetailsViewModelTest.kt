package ru.alex0d.investapp.screens.stock

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.utils.MainDispatcherRule

private const val STOCK_UID = "stockUid"

class StockDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var stockRepository: StockRepository
    private lateinit var marketRepository: MarketRepository
    private lateinit var portfolioRepository: PortfolioRepository
    private lateinit var viewModel: StockDetailsViewModel

    @Before
    fun setup() {
        stockRepository = mock()
        marketRepository = mock()
        portfolioRepository = mock()
        viewModel = StockDetailsViewModel(stockRepository, marketRepository, portfolioRepository, STOCK_UID)

        for (field in viewModel::class.java.fields) {
            field.isAccessible = true
        }
    }

    @Test
    fun `fetchStockDetails should update state on success`() = runTest {
        val share = mock<Share> {
            on { uid } doReturn STOCK_UID
        }
        val stockInfo = mock<PortfolioStockInfo>() {
            on { uid } doReturn STOCK_UID
        }
        val portfolio = mock<PortfolioInfo> {
            on { stocks } doReturn listOf(stockInfo)
        }
        whenever(stockRepository.getShareByUid(STOCK_UID)).thenReturn(share)
        whenever(portfolioRepository.getPortfolio()).thenReturn(portfolio)

        viewModel.fetchStockDetails()

        assertEquals(StockDetailsState.Success(share, stockInfo), viewModel.state.value)
    }

    @Test
    fun `fetchStockDetails should update state on error`() = runTest {
        whenever(stockRepository.getShareByUid(STOCK_UID)).thenThrow(RuntimeException("An error occurred"))

        viewModel.fetchStockDetails()

        assertEquals("An error occurred", (viewModel.state.value as StockDetailsState.Error).message)
    }
}