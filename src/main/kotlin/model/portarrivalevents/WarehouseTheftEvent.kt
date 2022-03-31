package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.CargoType
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View

class WarehouseTheftEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.random.nextInt(50) != 0 || !state.hongKongWarehouse.isNotEmpty) return

        for (type in CargoType.values()) {
            val available = state.hongKongWarehouse.unitsStored(type)
            val plundered = (available * (0.8 / 1.8) * state.random.nextDouble()).toInt()
            state.hongKongWarehouse.removeCargo(type, plundered)
        }

        GameLogic.updatePortStatistics(state)

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(Strings.LargeTheftFromWarehouse)

        GameLogic.skippableDelay(5)
    }
}
