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
class UserRepositoryJS : KoinComponent {
    private val userRepository by inject<UserRepository>()

    fun register(firstname: String, lastname: String?, email: String, password: String) = GlobalScope.promiseWithEvent(true) {
        userRepository.register(firstname, lastname, email, password)
    }

    fun authenticate(email: String, password: String) = GlobalScope.promiseWithEvent(true) {
        userRepository.authenticate(email, password)
    }

    fun smth() = GlobalScope.promiseWithEvent(false) {
        "hello"
    }
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val userRepository by lazy { UserRepositoryJS() }