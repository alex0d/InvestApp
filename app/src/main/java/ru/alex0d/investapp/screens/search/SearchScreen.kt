package ru.alex0d.investapp.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.StockDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.ui.composables.Keyboard
import ru.alex0d.investapp.ui.composables.SearchInputField
import ru.alex0d.investapp.ui.composables.keyboardAsState
import ru.alex0d.investapp.utils.MainGraph
import ru.alex0d.investapp.utils.toCurrencyFormat

@Destination<MainGraph>
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = koinViewModel()
) {
    val localFocusManager = LocalFocusManager.current

    val viewState = viewModel.viewState.collectAsState().value
    val searchFieldState = viewModel.searchFieldState.collectAsState().value
    val inputText = viewModel.inputText.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardState by keyboardAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    SearchScreenLayout(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            },
        viewState = viewState,
        searchFieldState = searchFieldState,
        inputText = inputText,
        onSearchInputChanged = { input -> viewModel.updateInput(input) },
        onSearchInputClicked = { viewModel.searchFieldActivated() },
        onClearInputClicked = { viewModel.clearInput() },
        onChevronClicked = {
            viewModel.revertToInitialState()
            keyboardController?.hide()
        },
        keyboardState = keyboardState,
        onKeyboardHidden = { viewModel.keyboardHidden() },
        onItemClicked = { share ->
            navigator.navigate(StockDetailsScreenDestination(stockUid = share.uid))
        }
    )
}

@Composable
private fun SearchScreenLayout(
    modifier: Modifier,
    viewState: SearchViewModel.ViewState,
    searchFieldState: SearchViewModel.SearchFieldState,
    inputText: String,
    onSearchInputChanged: (String) -> Unit,
    onSearchInputClicked: () -> Unit,
    onClearInputClicked: () -> Unit,
    onChevronClicked: () -> Unit,
    onKeyboardHidden: () -> Unit,
    keyboardState: Keyboard,
    onItemClicked: (Share) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchHeader(searchFieldState = searchFieldState)
        SearchInputField(
            searchFieldState = searchFieldState,
            inputText = inputText,
            onClearInputClicked = onClearInputClicked,
            onSearchInputChanged = onSearchInputChanged,
            onClicked = onSearchInputClicked,
            onChevronClicked = onChevronClicked,
            onKeyboardHidden = onKeyboardHidden,
            keyboardState = keyboardState,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )
        when (viewState) {
            SearchViewModel.ViewState.IdleScreen -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.start_investing),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            SearchViewModel.ViewState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.error_occurred),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            SearchViewModel.ViewState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            SearchViewModel.ViewState.NoResults -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_results),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            is SearchViewModel.ViewState.SearchResultsFetched -> {
                SearchResultsList(items = viewState.results, onItemClicked = onItemClicked)
            }
        }
    }
}

@Composable
private fun SearchHeader(
    searchFieldState: SearchViewModel.SearchFieldState
) {
    AnimatedVisibility(visible = searchFieldState == SearchViewModel.SearchFieldState.Idle) {
        Text(
            text = stringResource(R.string.stock_search),
            modifier = Modifier.padding(start = 24.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
            style = TextStyle(
                fontWeight = FontWeight(700),
                fontSize = 32.sp,
            )
        )
    }
}

@Composable
private fun SearchResultsList(items: List<Share>, onItemClicked: (Share) -> Unit) {
    LazyColumn {
        itemsIndexed(items = items) { index, share ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier.height(height = if (index == 0) 16.dp else 4.dp)
                )
                ShareItem(share = share, onClick = onItemClicked)
            }
        }
    }
}

@Composable
private fun ShareItem(
    share: Share,
    onClick: (Share) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(share) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                model = "https://invest-brands.cdn-tinkoff.ru/${share.url}x160.png",
                contentDescription = "${share.name} logo"
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = share.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = share.lastPrice.toCurrencyFormat("RUB"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}