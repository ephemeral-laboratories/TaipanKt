package garden.ephemeral.games.taipan.model.portarrivalevents

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.*
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class ElderBrotherWuDealEvent : PortArrivalEvent {
    override fun run(state: GameState, view: View) {
        if (state.ship.port != Port.HongKong) return

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(Strings.DoYouHaveBusinessWithElderBrotherWu)

        while (true) {
            var choice = view.askYesNo()
            if (choice == YesNo.No) {
                break
            }

            if (state.cash <= 0 && state.cashAtBank <= 0 && state.ship.guns == 0 && state.ship.isEmpty && state.hongKongWarehouse.isEmpty) {
                val i = Money(state.random.nextInt(1500).toLong() + 500)

                state.brotherWuBailoutCount++
                val j = Money(state.random.nextInt(2000).toLong() * state.brotherWuBailoutCount + 1500)

                while (true) {
                    view.showTitle(Strings.CompradorsReport)
                    view.showDetail(MessageFormat.format(Strings.ElderBrotherIsAwareOfYourPlight, i, j))

                    choice = view.askYesNo()
                    when (choice) {
                        YesNo.Yes -> {
                            state.cash += i
                            state.debt += j
                            GameLogic.updatePortStatistics(state)
                            view.showTitle(Strings.CompradorsReport)
                            view.showDetail(Strings.VeryWellGoodJoss)
                            GameLogic.skippableDelay(5)
                            return
                        }

                        YesNo.No -> {
                            view.showTitle(Strings.CompradorsReport)
                            view.showDetail(Strings.VeryWellGameOver)
                            GameLogic.skippableDelay(5)
                            GameLogic.gameOver(state)
                            break
                        }
                    }
                }
            }

            if (state.cash > 0 && state.debt > 0) {
                while (true) {
                    view.showTitle(Strings.CompradorsReport)
                    view.showDetail(Strings.HowMuchToRepay)

                    var wu = view.askUserForMoneyAmount()
                    if (wu < 0) {
                        wu = state.cash
                    }

                    if (wu <= state.cash) {
                        state.cash -= wu
                        if (wu > state.debt && state.debt > 0) {
                            state.debt -= wu + 1
                        } else {
                            state.debt -= wu
                        }

                        break
                    }

                    view.showDetail(Strings.YouOnlyHaveXInCash, state.cash.fancyNumbers())
                    GameLogic.skippableDelay(5)
                }
            }

            GameLogic.updatePortStatistics(state)

            while (true) {
                view.showTitle(Strings.CompradorsReport)
                view.showDetail(Strings.HowMuchToBorrow)

                var wu = view.askUserForMoneyAmount()
                if (wu < 0) {
                    wu = state.cash * 2
                }

                if (wu <= state.cash * 2) {
                    state.cash += wu
                    state.debt += wu
                    break
                } else {
                    view.showDetail(Strings.HeWontLoanYouSoMuch)
                    GameLogic.skippableDelay(5)
                }
            }

            GameLogic.updatePortStatistics(state)

            break
        }

        if (state.debt <= 20000 || state.cash <= 0 || state.random.nextInt(5) != 0) return

        val num = state.random.nextInt(3) + 1

        state.cash = Money.Zero
        GameLogic.updatePortStatistics(state)

        view.showTitle(Strings.CompradorsReport)
        view.showDetail(MessageFormat.format(Strings.BadJossYouHaveBeenRobbed, num))

        GameLogic.skippableDelay(5)
    }
}
