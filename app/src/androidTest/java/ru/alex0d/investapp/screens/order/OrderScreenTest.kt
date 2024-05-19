package ru.alex0d.investapp.screens.order

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.alex0d.investapp.MainActivity
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.Share

@RunWith(AndroidJUnit4::class)
class OrderScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private var share = Share(
        uid = "64c0da45-4c90-41d4-b053-0c66c7a8ddcd",
        figi = "TCS109029557",
        ticker = "SBERP",
        classCode = "SPEQ",
        isin = "RU0009029557",
        currency = "rub",
        name = "Сбер Банк - привилегированные акции",
        countryOfRisk = "RU",
        countryOfRiskName = "Российская Федерация",
        sector = "financial",
        lot = 1,
        lastPrice = 295.94,
        url = "sber3",
        backgroundColor = "#309c0b",
        textColor = "#ffffff"
    )
    private var availableLots by mutableStateOf(10)
    private var lotsInput by mutableStateOf("")  // Represents user input for lots
    private var totalValue by mutableStateOf("")
    private var orderAction by mutableStateOf(OrderAction.BUY)

    private lateinit var buyString: String
    private lateinit var sellString: String

    @Before
    fun setUp() {
        buyString = rule.activity.getString(R.string.buy)
        sellString = rule.activity.getString(R.string.sell)

        rule.activity.setContent {
            OrderOnDetailsFetched(
                share = share,
                availableLots = availableLots,
                lotsInput = lotsInput,
                totalValue = totalValue,
                orderAction = orderAction,
                onUpdateInputLots = { lotsInput = it },
                onDecreaseLots = { if (lotsInput.toInt() > 0) lotsInput = (lotsInput.toInt() - 1).toString() },
                onIncreaseLots = { lotsInput = (lotsInput.toInt() + 1).toString() },
                onConfirmOrder = {  }
            )
        }
    }

    @After
    fun resetToInitialState() {
        lotsInput = ""
        availableLots = 10
        totalValue = ""
        orderAction = OrderAction.BUY
    }

    @Test
    fun buyButtonShouldBeEnabledOnlyWhenLotsInputGreaterThanZero() {
        rule.onNodeWithText(buyString).assertIsNotEnabled()
        lotsInput = "1"
        rule.onNodeWithText(buyString).assertIsEnabled()
    }

    @Test
    fun sellButtonShouldBeEnabledOnlyWhenLotsInputGreaterThanZero() {
        orderAction = OrderAction.SELL
        setUp()  // Refresh the content with new orderAction
        rule.onNodeWithText(sellString).assertIsNotEnabled()
        lotsInput = "1"
        rule.onNodeWithText(sellString).assertIsEnabled()
    }

    @Test
    fun decreaseButtonShouldBeDisabledWhenLotsInputIsZero() {
        lotsInput = "0"
        rule.onNodeWithContentDescription("-").assertIsNotEnabled()
        lotsInput = "1"
        rule.onNodeWithContentDescription("-").assertIsEnabled()
    }

    @Test
    fun increaseButtonShouldRespectAvailableLotsWhenSellAction() {
        orderAction = OrderAction.SELL
        setUp()  // Refresh the content with new orderAction
        availableLots = 5
        lotsInput = "5"
        rule.onNodeWithContentDescription("+").assertIsNotEnabled()
        lotsInput = "4"
        rule.onNodeWithContentDescription("+").assertIsEnabled()
    }

    @Test
    fun totalValueShouldBeDisplayedCorrectly() {
        lotsInput = "3"
        val lastPrice = share.lastPrice
        totalValue = (lotsInput.toInt() * lastPrice).toString()
        rule.onNodeWithText(totalValue).assertExists()
    }
}