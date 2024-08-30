package ru.alex0d.investapp.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import ru.alex0d.investapp.utils.connectivity.ConnectivityObserver

class RootViewModel(
    private val networkConnectivityObserver: ConnectivityObserver
) : ViewModel() {

    @OptIn(FlowPreview::class)
    val networkStatus = networkConnectivityObserver
        .observe()
        .debounce(500)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ConnectivityObserver.Status.AVAILABLE)

}