# InvestApp - приложение для инвестиций 📈
Приложение для инвестиций в акции российских компаний

## Основные возможности приложения ✨
- Поиск акций по тикеру или названию компании
- Просмотр котировок в виде линейного и свечного графиков
- Возможность выбрать таймфрейм для графика
- Покупка и продажа акций
- Просмотр портфеля акций
- Получение специального эзотерического совета от приложения

## Особенности реализации 🚀
- Рекомендованная Google [архитектура](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) с использованием Clean Architecture и MVVM
- Подход Single Activity
- Kotlin Coroutines & Flows
- Material Design 3
- 🌞 Light Mode и 🌚 Dark Mode

## Используемые библиотеки 📚
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - современный UI-фреймворк
- [Compose Destinations](https://github.com/raamcosta/compose-destinations) - [KSP](https://kotlinlang.org/docs/ksp-overview.html) библиотека, генерирующая код для простой и типобезопасной навигации в Jetpack Compose
- [Retrofit](https://square.github.io/retrofit/) - HTTP-клиент для Android
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - хранилище данных, которое использует Kotlin Coroutines и Flow
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - библиотека для работы с SQLite базой данных
- [Koin](https://insert-koin.io/) - Dependency Injection фреймворк
- [Coil](https://coil-kt.github.io/coil/compose/) - библиотека для загрузки изображений
- [Vico](https://github.com/patrykandpatrick/vico) - библиотека для отображения графиков

## Установка 📲
Вы можете установить приложение, скачав APK-файл из раздела [Releases](https://github.com/alex0d/InvestApp/releases/latest)

## Самостоятельная сборка и запуск 🛠
1. Разверните серверную часть приложения, используя [репозиторий InvestmentAnalyst](https://github.com/alex0d/InvestmentAnalyst)
2. Создайте файл `secrets.properties` в корне проекта и добавьте в него адрес вашего сервера, например:
    ```
    INVEST_API_BASE_URL=http://10.0.2.2:8080
    ```
3. Соберите и установите приложение на устройство или эмулятор, используя [Android Studio](https://developer.android.com/studio)

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
