package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.CargoType
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class GoodPricesEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.random.nextInt(9) != 0) return

        val type = CargoType.values()[state.random.nextInt(4)]
        val direction = state.random.nextInt(2)

        view.showTitle(Strings.CompradorsReport)
        val line2 = if (direction == 0) {
            state.prices[type] = state.prices[type]!! / 5
            MessageFormat.format(Strings.HasDroppedToX, state.prices[type])
        } else {
            state.prices[type] = state.prices[type]!! * state.random.nextInt(5) + 5
            MessageFormat.format(Strings.HasRisenToX, state.prices[type])
        }

        view.showDetail(
            MessageFormat.format(Strings.ThePriceOfX, type.localizedName),
            line2
        )

        GameLogic.skippableDelay(3)
    }
}
