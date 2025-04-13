package ru.alex0d.investapp.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.alex0d.investapp.data.local.UserDataStore

fun makeNativeModule(
    userDataStore: NativeInjectionFactory<UserDataStore>,
): Module {
    return module {
        single { userDataStore() }
    }
}