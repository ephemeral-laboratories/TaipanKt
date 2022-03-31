package garden.ephemeral.games.taipan.model.portactions

import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View

interface PortAction {
    fun run(state: GameState, view: View)
}
