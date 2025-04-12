package ru.alex0d.investapp.domain.models

data class TarotPrediction(
    val card: TarotCard,
    val prediction: String
)

enum class TarotCard {
    THE_FOOL,
    THE_MAGICIAN,
    THE_HIGH_PRIESTESS,
    THE_EMPRESS,
    THE_EMPEROR,
    THE_HIEROPHANT,
    THE_LOVERS,
    THE_CHARIOT,
    JUSTICE,
    THE_HERMIT,
    WHEEL_OF_FORTUNE,
    STRENGTH,
    THE_HANGED_MAN,
    DEATH,
    TEMPERANCE,
    THE_DEVIL,
    THE_TOWER,
    THE_STAR,
    THE_MOON,
    THE_SUN,
    JUDGEMENT,
    THE_WORLD
}