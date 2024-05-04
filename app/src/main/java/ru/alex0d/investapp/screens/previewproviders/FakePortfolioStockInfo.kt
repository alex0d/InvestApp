package ru.alex0d.investapp.screens.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.alex0d.investapp.data.remote.models.PortfolioStockInfoDto

internal class FakePortfolioStockInfoDto : PreviewParameterProvider<PortfolioStockInfoDto> {
    override val values: Sequence<PortfolioStockInfoDto>
        get() = sequenceOf(
            PortfolioStockInfoDto(
                uid = "64c0da45-4c90-41d4-b053-0c66c7a8ddcd",
                ticker = "SBERP",
                classCode = "SPEQ",
                name = "Сбер Банк - привилегированные акции",
                amount = 10,
                price = 308.15,
                totalValue = 3081.50,
                profit = -215.10,
                profitPercent = -10.00,
                logoUrl = "sber3",
                backgroundColor = "#309c0b",
                textColor = "#ffffff"
            ),
            PortfolioStockInfoDto(
                uid = "de08affe-4fbd-454e-9fd1-46a81b23f870",
                ticker = "POSI",
                classCode = "PSSU",
                name = "Positive Technologies",
                amount = 5,
                price = 2978.60,
                totalValue = 14893.0,
                profit = 1631.01,
                profitPercent = 12.29,
                logoUrl = "RU000A103X66",
                backgroundColor = "#ff0000",
                textColor = "#ffffff"
            )
        )
}