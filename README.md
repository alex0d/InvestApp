# InvestApp - мультиплатформенное приложение для инвестиций 📈
Приложение для инвестиций в акции российских компаний. Доступны таргеты под Android, iOS, Web и ОС Аворора

## Основные возможности приложения ✨
- Поиск акций по тикеру или названию компании
- Просмотр котировок в виде линейного и свечного графиков
- Возможность выбрать таймфрейм для графика
- Покупка и продажа акций
- Просмотр портфеля акций
- Получение специального эзотерического совета от приложения

## Особенности реализации 🚀
- Kotlin Multiplatform / Compose Multiplatform
- Рекомендованная Google [архитектура](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) с использованием Clean Architecture и MVVM
- Подход Single Activity
- Kotlin Coroutines & Flows
- Material Design 3
- 🌞 Light Mode и 🌚 Dark Mode

## Используемые библиотеки 📚
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - современный UI-фреймворк
- [Voyager](https://github.com/adrielcafe/voyager) - мультиплатформенное решение для навигации в Jetpack Compose
- [Ktor Client](https://ktor.io/docs/client-create-and-configure.html) - мультиплатформенный HTTP-клиент
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - хранилище данных, которое использует Kotlin Coroutines и Flow
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - библиотека для работы с SQLite базой данных
- [Koin](https://insert-koin.io/) - Dependency Injection фреймворк
- [Coil](https://coil-kt.github.io/coil/compose/) - библиотека для загрузки изображений
- [Vico](https://github.com/patrykandpatrick/vico) - библиотека для отображения графиков на iOS и Android
- [KoalaPlot](https://github.com/KoalaPlot/koalaplot-core) - мультиплатформенная библиотека для отображения графиков. Используется в Web-версии

## Установка 📲
Установочные файлы для iOS (симулятор), Android и ОС Аврора доступны в разделе [Releases](https://github.com/alex0d/InvestApp/releases/latest)

## Самостоятельная сборка и запуск 🛠
Разверните серверную часть приложения, используя [репозиторий InvestBackend](https://github.com/alex0d/InvestBackend)
### Android 🤖 / iOS 🍏
Соберите и установите приложение на устройство, Android эмулятор или iOS симулятор, используя [Android Studio](https://developer.android.com/studio)
### Web 🌐 \[Alpha версия!]
Выполните Gradle-задачу `:composeApp:wasmJsBrowserDevelopmentRun`
### ОС Аврора 📞
1. Выполните Gradle-задачу `:composeApp:jsBuildForAurora`. Она соберёт JS бандл и поместит его в директорию `<project dir>/auroraApp/InvestApp/qml/kmp`
2. Соберите приложение в [Аврора IDE](https://developer.auroraos.ru/doc/5.1.1/sdk/app_development/start?ysclid=m9kcppvjbn388919785)

## Скриншоты 📸
<p float="left">
    <img src="/media/auth_night.jpg" width="150" />
    <img src="/media/portfolio_night.jpg" width="150" /> 
    <img src="/media/details_night.jpg" width="150" />
    <img src="/media/sell_night.jpg" width="150" />
    <img src="/media/tarot_night.jpg" width="150" />
</p>
<p float="left">
    <img src="/media/auth_day.jpg" width="150" />
    <img src="/media/portfolio_day.jpg" width="150" /> 
    <img src="/media/details_day.jpg" width="150" />
    <img src="/media/sell_day.jpg" width="150" />
    <img src="/media/tarot_day.jpg" width="150" />
</p>
<p>
    <img src="/media/multiplatform_yeah.png" />
</p>
