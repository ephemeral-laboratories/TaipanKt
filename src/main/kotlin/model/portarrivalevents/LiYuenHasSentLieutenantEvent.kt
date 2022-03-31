package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.model.Port
import garden.ephemeral.games.taipan.view.View

class LiYuenHasSentLieutenantEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.ship.port == Port.HongKong || state.liYuenTrust != 0 || state.random.nextInt(4) == 0) return

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(Strings.LiYuenHasSentALieutenant)

        GameLogic.skippableDelay(3)
    }
}
