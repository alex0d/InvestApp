package ru.alex0d.investapp.screens.auth

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.alex0d.investapp.MainActivity
import ru.alex0d.investapp.R

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private var firstname by mutableStateOf("")
    private var lastname by mutableStateOf("")
    private var email by mutableStateOf("")
    private var password by mutableStateOf("")

    private var isValidFirstname by mutableStateOf(false)
    private var isValidLastname by mutableStateOf(false)
    private var isValidEmail by mutableStateOf(false)
    private var isValidPassword by mutableStateOf(false)

    private lateinit var firstnameString: String
    private lateinit var lastnameString: String
    private lateinit var emailString: String
    private lateinit var passwordString: String

    private lateinit var logInString: String
    private lateinit var registerString : String

    @Before
    fun setUp() {
        firstnameString = rule.activity.getString(R.string.firstname)
        lastnameString = rule.activity.getString(R.string.lastname_optional)
        emailString = rule.activity.getString(R.string.email)
        passwordString = rule.activity.getString(R.string.password)

        logInString = rule.activity.getString(R.string.log_in)
        registerString = rule.activity.getString(R.string.register)

        rule.activity.setContent {
            AuthScreenContent(
                modifier = Modifier,
                authState = AuthState.Idle,
                firstname = firstname,
                onFirstnameUpdate = { firstname = it },
                isValidFirstname = isValidFirstname,
                lastname = lastname,
                onLastnameUpdate = { lastname = it },
                isValidLastname = isValidLastname,
                email = email,
                onEmailUpdate = { email = it },
                isValidEmail = isValidEmail,
                password = password,
                onPasswordUpdate = { password = it },
                isValidPassword = isValidPassword,
                authenticate = { },
                register = { },
            )
        }
    }

    @After
    fun resetToInitialState() {
        firstname = ""
        lastname = ""
        email = ""
        password = ""
    }

    @Test
    fun shouldSwitchToRegistrationScreenAndBack() {
        rule.onNodeWithText(firstnameString).assertDoesNotExist()
        rule.onNodeWithText(lastnameString).assertDoesNotExist()
        rule.onNodeWithText(emailString).assertExists()
        rule.onNodeWithText(passwordString).assertExists()

        rule.onNodeWithText(registerString).performClick()

        rule.onNodeWithText(firstnameString).assertExists()
        rule.onNodeWithText(lastnameString).assertExists()
        rule.onNodeWithText(emailString).assertExists()
        rule.onNodeWithText(passwordString).assertExists()

        rule.onNodeWithText(logInString).performClick()

        rule.onNodeWithText(firstnameString).assertDoesNotExist()
        rule.onNodeWithText(lastnameString).assertDoesNotExist()
        rule.onNodeWithText(emailString).assertExists()
        rule.onNodeWithText(passwordString).assertExists()
    }

    @Test
    fun logInButtonShouldBeEnabledOnlyWhenEmailAndPasswordAreValid() {
        rule.onNodeWithText(logInString).assertIsNotEnabled()

        isValidEmail = true
        rule.onNodeWithText(logInString).assertIsNotEnabled()

        isValidPassword = true
        rule.onNodeWithText(logInString).assertIsEnabled()
    }

    @Test
    fun registerButtonShouldBeEnabledOnlyWhenAllFieldsAreValid() {
        rule.onNodeWithText(registerString).performClick()

        rule.onNodeWithText(registerString).assertIsNotEnabled()

        isValidFirstname = true
        rule.onNodeWithText(registerString).assertIsNotEnabled()

        isValidLastname = true
        rule.onNodeWithText(registerString).assertIsNotEnabled()

        isValidEmail = true
        rule.onNodeWithText(registerString).assertIsNotEnabled()

        isValidPassword = true
        rule.onNodeWithText(registerString).assertIsEnabled()
    }
}