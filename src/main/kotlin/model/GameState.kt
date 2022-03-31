package garden.ephemeral.games.taipan.model

import kotlin.random.Random

class GameState(
    val random: Random = Random,
    var firmName: String = "",
    var cash: Money = Money.Zero,
    var cashAtBank: Money = Money.Zero,
    var debt: Money = Money.Zero,
    var enemyCountMultiplier: Double = 20.0,
    var enemyDamageMultiplier: Double = 0.5,
    val prices: MutableMap<CargoType, Money> = mutableMapOf(),
    val hongKongWarehouse: CargoStorage = CargoStorage(10_000),
    val ship: Ship = Ship(60),
    var battleProbability: Int = 0,
    var calendar: Calendar = Calendar.atStart(),
    var liYuenTrust: Int = 0,
    var brotherWuWarning: Boolean = false,
    var brotherWuBailoutCount: Int = 0,
)
