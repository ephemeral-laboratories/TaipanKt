package garden.ephemeral.games.taipan.model

class InitialState private constructor(
    private val cash: Money,
    private val debt: Money,
    private val hold: Int,
    private val guns: Int,
    private val liYuenTrust: Int,
    private val battleProbability: Int,
) {
    fun newGame(firmName: String): GameState {
        return GameState(
            firmName = firmName,
            cash = cash,
            debt = debt,
            liYuenTrust = liYuenTrust,
            battleProbability = battleProbability,
            ship = Ship(hold)
        ).apply {
            ship.addGuns(guns)
            ship.capacity = hold
        }
    }

    companion object {
        fun withCash(): InitialState {
            return InitialState(Money(400), Money(5000), 60, 0, 0, 10)
        }

        fun withGuns(): InitialState {
            return InitialState(Money.Zero, Money.Zero, 10, 5, 1, 7)
        }
    }
}
