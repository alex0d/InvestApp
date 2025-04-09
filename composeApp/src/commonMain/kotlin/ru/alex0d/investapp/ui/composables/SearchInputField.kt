package ru.alex0d.investapp.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.ic_chevron_left
import investapp.composeapp.generated.resources.ic_close_search
import investapp.composeapp.generated.resources.navbar_icons_search_inactive
import investapp.composeapp.generated.resources.search_stock_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alex0d.investapp.screens.search.SearchViewModel

@Composable
fun SearchInputField(
    searchFieldState: SearchViewModel.SearchFieldState,
    inputText: String,
    onSearchInputChanged: (String) -> Unit,
    onClearInputClicked: () -> Unit,
    onClicked: () -> Unit,
    onChevronClicked: () -> Unit,
    onKeyboardHidden: () -> Unit,
    keyboardState: Keyboard
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val isKeyboardOpen = keyboardState == Keyboard.OPENED

    if (searchFieldState is SearchViewModel.SearchFieldState.WithInputActive && isKeyboardOpen) {
        onKeyboardHidden()
    }

    TextField(
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onKeyboardHidden()
        }, onGo = {
            onKeyboardHidden()
        }),
        value = inputText,
        onValueChange = { newInput -> onSearchInputChanged(newInput) },
        leadingIcon = {
            when (searchFieldState) {
                SearchViewModel.SearchFieldState.Idle -> Icon(
                    painter = painterResource(Res.drawable.navbar_icons_search_inactive),
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                SearchViewModel.SearchFieldState.EmptyActive,
                SearchViewModel.SearchFieldState.WithInputActive -> Icon(
                    painter = painterResource(Res.drawable.ic_chevron_left),
                    contentDescription = "Search chevron icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { onChevronClicked.invoke() }
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        trailingIcon = if (searchFieldState is SearchViewModel.SearchFieldState.WithInputActive) {
            {
                Icon(
                    painter = painterResource(Res.drawable.ic_close_search),
                    contentDescription = "Search close icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { onClearInputClicked.invoke() }
                )
            }
        } else {
            null
        },
        placeholder = {
            Text(
                text = stringResource(Res.string.search_stock_placeholder),
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontWeight = FontWeight(500),
                    fontSize = 14.sp,
                    lineHeight = 19.6.sp,
                ),
                textAlign = TextAlign.Start,
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .focusRequester(focusRequester)
            .focusable(true),
        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
            LaunchedEffect(key1 = interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        onClicked.invoke()
                    }
                }
            }
        },
    )
}
