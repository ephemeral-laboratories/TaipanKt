package garden.ephemeral.games.taipan.model.portactions

import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View

class RetireAction : PortAction {
    override fun run(state: GameState, view: View) {
        view.showRetirement()
    }
}
