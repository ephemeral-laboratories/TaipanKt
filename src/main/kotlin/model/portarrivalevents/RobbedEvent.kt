package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class RobbedEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.cash <= 25000 || state.random.nextInt(20) != 0) return

        val robbed = state.cash / 1.4 * state.random.nextDouble()

        state.cash -= robbed
        GameLogic.updatePortStatistics(state)

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(MessageFormat.format(Strings.YouGotRobbed, robbed.fancyNumbers()))

        GameLogic.skippableDelay(5)
    }
}
