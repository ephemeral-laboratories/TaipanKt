package garden.ephemeral.games.taipan.model.portactions

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.model.Money
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class VisitBankAction : PortAction {
    override fun run(state: GameState, view: View) {
        var amount: Money

        while (true) {
            view.showTitle(Strings.CompradorsReport)
            view.showDetail(Strings.HowMuchWillYouDeposit)

            amount = view.askUserForMoneyAmount()
            if (amount < 0) {
                amount = state.cash
            }

            if (amount <= state.cash) {
                state.cash -= amount
                state.cashAtBank += amount
                break
            }

            view.showDetail(MessageFormat.format(Strings.YouOnlyHaveXInCash, state.cash.fancyNumbers()))
            GameLogic.skippableDelay(5)
        }

        GameLogic.updatePortStatistics(state)

        while (true) {
            view.showTitle(Strings.CompradorsReport)
            view.showDetail(Strings.HowMuchToWithdraw)

            amount = view.askUserForMoneyAmount()
            if (amount < 0) {
                amount = state.cashAtBank
            }

            if (amount <= state.cashAtBank) {
                state.cash += amount
                state.cashAtBank -= amount
                break
            } else {
                view.showFeedback(MessageFormat.format(Strings.YouOnlyHaveXInTheBank, state.cash.fancyNumbers()))
                GameLogic.skippableDelay(5)
            }
        }

        GameLogic.updatePortStatistics(state)
    }
}
