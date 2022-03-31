package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.model.Port
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class ElderBrotherWuDebtCollectionEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.ship.port != Port.HongKong || state.debt < 10000 || state.brotherWuWarning) return

        val braves = state.random.nextInt(100) + 50

        view.showDetail(MessageFormat.format(Strings.ElderBrotherWuSpiel_1, braves))

        GameLogic.skippableDelay(3)

        view.showDetail(Strings.ElderBrotherWuSpiel_2)

        GameLogic.skippableDelay(3)

        view.showDetail(Strings.ElderBrotherWuSpiel_3)

        GameLogic.skippableDelay(5)

        state.brotherWuWarning = true
    }
}
