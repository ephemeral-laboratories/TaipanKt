package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.*
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class LiYuenExtortionEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.ship.port != Port.HongKong || state.liYuenTrust != 0 || state.cash <= 0) return

        val time = state.calendar.monthsSinceStart

        var i = 1.8
        var j = 0

        if (time > 12) {
            i = 1.0
            j = state.random.nextInt(1000 * time) + 1000 * time
        }

        var amount = state.cash / i * state.random.nextDouble() + j.toLong()

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(MessageFormat.format(Strings.LiYuenAsksXInDonation, amount))

        var choice = view.askYesNo()
        if (choice == YesNo.Yes) {
            if (amount <= state.cash) {
                state.cash -= amount
                state.liYuenTrust = 1
            } else {
                view.showDetail(Strings.YouDoNotHaveEnoughCash)
                GameLogic.skippableDelay(3)

                view.showDetail(Strings.DoYouWantElderBrotherWuToMakeUpTheDifference)
                choice = view.askYesNo()
                if (choice == YesNo.Yes) {
                    amount -= state.cash
                    state.debt += amount
                    state.cash = Money.Zero
                    state.liYuenTrust = 1

                    view.showDetail(Strings.ElderBrotherHasGivenLiYuenTheDifference)
                    GameLogic.skippableDelay(5)
                } else {
                    state.cash = Money.Zero
                    view.showDetail(Strings.ElderBrotherWuWillNotPayLiYuenTheDifference)
                    GameLogic.skippableDelay(5)
                }
            }
        }

        GameLogic.updatePortStatistics(state)
    }
}
