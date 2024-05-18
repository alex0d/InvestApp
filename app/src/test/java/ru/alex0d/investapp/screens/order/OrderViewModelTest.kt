package ru.alex0d.investapp.screens.order

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.utils.MainDispatcherRule
import ru.alex0d.investapp.utils.toCurrencyFormat

private const val STOCK_UID = "stockUid"

class OrderViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var stockRepository: StockRepository
    private lateinit var portfolioRepository: PortfolioRepository
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var share: Share

    @Before
    fun setup() = runTest {
        stockRepository = mock()
        portfolioRepository = mock()

        share = mock {
            on { uid } doReturn STOCK_UID
            on { lot } doReturn 10
            on { lastPrice } doReturn 123.50
        }
        whenever(stockRepository.getShareByUid(STOCK_UID)).thenReturn(share)

        orderViewModel = OrderViewModel(stockRepository, portfolioRepository, OrderAction.BUY, STOCK_UID)
    }

    @Test
    fun `should increase lots`() {
        val initialLots = orderViewModel.inputLots.value
        orderViewModel.increaseLots()
        assertEquals(initialLots + 1, orderViewModel.inputLots.value)
    }

    @Test
    fun `should decrease lots`() {
        orderViewModel.increaseLots() // Increase once to ensure we have more than 0 lots
        val initialLots = orderViewModel.inputLots.value

        orderViewModel.decreaseLots()
        assertEquals(initialLots - 1, orderViewModel.inputLots.value)
    }

    @Test
    fun `should not decrease lots below 0`() {
        val initialLots = orderViewModel.inputLots.value

        orderViewModel.decreaseLots()
        assertEquals(initialLots, orderViewModel.inputLots.value)
    }

    @Test
    fun `should update input lots`() {
        val lots = 10

        orderViewModel.updateInputLots(lots.toString())
        assertEquals(lots, orderViewModel.inputLots.value)
    }

    @Test
    fun `should not update input lots if invalid`() {
        val lots = -1

        orderViewModel.updateInputLots(lots.toString())
        assertEquals(0, orderViewModel.inputLots.value)
    }

    @Test
    fun `should set total value`() {
        val lots = 10
        val expectedTotalValue = lots * share.lot * share.lastPrice

        orderViewModel.updateInputLots(lots.toString())
        assertEquals(expectedTotalValue.toCurrencyFormat("RUB"), orderViewModel.totalValue.value)
    }

    @Test
    fun `should successfully confirm buy order`() = runTest {
        whenever(portfolioRepository.buyStock(STOCK_UID, 20)).thenReturn(true)

        orderViewModel.updateInputLots("2")

        val result = orderViewModel.confirmOrder()
        assertEquals(true, result)
        verify(portfolioRepository).buyStock(STOCK_UID, 20)
    }

    @Test
    fun `should successfully confirm sell order`() = runTest {
        val portfolioStock = mock<PortfolioStockInfo> {
            on { uid } doReturn STOCK_UID
            on { amount } doReturn 20
        }
        val portfolio = mock<PortfolioInfo> {
            on { stocks } doReturn listOf(portfolioStock)
        }
        whenever(portfolioRepository.getPortfolio()).thenReturn(portfolio)
        whenever(portfolioRepository.sellStock(STOCK_UID, 20)).thenReturn(true)

        orderViewModel = OrderViewModel(stockRepository, portfolioRepository, OrderAction.SELL, STOCK_UID)
        orderViewModel.updateInputLots("2")

        val result = orderViewModel.confirmOrder()
        assertEquals(true, result)
        verify(portfolioRepository).sellStock(STOCK_UID, 20)
    }
}