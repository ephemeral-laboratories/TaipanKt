package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.model.Money
import garden.ephemeral.games.taipan.model.YesNo
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class OfferShipUpgradeEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.random.nextInt(4) != 0) return

        if (state.random.nextInt(2) == 0) {
            newShip(state, view)
        } else if (state.ship.guns < 1000) {
            newGun(state, view)
        }
    }

    private fun newShip(state: GameState, view: View) {
        val time = state.calendar.monthsSinceStart
        val amount = Money((state.random.nextInt(1000 * (time + 5) / 6) * (state.ship.capacity / 50) + 1000).toLong())

        if (state.cash < amount) {
            return
        }

        view.showTitle(Strings.CompradorsReport)
        // TODO: Should invert the text if damaged.
        view.showDetail(
            MessageFormat.format(
                if (state.ship.damage > 0) {
                    Strings.DoYouWishToTradeInYourShip_Damaged
                } else {
                    Strings.DoYouWishToTradeInYourShip_Fine
                },
                amount.fancyNumbers()
            )
        )

        val choice = view.askYesNo()
        if (choice == YesNo.Yes) {
            state.cash -= amount
            state.ship.expandHoldBy(50)
            state.ship.resetDamage()
        }

        if (state.random.nextInt(2) == 0 && state.ship.guns < 1000) {
            GameLogic.updatePortStatistics(state)
            newGun(state, view)
        }

        GameLogic.updatePortStatistics(state)
    }

    private fun newGun(state: GameState, view: View) {
        val time = state.calendar.monthsSinceStart
        val amount = Money((state.random.nextInt(1000 * (time + 5) / 6) + 500).toLong())

        if (state.cash < amount || state.ship.available < 10) {
            return
        }

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(MessageFormat.format(Strings.DoYouWishToBuyAGun, amount.fancyNumbers()))

        val choice = view.askYesNo()
        if (choice == YesNo.Yes) {
            state.cash -= amount
            state.ship.addGun()
        }

        GameLogic.updatePortStatistics(state)
    }
}
