package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.CargoType
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.model.Port
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class OpiumSeizedEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.ship.port == Port.HongKong || state.random.nextInt(18) != 0 ||
            !state.ship.hasStored(CargoType.Opium)
        ) {
            return
        }

        val fine = state.cash / 1.8 * state.random.nextDouble() + 1

        state.ship.removeCargo(CargoType.Opium, state.ship.unitsStored(CargoType.Opium))
        state.cash -= fine

        GameLogic.updatePortStatistics(state)

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(MessageFormat.format(Strings.AuthoritiesSeizedYourOpium, fine.fancyNumbers()))

        GameLogic.skippableDelay(5)
    }
}
