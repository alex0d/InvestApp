@file:OptIn(ExperimentalAdaptiveApi::class)

package ru.alex0d.investapp.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.account_circle
import investapp.composeapp.generated.resources.error_occurred
import investapp.composeapp.generated.resources.logout
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.alex0d.investapp.screens.auth.AuthScreen
import ru.alex0d.investapp.screens.main.MainTabs
import ru.alex0d.investapp.screens.main.voyagerTabOptions
import ru.alex0d.investapp.ui.composables.TabX

class ProfileScreen : TabX {
    override val key = uniqueScreenKey
    override val options: TabOptions
        @Composable
        get() = MainTabs.PROFILE.voyagerTabOptions

    @Composable
    override fun Content(
        innerPadding: PaddingValues,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<ProfileViewModel>(key = this.key)
        val state = viewModel.state.collectAsState().value

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is ProfileState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.error_occurred),
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                is ProfileState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AdaptiveCircularProgressIndicator()
                    }
                }

                is ProfileState.Success -> {
                    Icon(
                        modifier = Modifier
                            .size(240.dp)
                            .padding(top = 32.dp, bottom = 16.dp),
                        painter = painterResource(Res.drawable.account_circle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = state.email,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${state.firstname} ${state.lastname ?: ""}".trimEnd(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AdaptiveButton(
                        modifier = Modifier.padding(bottom = 32.dp),
                        onClick = {
                            viewModel.logout()
                            navigator.parent?.replaceAll(AuthScreen())
                        },
                        adaptation = {
                            material {
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        },
                    ) {
                        Text(
                            text = stringResource(Res.string.logout),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}