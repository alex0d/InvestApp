package ru.alex0d.investapp.di

import org.koin.core.scope.Scope

typealias NativeInjectionFactory<T> = Scope.() -> T