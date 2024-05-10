package ru.alex0d.investapp.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest
import ru.alex0d.investapp.utils.isDarkThemeOn

@Destination<RootGraph>(start = true)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator
) {
    val localFocusManager = LocalFocusManager.current

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                },
        ) {
            AuthScreenContent(
                onSuccess = {
                    navigator.navigate(MainScreenDestination) {
                        popUpTo(AuthScreenDestination) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun AuthScreenContent(
    viewModel: AuthViewModel = koinViewModel(),
    onSuccess: () -> Unit
) {
    val context = LocalContext.current

    var screenState by remember { mutableStateOf<AuthScreenState>(AuthScreenState.Login) }
    val authState by viewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Idle -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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
                    painter = painterResource(id = if (!context.isDarkThemeOn()) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground_whiteborders),
                    contentDescription = null
                )

                when (screenState) {
                    AuthScreenState.Login -> LoginForm(
                        onAuthenticate = { request -> viewModel.authenticate(request) },
                        onNavigateToRegister = { screenState = AuthScreenState.Register }
                    )
                    AuthScreenState.Register -> RegisterForm(
                        onRegister = { request -> viewModel.register(request) },
                        onNavigateToLogin = { screenState = AuthScreenState.Login }
                    )
                }
            }
        }
        is AuthState.Loading -> LoadingIndicator()
        is AuthState.Success -> LaunchedEffect(Unit) { onSuccess() }
        is AuthState.Error -> ErrorMessage(message = (authState as AuthState.Error).message)
    }
}

@Composable
private fun LoginForm(
    onAuthenticate: (AuthRequest) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text(stringResource(R.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(stringResource(R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { onAuthenticate(AuthRequest(email, password)) }
    ) {
        Text(stringResource(R.string.log_in))
    }
    Spacer(modifier = Modifier.height(8.dp))
    TextButton(
        onClick = onNavigateToRegister
    ) {
        Text(stringResource(R.string.register))
    }

}

@Composable
private fun RegisterForm(
    onRegister: (RegisterRequest) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    OutlinedTextField(
        value = firstname,
        onValueChange = { firstname = it },
        label = { Text(stringResource(R.string.firstname)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = lastname,
        onValueChange = { lastname = it },
        label = { Text(stringResource(R.string.last_name)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text(stringResource(R.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(stringResource(R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { onRegister(RegisterRequest(firstname, lastname, email, password)) }
    ) {
        Text(stringResource(R.string.register))
    }
    Spacer(modifier = Modifier.height(8.dp))
    TextButton(
        onClick = onNavigateToLogin
    ) {
        Text(stringResource(R.string.log_in))
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
}

sealed class AuthScreenState {
    object Login : AuthScreenState()
    object Register : AuthScreenState()
}