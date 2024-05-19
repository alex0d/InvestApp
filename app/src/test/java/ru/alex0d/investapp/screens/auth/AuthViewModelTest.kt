package ru.alex0d.investapp.screens.auth

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.alex0d.investapp.data.repositories.UserRepository
import ru.alex0d.investapp.domain.models.AuthResult
import ru.alex0d.investapp.utils.MainDispatcherRule

private const val FIRSTNAME = "Alex"
private const val LASTNAME = "Doe"
private const val EMAIL = "alex@doe.com"
private const val PASSWORD = "Password123!"

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        userRepository = mock()
        authViewModel = AuthViewModel(userRepository)
    }

    @Test
    fun `should update valid firstname`() {
        authViewModel.updateFirstname(FIRSTNAME)

        assertEquals(FIRSTNAME, authViewModel.firstname)
        assertEquals(true, authViewModel.isValidFirstname)
    }

    @Test
    fun `should not update invalid firstname`() {
        authViewModel.updateFirstname("A^") // special character

        assertEquals(false, authViewModel.isValidFirstname)
    }

    @Test
    fun `should update valid lastname`() {
        authViewModel.updateLastname(LASTNAME)

        assertEquals("Doe", authViewModel.lastname)
        assertEquals(true, authViewModel.isValidLastname)
    }

    @Test
    fun `should not update invalid lastname`() {
        authViewModel.updateLastname("^%") // special character

        assertEquals(false, authViewModel.isValidLastname)
    }

    @Test
    fun `should update valid email`() {
        authViewModel.updateEmail(EMAIL)

        assertEquals(EMAIL, authViewModel.email)
        assertEquals(true, authViewModel.isValidEmail)
    }

    @Test
    fun `should not update invalid email`() {
        authViewModel.updateEmail("alexdoe.com") // missing @

        assertEquals(false, authViewModel.isValidEmail)
    }

    @Test
    fun `should update valid password`() {
        authViewModel.updatePassword(PASSWORD)

        assertEquals(PASSWORD, authViewModel.password)
        assertEquals(true, authViewModel.isValidPassword)
    }

    @Test
    fun `should not update invalid password`() {
        authViewModel.updatePassword("pass") // too short

        assertEquals(false, authViewModel.isValidPassword)
    }

    @Test
    fun `should set auth state to success on register success`() = runTest {
        whenever(userRepository.register(FIRSTNAME, LASTNAME, EMAIL, PASSWORD)).thenReturn(AuthResult.SUCCESS)

        authViewModel.updateFirstname(FIRSTNAME)
        authViewModel.updateLastname(LASTNAME)
        authViewModel.updateEmail(EMAIL)
        authViewModel.updatePassword(PASSWORD)
        authViewModel.register()

        assertEquals(AuthState.Success, authViewModel.authState.value)
    }

    @Test
    fun `should set auth state to idle on register failure`() = runTest {
        whenever(userRepository.register(FIRSTNAME, LASTNAME, EMAIL, PASSWORD)).thenReturn(AuthResult.INVALID_CREDENTIALS)

        authViewModel.updateFirstname(FIRSTNAME)
        authViewModel.updateLastname(LASTNAME)
        authViewModel.updateEmail(EMAIL)
        authViewModel.updatePassword(PASSWORD)
        authViewModel.register()

        assertEquals(AuthState.Idle, authViewModel.authState.value)
    }

    @Test
    fun `should set auth state to success on authenticate success`() = runTest {
        whenever(userRepository.authenticate(EMAIL, PASSWORD)).thenReturn(AuthResult.SUCCESS)

        authViewModel.updateEmail(EMAIL)
        authViewModel.updatePassword(PASSWORD)
        authViewModel.authenticate()

        assertEquals(AuthState.Success, authViewModel.authState.value)
    }

    @Test
    fun `should set auth state to idle on authenticate failure`() = runTest {
        whenever(userRepository.authenticate(EMAIL, PASSWORD)).thenReturn(AuthResult.INVALID_CREDENTIALS)

        authViewModel.updateEmail(EMAIL)
        authViewModel.updatePassword(PASSWORD)
        authViewModel.authenticate()

        assertEquals(AuthState.Idle, authViewModel.authState.value)
    }
}