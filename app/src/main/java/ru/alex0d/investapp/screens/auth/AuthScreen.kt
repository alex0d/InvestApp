package ru.alex0d.investapp.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AuthScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.AuthResult
import ru.alex0d.investapp.utils.isDarkThemeOn

@Destination<RootGraph>(start = true)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val authState = viewModel.authState.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    dismissActionContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        },
    ) { innerPadding ->
        if (authState is AuthState.Success) {
            navigator.navigate(MainScreenDestination) {
                popUpTo(AuthScreenDestination) {
                    inclusive = true
                }
            }
            return@Scaffold
        }

        val context = LocalContext.current
        val localFocusManager = LocalFocusManager.current

        var screenState by remember { mutableStateOf<AuthScreenState>(AuthScreenState.Login) }

        val authErrorState = viewModel.authErrorState.collectAsState().value
        SnackbarOnError(
            authErrorState = authErrorState,
            snackbarHostState = snackbarHostState,
            clearError = { viewModel.clearError() }
        )

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier.size(170.dp),
                    painter = painterResource(id = if (!context.isDarkThemeOn()) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground_whiteborders),
                    contentDescription = null
                )

                if (screenState is AuthScreenState.Register) {
                    NameTextFields(
                        firstname = viewModel.firstname,
                        onFirstnameUpdate = { viewModel.updateFirstname(it) },
                        isFirstnameError = !viewModel.isValidFirstname && viewModel.firstname.isNotEmpty(),
                        lastname = viewModel.lastname,
                        onLastnameUpdate = { viewModel.updateLastname(it) },
                        isLastnameError = !viewModel.isValidLastname && viewModel.lastname.isNotEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                EmailAndPasswordTextFields(
                    email = viewModel.email,
                    onEmailUpdate = { viewModel.updateEmail(it) },
                    isEmailError = !viewModel.isValidEmail && viewModel.email.isNotEmpty(),
                    password = viewModel.password,
                    onPasswordUpdate = { viewModel.updatePassword(it) },
                    isPasswordError = !viewModel.isValidPassword && viewModel.password.isNotEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                when (screenState) {
                    is AuthScreenState.Login -> LoginButtons(
                        onAuthenticate = { viewModel.authenticate() },
                        onNavigateToRegister = { screenState = AuthScreenState.Register },
                        isLoginButtonEnabled = viewModel.isValidEmail && viewModel.isValidPassword,
                        isLoading = authState is AuthState.Loading
                    )

                    is AuthScreenState.Register -> RegisterButtons(
                        onRegister = { viewModel.register() },
                        onNavigateToLogin = { screenState = AuthScreenState.Login },
                        isRegisterButtonEnabled = viewModel.isValidFirstname && viewModel.isValidLastname && viewModel.isValidEmail && viewModel.isValidPassword,
                        isLoading = authState is AuthState.Loading
                    )
                }
            }
        }
    }
}

@Composable
private fun NameTextFields(
    firstname: String,
    onFirstnameUpdate: (String) -> Unit,
    isFirstnameError: Boolean,
    lastname: String,
    onLastnameUpdate: (String) -> Unit,
    isLastnameError: Boolean
) {
    OutlinedTextField(
        value = firstname,
        onValueChange = { onFirstnameUpdate(it) },
        label = { Text(stringResource(R.string.firstname)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        isError = isFirstnameError,
        supportingText = { if (isFirstnameError) Text(stringResource(R.string.invalid_firstname)) }
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = lastname,
        onValueChange = { onLastnameUpdate(it) },
        label = { Text(stringResource(R.string.lastname)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        isError = isLastnameError,
        supportingText = { if (isLastnameError) Text(stringResource(R.string.invalid_lastname)) }
    )
}

@Composable
private fun EmailAndPasswordTextFields(
    email: String,
    onEmailUpdate: (String) -> Unit,
    isEmailError: Boolean,
    password: String,
    onPasswordUpdate: (String) -> Unit,
    isPasswordError: Boolean
) {
    OutlinedTextField(
        value = email,
        onValueChange = { newValue -> onEmailUpdate(newValue.trim()) },
        label = { Text(stringResource(R.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        isError = isEmailError,
        supportingText = { if (isEmailError) Text(stringResource(R.string.invalid_email)) }
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = password,
        onValueChange = { newValue -> onPasswordUpdate(newValue.trim()) },
        label = { Text(stringResource(R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        isError = isPasswordError,
        supportingText = { if (isPasswordError) Text(stringResource(R.string.invalid_password)) }
    )
}

@Composable
private fun LoginButtons(
    onAuthenticate: () -> Unit,
    onNavigateToRegister: () -> Unit,
    isLoginButtonEnabled: Boolean,
    isLoading: Boolean
) {
    Button(
        onClick = onAuthenticate,
        enabled = isLoginButtonEnabled
    ) {
        if (!isLoading) {
            Text(stringResource(R.string.log_in))
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    TextButton(
        onClick = onNavigateToRegister
    ) {
        Text(stringResource(R.string.register))
    }
}

@Composable
private fun RegisterButtons(
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    isRegisterButtonEnabled: Boolean,
    isLoading: Boolean
) {
    Button(
        onClick = onRegister,
        enabled = isRegisterButtonEnabled
    ) {
        if (!isLoading) {
            Text(stringResource(R.string.register))
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    TextButton(
        onClick = onNavigateToLogin
    ) {
        Text(stringResource(R.string.log_in))
    }
}

@Composable
private fun SnackbarOnError(
    authErrorState: AuthErrorState,
    snackbarHostState: SnackbarHostState,
    clearError: () -> Unit
) {
    val emailAlreadyRegisteredMessage = stringResource(R.string.email_already_registered)
    val invalidCredentialsMessage = stringResource(R.string.invalid_credentials)
    val userNotFoundMessage = stringResource(R.string.user_not_found)
    val unknownErrorMessage = stringResource(R.string.unknown_error)
    LaunchedEffect(authErrorState) {
        if (authErrorState is AuthErrorState.Error) {
            when (authErrorState.result) {
                AuthResult.EMAIL_ALREADY_REGISTERED -> snackbarHostState.showSnackbar(emailAlreadyRegisteredMessage, withDismissAction = true)
                AuthResult.INVALID_CREDENTIALS -> snackbarHostState.showSnackbar(invalidCredentialsMessage, withDismissAction = true)
                AuthResult.USER_NOT_FOUND -> snackbarHostState.showSnackbar(userNotFoundMessage, withDismissAction = true)
                else -> snackbarHostState.showSnackbar(unknownErrorMessage, withDismissAction = true)
            }
        }
        clearError()
    }
}

sealed class AuthScreenState {
    object Login : AuthScreenState()
    object Register : AuthScreenState()
}