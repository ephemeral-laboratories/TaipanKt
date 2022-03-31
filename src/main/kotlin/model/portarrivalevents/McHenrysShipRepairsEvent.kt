package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.model.Port
import garden.ephemeral.games.taipan.model.YesNo
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class McHenrysShipRepairsEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.ship.port != Port.HongKong || state.ship.damage <= 0) return

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(Strings.McHenryHasArrived)

        val choice = view.askYesNo()

        if (choice != YesNo.Yes) {
            return
        }

        val percent = (state.ship.damage.toDouble() / state.ship.capacity * 100).toInt()
        val time = state.calendar.monthsSinceStart

        val k = (time + 3) / 4
        val br = (state.random.nextInt(60 * k) + 25 * k) * state.ship.capacity / 50
        val repairPrice = br * state.ship.damage + 1

        view.showDetail(MessageFormat.format(Strings.HowMuchWillYeSpend, percent, repairPrice))

        while (true) {
            var amount = view.askUserForMoneyAmount()
            if (amount < 0) {
                amount = state.cash
            } else if (amount > state.cash) {
                continue
            }

            state.cash -= amount
            state.ship.repair((amount.Amount.toDouble() / br + 0.5).toInt())
            GameLogic.updatePortStatistics(state)
            break
        }
    }
}
