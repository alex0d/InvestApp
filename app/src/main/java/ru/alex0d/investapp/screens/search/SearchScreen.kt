package ru.alex0d.investapp.screens.search

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.utils.MainGraph

@Destination<MainGraph>
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {

}