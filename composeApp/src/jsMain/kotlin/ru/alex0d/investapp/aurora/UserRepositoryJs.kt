@file:Suppress("unused")

package ru.alex0d.investapp.aurora

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alex0d.investapp.data.repositories.UserRepository
import ru.alex0d.investapp.utils.AuroraExport
import ru.alex0d.investapp.utils.extensions.promiseWithEvent

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@AuroraExport
class UserRepositoryJs : KoinComponent {
    private val repository by inject<UserRepository>()

    fun register(firstname: String, lastname: String?, email: String, password: String) = GlobalScope.promiseWithEvent {
        repository.register(firstname, lastname, email, password)
    }

    fun authenticate(email: String, password: String) = GlobalScope.promiseWithEvent {
        repository.authenticate(email, password)
    }

    fun authenticateByTokensInDataBase() = GlobalScope.promiseWithEvent {
        repository.authenticateByTokensInDataBase()
    }

    fun logout() = GlobalScope.promiseWithEvent {
        repository.logout()
    }

    fun getUserFirstname() = GlobalScope.promiseWithEvent {
        repository.getUserFirstname()
    }

    fun getUserLastname() = GlobalScope.promiseWithEvent {
        repository.getUserLastname()
    }

    fun getUserEmail() = GlobalScope.promiseWithEvent {
        repository.getUserEmail()
    }
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val userRepository by lazy { UserRepositoryJs() }