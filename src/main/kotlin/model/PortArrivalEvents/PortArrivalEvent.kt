package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View

interface PortArrivalEvent {
    fun run(state: GameState, view: View)
}
