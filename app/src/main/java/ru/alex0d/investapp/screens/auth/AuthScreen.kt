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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AuthScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.AuthResult

private sealed class AuthScreenState {
    object Login : AuthScreenState()
    object Register : AuthScreenState()
}

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
            text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier.size(170.dp),
            painter = painterResource(id = if (!isSystemInDarkTheme()) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground_whiteborders),
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
        label = { Text(stringResource(R.string.lastname_optional)) },
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
    var isPasswordVisible by remember { mutableStateOf(false) }

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
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (password.isNotEmpty()) {
                val icon =
                    if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                val description =
                    if (isPasswordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = description
                    )
                }
            }
        },
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
        enabled = isLoginButtonEnabled && !isLoading
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
        enabled = isRegisterButtonEnabled && !isLoading
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

@Preview(showBackground = true)
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