package ru.alex0d.investapp.di

import ru.alex0d.investapp.data.local.UserDataStore

val nativeModule = makeNativeModule(
    userDataStore = {
        UserDataStore(dataStore = get())
    }
)