package ru.alex0d.investapp.ui.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.generated.destinations.PortfolioScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import ru.alex0d.investapp.R

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val navigator = navController.rememberDestinationsNavigator()
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination

        BottomBarItem.entries.forEach { destination ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)
            val selected = currentRoute?.route == destination.direction.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        navigator.popBackStack(destination.direction, false)
                        return@NavigationBarItem
                    }

                    navigator.navigate(destination.direction) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = stringResource(destination.label),
                        tint = if (selected) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

enum class BottomBarItem(
    val direction: DirectionDestinationSpec, @DrawableRes val icon: Int, @StringRes val label: Int
) {
    Portfolio(PortfolioScreenDestination, R.drawable.portfolio, R.string.portfolio), Profile(
        ProfileScreenDestination,
        R.drawable.account_circle,
        R.string.profile
    )
}