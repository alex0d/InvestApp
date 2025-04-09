package ru.alex0d.investapp.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.email
import investapp.composeapp.generated.resources.email_already_registered
import investapp.composeapp.generated.resources.firstname
import investapp.composeapp.generated.resources.hide_password
import investapp.composeapp.generated.resources.ic_launcher_foreground
import investapp.composeapp.generated.resources.ic_launcher_foreground_whiteborders
import investapp.composeapp.generated.resources.ic_visibility
import investapp.composeapp.generated.resources.ic_visibility_off
import investapp.composeapp.generated.resources.invalid_credentials
import investapp.composeapp.generated.resources.invalid_email
import investapp.composeapp.generated.resources.invalid_firstname
import investapp.composeapp.generated.resources.invalid_lastname
import investapp.composeapp.generated.resources.invalid_password
import investapp.composeapp.generated.resources.lastname_optional
import investapp.composeapp.generated.resources.log_in
import investapp.composeapp.generated.resources.password
import investapp.composeapp.generated.resources.register
import investapp.composeapp.generated.resources.show_password
import investapp.composeapp.generated.resources.unknown_error
import investapp.composeapp.generated.resources.user_not_found
import investapp.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.alex0d.investapp.domain.models.AuthResult
import ru.alex0d.investapp.screens.main.MainScreen

private sealed class AuthScreenState {
    object Login : AuthScreenState()
    object Register : AuthScreenState()
}

class AuthScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<AuthViewModel>(key = this.key)

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
                navigator.replaceAll(MainScreen())
                return@Scaffold
            }

            val authErrorState = viewModel.authErrorState.collectAsState().value
            SnackbarOnError(
                authErrorState = authErrorState,
                snackbarHostState = snackbarHostState,
                clearError = { viewModel.clearError() }
            )

            AuthScreenContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                authState = authState,
                firstname = viewModel.firstname,
                onFirstnameUpdate = { viewModel.updateFirstname(it) },
                isValidFirstname = viewModel.isValidFirstname,
                lastname = viewModel.lastname,
                onLastnameUpdate = { viewModel.updateLastname(it) },
                isValidLastname = viewModel.isValidLastname,
                email = viewModel.email,
                onEmailUpdate = { viewModel.updateEmail(it) },
                isValidEmail = viewModel.isValidEmail,
                password = viewModel.password,
                onPasswordUpdate = { viewModel.updatePassword(it) },
                isValidPassword = viewModel.isValidPassword,
                authenticate = { viewModel.authenticate() },
                register = { viewModel.register() }
            )
        }
    }

    @Composable
    internal fun AuthScreenContent(
        modifier: Modifier,
        authState: AuthState,
        firstname: String,
        onFirstnameUpdate: (String) -> Unit,
        isValidFirstname: Boolean,
        lastname: String,
        onLastnameUpdate: (String) -> Unit,
        isValidLastname: Boolean,
        email: String,
        onEmailUpdate: (String) -> Unit,
        isValidEmail: Boolean,
        password: String,
        onPasswordUpdate: (String) -> Unit,
        isValidPassword: Boolean,
        authenticate: () -> Unit,
        register: () -> Unit
    ) {
        var screenState by remember { mutableStateOf<AuthScreenState>(AuthScreenState.Login) }
        val localFocusManager = LocalFocusManager.current

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.welcome),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.size(170.dp),
                painter = painterResource(if (!isSystemInDarkTheme()) Res.drawable.ic_launcher_foreground else Res.drawable.ic_launcher_foreground_whiteborders),
                contentDescription = null
            )

            if (screenState is AuthScreenState.Register) {
                NameTextFields(
                    firstname = firstname,
                    onFirstnameUpdate = { onFirstnameUpdate(it) },
                    isFirstnameError = !isValidFirstname && firstname.isNotEmpty(),
                    lastname = lastname,
                    onLastnameUpdate = { onLastnameUpdate(it) },
                    isLastnameError = !isValidLastname && lastname.isNotEmpty()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            EmailAndPasswordTextFields(
                email = email,
                onEmailUpdate = { onEmailUpdate(it) },
                isEmailError = !isValidEmail && email.isNotEmpty(),
                password = password,
                onPasswordUpdate = { onPasswordUpdate(it) },
                isPasswordError = !isValidPassword && password.isNotEmpty()
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (screenState) {
                is AuthScreenState.Login -> LoginButtons(
                    onAuthenticate = { authenticate() },
                    onNavigateToRegister = { screenState = AuthScreenState.Register },
                    isLoginButtonEnabled = isValidEmail && isValidPassword,
                    isLoading = authState is AuthState.Loading
                )

                is AuthScreenState.Register -> RegisterButtons(
                    onRegister = { register() },
                    onNavigateToLogin = { screenState = AuthScreenState.Login },
                    isRegisterButtonEnabled = isValidFirstname && isValidLastname && isValidEmail && isValidPassword,
                    isLoading = authState is AuthState.Loading
                )
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
            label = { Text(stringResource(Res.string.firstname)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = isFirstnameError,
            supportingText = { if (isFirstnameError) Text(stringResource(Res.string.invalid_firstname)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastname,
            onValueChange = { onLastnameUpdate(it) },
            label = { Text(stringResource(Res.string.lastname_optional)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = isLastnameError,
            supportingText = { if (isLastnameError) Text(stringResource(Res.string.invalid_lastname)) }
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
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = email,
            onValueChange = { newValue -> onEmailUpdate(newValue.trim()) },
            label = { Text(stringResource(Res.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            isError = isEmailError,
            supportingText = { if (isEmailError) Text(stringResource(Res.string.invalid_email)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { newValue -> onPasswordUpdate(newValue.trim()) },
            label = { Text(stringResource(Res.string.password)) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                if (password.isNotEmpty()) {
                    val icon =
                        if (isPasswordVisible) Res.drawable.ic_visibility_off else Res.drawable.ic_visibility
                    val description =
                        if (isPasswordVisible) stringResource(Res.string.hide_password) else stringResource(
                            Res.string.show_password
                        )
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = description
                        )
                    }
                }
            },
            singleLine = true,
            isError = isPasswordError,
            supportingText = { if (isPasswordError) Text(stringResource(Res.string.invalid_password)) }
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
            enabled = isLoginButtonEnabled && !isLoading
        ) {
            if (!isLoading) {
                Text(stringResource(Res.string.log_in))
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
            Text(stringResource(Res.string.register))
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
            enabled = isRegisterButtonEnabled && !isLoading
        ) {
            if (!isLoading) {
                Text(stringResource(Res.string.register))
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
            Text(stringResource(Res.string.log_in))
        }
    }

    @Composable
    private fun SnackbarOnError(
        authErrorState: AuthErrorState,
        snackbarHostState: SnackbarHostState,
        clearError: () -> Unit
    ) {
        val emailAlreadyRegisteredMessage = stringResource(Res.string.email_already_registered)
        val invalidCredentialsMessage = stringResource(Res.string.invalid_credentials)
        val userNotFoundMessage = stringResource(Res.string.user_not_found)
        val unknownErrorMessage = stringResource(Res.string.unknown_error)
        LaunchedEffect(authErrorState) {
            if (authErrorState is AuthErrorState.Error) {
                when (authErrorState.result) {
                    AuthResult.EMAIL_ALREADY_REGISTERED -> snackbarHostState.showSnackbar(
                        emailAlreadyRegisteredMessage,
                        withDismissAction = true
                    )

                    AuthResult.INVALID_CREDENTIALS -> snackbarHostState.showSnackbar(
                        invalidCredentialsMessage,
                        withDismissAction = true
                    )

                    AuthResult.USER_NOT_FOUND -> snackbarHostState.showSnackbar(
                        userNotFoundMessage,
                        withDismissAction = true
                    )

                    else -> snackbarHostState.showSnackbar(
                        unknownErrorMessage,
                        withDismissAction = true
                    )
                }
                clearError()
            }
        }
    }

    @Preview
    @Composable
    private fun AuthScreenContentPreview() {
        AuthScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            authState = AuthState.Idle,
            firstname = "John",
            onFirstnameUpdate = {},
            isValidFirstname = true,
            lastname = "Doe",
            onLastnameUpdate = {},
            isValidLastname = true,
            email = "john@doe.com",
            onEmailUpdate = {},
            isValidEmail = true,
            password = "test1234",
            onPasswordUpdate = {},
            isValidPassword = true,
            authenticate = {},
            register = {}
        )
    }
}
