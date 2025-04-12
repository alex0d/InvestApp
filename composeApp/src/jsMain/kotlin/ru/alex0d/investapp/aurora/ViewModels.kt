package ru.alex0d.investapp.aurora

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alex0d.investapp.screens.auth.AuthViewModel
import ru.alex0d.investapp.utils.AuroraExport

@Suppress("NON_EXPORTABLE_TYPE")
@OptIn(ExperimentalJsExport::class)
@AuroraExport
class ViewModels : KoinComponent {
    val authViewModel = inject<AuthViewModel>().value
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val viewModels = ViewModels()