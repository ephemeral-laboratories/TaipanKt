package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View

class LiYuenTrustEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.random.nextInt(20) != 0) return

        if (state.liYuenTrust > 0) {
            state.liYuenTrust++
        }

        if (state.liYuenTrust == 4) {
            state.liYuenTrust = 0
        }
    }
}
