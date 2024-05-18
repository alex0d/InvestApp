package ru.alex0d.investapp.screens.search

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.utils.MainDispatcherRule

private const val TICKER = "AAPL"

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var stockRepository: StockRepository
    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        stockRepository = mock()
        searchViewModel = SearchViewModel(stockRepository)
    }

    @Test
    fun `should start listening to inputText changes when initialized`() = runTest {
        searchViewModel.initialize()

        searchViewModel.updateInput(TICKER)

        assertEquals(TICKER, searchViewModel.inputText.first())
    }

    @Test
    fun `should update inputText and viewState when updateInput is called`() = runTest {
        val shares = listOf(mock<Share>())
        whenever(stockRepository.getSharesByTicker(TICKER)).thenReturn(shares)

        searchViewModel.updateInput(TICKER)

        assertEquals(TICKER, searchViewModel.inputText.first())
        assertEquals(SearchViewModel.ViewState.Loading, searchViewModel.viewState.first())
    }

    @Test
    fun `should update searchFieldState when searchFieldActivated is called`() = runTest {
        searchViewModel.searchFieldActivated()

        assertEquals(SearchViewModel.SearchFieldState.EmptyActive, searchViewModel.searchFieldState.first())
    }

    @Test
    fun `should clear inputText and update viewState and searchFieldState when clearInput is called`() = runTest {
        searchViewModel.clearInput()

        assertEquals("", searchViewModel.inputText.first())
        assertEquals(SearchViewModel.ViewState.Loading, searchViewModel.viewState.first())
        assertEquals(SearchViewModel.SearchFieldState.EmptyActive, searchViewModel.searchFieldState.first())
    }

    @Test
    fun `should revert all states to initial values when revertToInitialState is called`() = runTest {
        searchViewModel.revertToInitialState()

        assertEquals("", searchViewModel.inputText.first())
        assertEquals(SearchViewModel.ViewState.IdleScreen, searchViewModel.viewState.first())
        assertEquals(SearchViewModel.SearchFieldState.Idle, searchViewModel.searchFieldState.first())
    }

    @Test
    fun `should update searchFieldState based on inputText when keyboardHidden is called`() = runTest {
        searchViewModel.updateInput(TICKER)
        searchViewModel.keyboardHidden()

        assertEquals(SearchViewModel.SearchFieldState.WithInputActive, searchViewModel.searchFieldState.first())
    }
}