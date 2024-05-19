package ru.alex0d.investapp.screens.profile

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.alex0d.investapp.data.repositories.UserRepository
import ru.alex0d.investapp.utils.MainDispatcherRule

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setup() {
        userRepository = mock()
        profileViewModel = ProfileViewModel(userRepository)
    }

    @Test
    fun `should call repository logout when logout`() = runTest {
        profileViewModel.logout()

        verify(userRepository).logout()
    }
}