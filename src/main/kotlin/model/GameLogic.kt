package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.portactions.BuyAction
import garden.ephemeral.games.taipan.model.portactions.RetireAction
import garden.ephemeral.games.taipan.model.portactions.SellAction
import garden.ephemeral.games.taipan.model.portactions.TransferCargoAction
import garden.ephemeral.games.taipan.model.portactions.VisitBankAction
import garden.ephemeral.games.taipan.model.portarrivalevents.ElderBrotherWuDealEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.ElderBrotherWuDebtCollectionEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.GoodPricesEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.LiYuenExtortionEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.LiYuenHasSentLieutenantEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.LiYuenTrustEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.McHenrysShipRepairsEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.OfferShipUpgradeEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.OpiumSeizedEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.RobbedEvent
import garden.ephemeral.games.taipan.model.portarrivalevents.WarehouseTheftEvent
import garden.ephemeral.games.taipan.view.StubbedView
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

object GameLogic {
    private var view: View = StubbedView()

    fun main() {
        val state = initialiseGame()
        mainLoop(state)
    }

    private fun initialiseGame(): GameState {
        splashIntro()

        val firmName = view.askForFirmName(Strings.WhatWillYouNameYourFirm_1, Strings.WhatWillYouNameYourFirm_2)
        val initialState = cashOrGuns()
        val state = initialState.newGame(firmName)

        // XXX: Maybe this should be elsewhere.
        setPrices(state)

        return state
    }

    private fun mainLoop(state: GameState) {
        val portArrivalEvents = listOf(
            LiYuenExtortionEvent(),
            McHenrysShipRepairsEvent(),
            ElderBrotherWuDebtCollectionEvent(),
            ElderBrotherWuDealEvent(),
            OfferShipUpgradeEvent(),
            OpiumSeizedEvent(),
            WarehouseTheftEvent(),
            LiYuenTrustEvent(),
            LiYuenHasSentLieutenantEvent(),
            GoodPricesEvent(),
            RobbedEvent(),
        )
        val portActions = mapOf(
            PortActivity.Buy to BuyAction(),
            PortActivity.Sell to SellAction(),
            PortActivity.VisitBank to VisitBankAction(),
            PortActivity.TransferCargo to TransferCargoAction(),
            PortActivity.Retire to RetireAction(),
        )

        while (true) {
            updatePortStatistics(state)

            for (portArrivalEvent in portArrivalEvents) {
                portArrivalEvent.run(state, view)
            }

            while (true) {
                while (true) {
                    val choice = portChoices(state)
                    if (choice == PortActivity.QuitTrading) {
                        break
                    }

                    portActions[choice]!!.run(state, view)

                    updatePortStatistics(state)
                }

                if (!state.ship.isOverloaded) {
                    quitTrading(state)
                    break
                }

                overload()
            }
        }
    }

    private fun splashIntro() {
        view.showSplashUntilKeyPressed()
    }

    private fun cashOrGuns(): InitialState {
        // Still back to front. Should create both initial states upfront,
        // send the two to the view to pick which.
        return view.selectInitialState(
            Strings.CashOrGuns,
            InitialState.withCash(),
            InitialState.withGuns()
        )
    }

    private fun setPrices(state: GameState) {
        val basePriceMultiplier = mapOf(
            CargoType.Opium to 1000,
            CargoType.Silk to 100,
            CargoType.Arms to 10,
            CargoType.General to 1,
        )

        val basePriceByPort = mapOf(
            Port.HongKong to mapOf(
                CargoType.Opium to 11, CargoType.Silk to 11, CargoType.Arms to 12, CargoType.General to 10
            ),
            Port.ShangHai to mapOf(
                CargoType.Opium to 16, CargoType.Silk to 14, CargoType.Arms to 16, CargoType.General to 11
            ),
            Port.Nagasaki to mapOf(
                CargoType.Opium to 15, CargoType.Silk to 15, CargoType.Arms to 10, CargoType.General to 12
            ),
            Port.Saigon to mapOf(
                CargoType.Opium to 14, CargoType.Silk to 16, CargoType.Arms to 11, CargoType.General to 13
            ),
            Port.Manila to mapOf(
                CargoType.Opium to 12, CargoType.Silk to 10, CargoType.Arms to 13, CargoType.General to 14
            ),
            Port.Singapore to mapOf(
                CargoType.Opium to 10, CargoType.Silk to 13, CargoType.Arms to 14, CargoType.General to 15
            ),
            Port.Batavia to mapOf(
                CargoType.Opium to 13, CargoType.Silk to 12, CargoType.Arms to 15, CargoType.General to 16
            ),
        )

        val basePrices = basePriceByPort[state.ship.port]!!
        for (type in CargoType.values()) {
            state.prices[type] =
                Money(basePrices[type]!!.toLong()) / 2 * (state.random.nextInt(3) + 1) * basePriceMultiplier[type]!!
        }
    }

    fun updatePortStatistics(state: GameState) {
        val statusPercentage = 100 - (state.ship.damage.toFloat() / state.ship.capacity * 100).toInt()

        view.clearScreen()

        view.showPortStatsScreen()

        for (type in CargoType.values()) {
            view.showWarehouseCargoQuantity(type, state.hongKongWarehouse.unitsStored(type))
        }

        if (state.ship.isOverloaded) {
            view.showShipOverloaded()
        } else {
            view.showShipAvailable(state.ship.available)
        }

        for (type in CargoType.values()) {
            view.showShipCargoQuantity(type, state.ship.unitsStored(type))
        }

        view.showCash(state.cash.fancyNumbers())
        view.showWarehouseInUse(state.hongKongWarehouse.totalUnitsStored)
        view.showWarehouseAvailable(state.hongKongWarehouse.available)
        view.showShipGuns(state.ship.guns)
        view.showCashAtBank(state.cashAtBank.fancyNumbers())

        view.showBattleStatus(
            MessageFormat.format(
                Strings.MidMonthDateFormat, state.calendar.month.localizedName, state.calendar.year
            )
        )

        view.showPort(state.ship.port)
        view.showDebt(state.debt.fancyNumbers())

        val status = Status.forPercentage(statusPercentage)
        view.showShipStatus(status, statusPercentage)
    }

    private fun portChoices(state: GameState): PortActivity {
        view.showTitle(Strings.CompradorsReport)
        view.showDetail(
            "Taipan, present prices per unit here are\n   Opium:          Silk:\n   Arms:           General:\n"
        )

        for (type in CargoType.values()) {
            view.showPrice(type, state.prices[type]!!)
        }

        return if (state.ship.port == Port.HongKong) {
            if (state.cash + state.cashAtBank >= 1_000_000) {
                view.askUserForHomePortActivityIncludingRetirement(Strings.ShallIBuySellVisitBankTransferCargoQuitTradingOrRetire)
            } else {
                view.askUserForHomePortActivity(Strings.ShallIBuySellVisitBankTransferCargoOrQuitTrading)
            }
        } else {
            view.askUserForPortActivity(Strings.ShallIBuySellOrQuitTrading)
        }
    }

    private fun quitTrading(state: GameState) {
        view.showTitle(Strings.CompradorsReport)
        view.showDetail(Strings.WhereToGo)

        while (true) {
            val destination = view.askUserForPort()
            if (destination == state.ship.port) {
                view.showFeedback(Strings.YouAreAlreadyHere)
                skippableDelay(5)
            } else {
                state.ship.port = destination
                break
            }
        }

        view.showPort(Port.AtSea)
        view.showTitle(Strings.CaptainsReport)

        var battleResult = runNormalBattleEvent(state)

        battleResult = runLiYuenBattleEvent(state, battleResult)

        if (battleResult.first != BattleResult.NoBattle) {
            updatePortStatistics(state)
            view.showPort(Port.AtSea)

            view.showTitle(Strings.CaptainsReport)

            // ReSharper disable once SwitchStatementHandlesSomeKnownEnumValuesWithDefault
            when (battleResult.first) {
                BattleResult.Victory -> {
                    val booty = battleResult.second
                    view.showDetail(MessageFormat.format(Strings.WeCapturedSomeBooty, booty.fancyNumbers()))
                    state.cash += booty
                }

                BattleResult.Retreat -> {
                    view.showDetail(Strings.WeMadeIt)
                }

                // Must be BattleResult.Sunk
                else -> {
                    view.showDetail(Strings.TheBuggersGotUs)
                    skippableDelay(5)
                    gameOver(state)
                    return
                }
            }

            skippableDelay(3)
        }

        stormAtSeaEvent(state)

        advanceTime(state)

        view.showDetail(MessageFormat.format(Strings.ArrivingAt, state.ship.port.localizedName))

        skippableDelay(3)
    }

    private fun runNormalBattleEvent(state: GameState): Pair<BattleResult, Money> {
        var result = Pair(BattleResult.NoBattle, Money.Zero)

        if (state.random.nextInt(state.battleProbability) != 0) return result

        val numShips = (state.random.nextInt(state.ship.capacity / 10 + state.ship.guns) + 1)
            .coerceAtMost(9999)

        view.showDetail(MessageFormat.format(Strings.HostileShipsApproaching, numShips))

        skippableDelay(3)

        result = seaBattle(state, BattleType.Generic, numShips)

        if (result.first != BattleResult.EnemyChasedOffByLiYuen) return result

        updatePortStatistics(state)
        view.showPort(Port.AtSea)
        view.showTitle(Strings.CaptainsReport)
        view.showDetail(Strings.LiYuensFleetDroveThemOff)

        skippableDelay(3)

        return result
    }

    private fun runLiYuenBattleEvent(state: GameState, result: Pair<BattleResult, Money>): Pair<BattleResult, Money> {
        if ((result.first != BattleResult.NoBattle || state.random.nextInt(4 + 8 * state.liYuenTrust) != 0) &&
            result.first != BattleResult.EnemyChasedOffByLiYuen
        ) {
            return result
        }

        view.showDetail(Strings.LiYuensPirates)
        skippableDelay(3)

        if (state.liYuenTrust > 0) {
            view.showDetail(Strings.GoodJossTheyLetUsBe)
            skippableDelay(3)

            // C port from CymonsGames had a return here, but on consulting the original BASIC code,
            // the correct behaviour is to jump past the booty. One way to do that is to pretend
            // that there was no battle.
            return Pair(BattleResult.NoBattle, Money.Zero)
        }

        val numShips = state.random.nextInt(state.ship.capacity / 5 + state.ship.guns) + 5
        view.showDetail(MessageFormat.format(Strings.XShipsOfLiYuensPirateFleet, numShips))
        skippableDelay(3)
        return seaBattle(state, BattleType.LiYuen, numShips)
    }

    private fun stormAtSeaEvent(state: GameState) {
        if (state.random.nextInt(10) != 0) return

        view.showDetail(Strings.Storm)
        skippableDelay(3)

        if (state.random.nextInt(30) == 0) {
            view.showDetail(Strings.IThinkWereGoingDown)
            skippableDelay(3)

            if (state.ship.damage / state.ship.capacity * 3 * state.random.nextDouble() >= 1) {
                view.showDetail(Strings.WereGoingDown)
                skippableDelay(5)
                gameOver(state)
            }
        }

        view.showDetail(Strings.WeMadeIt2)
        skippableDelay(3)

        if (state.random.nextInt(3) != 0) return

        val originalDestination = state.ship.port
        while (state.ship.port == originalDestination) {
            state.ship.port = Port.values()[state.random.nextInt(1, 8)]
        }

        view.showDetail(MessageFormat.format(Strings.WeveBeenBlownOffCourseTo, state.ship.port.localizedName))

        skippableDelay(3)
    }

    private fun advanceTime(state: GameState) {
        state.calendar.advance()
        if (state.calendar.month == Month.Jan) {
            state.enemyCountMultiplier += 100
            state.enemyDamageMultiplier += 0.5
        }

        state.debt += state.debt * 0.1
        state.cashAtBank += state.cashAtBank * 0.005
        setPrices(state)
    }

    private fun overload() {
        view.showTitle(Strings.CompradorsReport)
        view.showDetail(Strings.YourShipIsOverloaded)
        skippableDelay(5)
    }

    private fun seaBattle(state: GameState, battleType: BattleType, numShipsAtStart: Int): Pair<BattleResult, Money> {
        var numShips = numShipsAtStart
        var numOnScreen = 0
        val shipsOnScreen = mutableMapOf<LorchaPosition, Lorcha>()
        val time = state.calendar.monthsSinceStart
        val s0 = numShips
        var ok = 0
        var ik = 1

        val booty = Money((time / 4 * 1000 * numShips + state.random.nextInt(1000) + 250).toLong())

        for (position in LorchaPosition.All) {
            shipsOnScreen.remove(position)
        }

        var orders = view.checkForOrders()
        fightStats(state, numShips, orders)

        while (numShips > 0) {
            val statusPercentage = (100 - state.ship.damage.toDouble() / state.ship.capacity * 100).toInt()
            if (statusPercentage <= 0) {
                return Pair(BattleResult.Sunk, Money.Zero)
            }

            view.showBattleStatus(
                MessageFormat.format(
                    Strings.CurrentSeaworthiness,
                    Status.forPercentage(statusPercentage),
                    statusPercentage
                )
            )

            // Spawns more Lorcha
            // ReSharper disable once ForeachCanBePartlyConvertedToQueryUsingAnotherGetEnumerator
            for (position in LorchaPosition.All) {
                if (numShips <= numOnScreen) continue
                if (shipsOnScreen[position] != null) continue

                Thread.sleep(100)
                val lorcha = Lorcha((state.enemyCountMultiplier * state.random.nextDouble() + 20).toInt())
                shipsOnScreen[position] = lorcha
                view.drawLorcha(position, lorcha)
                numOnScreen++
            }

            if (numShips > numOnScreen) {
                view.showMoreShipsIndicator()
            } else {
                view.hideMoreShipsIndicator()
            }

            // TODO: Any input during 3 seconds is fine. How to model?
            // orders = _view.AskForOrders();
            // SkippableDelay(3);
            view.showBattleStatus(Strings.WhatShallWeDo)
            orders = view.checkForOrders()

            fightStats(state, numShips, orders)

            when (orders) {
                Orders.Fight -> run {
                    if (state.ship.guns > 0) {
                        var shipsSunk = 0

                        ok = 3
                        ik = 1

                        view.showBattleStatus(Strings.WellFightEm)

                        skippableDelay(3)

                        view.showBattleStatus(Strings.WereFiringOnThem)

                        for (i in 1..state.ship.guns) {
                            if (shipsOnScreen.isEmpty()) {
                                for (position in LorchaPosition.All) {
                                    if (numShips > numOnScreen) {
                                        if (shipsOnScreen[position] == null) {
                                            Thread.sleep(100)
                                            val lorcha = Lorcha(
                                                (state.enemyCountMultiplier * state.random.nextDouble() + 20).toInt()
                                            )
                                            shipsOnScreen[position] = lorcha
                                            view.drawLorcha(position, lorcha)
                                            numOnScreen++
                                        }
                                    }
                                }
                            }

                            if (numShips > numOnScreen) {
                                view.showMoreShipsIndicator()
                            } else {
                                view.hideMoreShipsIndicator()
                            }

                            var targeted = LorchaPosition.All[state.random.nextInt(10)]
                            while (shipsOnScreen[targeted] == null) {
                                // Yikes, a bit of an expensive way to choose but OK
                                targeted = LorchaPosition.All[state.random.nextInt(10)]
                            }

                            view.drawBlast(targeted)
                            Thread.sleep(100)

                            view.drawLorcha(targeted, shipsOnScreen[targeted]!!)
                            Thread.sleep(100)

                            view.drawBlast(targeted)
                            Thread.sleep(100)

                            view.drawLorcha(targeted, shipsOnScreen[targeted]!!)
                            Thread.sleep(100)

                            shipsOnScreen[targeted]!!.takeDamage(state.random.nextInt(30) + 10)

                            if (shipsOnScreen[targeted]!!.isSunk) {
                                numOnScreen--
                                numShips--
                                shipsSunk++
                                shipsOnScreen.remove(targeted)

                                Thread.sleep(100)

                                view.sinkLorcha(targeted)

                                if (numShips == numOnScreen) {
                                    view.hideMoreShipsIndicator()
                                }

                                fightStats(state, numShips, orders)
                            }

                            if (numShips == 0) {
                                return@run
                            } else {
                                Thread.sleep(500)
                            }
                        }

                        if (shipsSunk > 0) {
                            view.showBattleStatus(MessageFormat.format(Strings.SunkXOfTheBuggers, shipsSunk))
                        } else {
                            view.showBattleStatus(Strings.HitEmButDidntSinkEm)
                        }

                        skippableDelay(3)

                        if (state.random.nextInt(s0) > numShips * 0.6 / battleType.value && numShips > 2) {
                            val ran = state.random.nextInt(numShips / 3 / battleType.value) + 1

                            numShips -= ran
                            fightStats(state, numShips, orders)

                            view.showBattleStatus(MessageFormat.format(Strings.XRanAway, ran))

                            if (numShips <= 10) {
                                for (position in LorchaPosition.AllBackwards) {
                                    if (numOnScreen > numShips && shipsOnScreen[position] != null) {
                                        shipsOnScreen.remove(position)
                                        numOnScreen--

                                        view.clearLorcha(position)

                                        Thread.sleep(100)
                                    }
                                }

                                if (numShips == numOnScreen) {
                                    view.hideMoreShipsIndicator()
                                }
                            }

                            skippableDelay(3)

                            orders = view.checkForOrders()
                        }
                    } else { // state.Ship.Guns == 0
                        view.showBattleStatus(Strings.WeHaveNoGuns)
                        skippableDelay(3)
                    }
                }
                Orders.ThrowCargo -> {
                    var amount = 0

                    view.showDetail(
                        MessageFormat.format(
                            Strings.YouHaveTheFollowingOnBoard,
                            state.ship.unitsStored(CargoType.Opium),
                            state.ship.unitsStored(CargoType.Silk),
                            state.ship.unitsStored(CargoType.Arms),
                            state.ship.unitsStored(CargoType.General)
                        )
                    )

                    view.showBattleStatus(Strings.WhatShallIThrowOverboard)

                    val choice = view.askUserForCargoTypeOrAll()

                    val total = if (choice != CargoToThrow.Everything) {
                        view.showBattleStatus(Strings.HowMuch)
                        amount = view.askUserForNumber()
                        val cargoType = choice.toCargoType()
                        if (state.ship.hasStored(cargoType) &&
                            (amount == -1 || amount > state.ship.unitsStored(cargoType))
                        ) {
                            amount = state.ship.unitsStored(cargoType)
                        }

                        state.ship.unitsStored(cargoType)
                    } else {
                        state.ship.totalUnitsStored
                    }

                    if (total > 0) {
                        view.showBattleStatus(Strings.LetsHopeWeLoseEm)
                        ok += if (choice != CargoToThrow.Everything) {
                            state.ship.dropCargo(choice.toCargoType(), amount)
                            amount / 10
                        } else {
                            state.ship.clearAllCargo()
                            total / 10
                        }

                        view.showDetail("")
                        skippableDelay(3)
                    } else {
                        view.showBattleStatus(Strings.TheresNothingThere)
                        view.showDetail("")
                        skippableDelay(3)
                    }
                }
                Orders.Run -> {}
            }

            if (orders == Orders.Run || orders == Orders.ThrowCargo) {
                if (orders == Orders.Run) {
                    view.showBattleStatus(Strings.WellRun)
                    skippableDelay(3)
                }

                ok += ik++
                if (state.random.nextInt(ok) > state.random.nextInt(numShips)) {
                    view.showBattleStatus(Strings.WeGotAwayFromEm)
                    skippableDelay(3)
                    numShips = 0
                } else {
                    view.showBattleStatus(Strings.CouldntLoseEm)
                    skippableDelay(3)

                    if (numShips > 2 && state.random.nextInt(5) == 0) {
                        val lost = state.random.nextInt(numShips / 2) + 1

                        numShips -= lost
                        fightStats(state, numShips, orders)
                        view.showBattleStatus(MessageFormat.format(Strings.ButWeEscapedFromXOfEm, lost))

                        if (numShips <= 10) {
                            for (position in LorchaPosition.AllBackwards) {
                                if (numOnScreen > numShips && shipsOnScreen[position] != null) {
                                    shipsOnScreen.remove(position)
                                    numOnScreen--

                                    view.clearLorcha(position)
                                    Thread.sleep(100)
                                }
                            }

                            if (numShips == numOnScreen) {
                                view.hideMoreShipsIndicator()
                            }
                        }

                        skippableDelay(3)
                        orders = view.checkForOrders()
                    }
                }
            }

            if (numShips > 0) {
                view.showBattleStatus(Strings.TheyreFiringOnUs)

                skippableDelay(3)

                view.showBeingHit()

                fightStats(state, numShips, orders)
                for (position in LorchaPosition.All) {
                    val ship = shipsOnScreen[position]
                    if (ship != null) {
                        view.drawLorcha(position, ship)
                    }
                }

                if (numShips > numOnScreen) {
                    view.showMoreShipsIndicator()
                } else {
                    view.hideMoreShipsIndicator()
                }

                view.showBattleStatus(Strings.WeveBeenHit)

                skippableDelay(3)

                var i = numShips.coerceAtMost(15)
                if (state.ship.guns > 0 && (
                    state.random.nextInt(100) < state.ship.damage.toFloat() / state.ship.capacity * 100 ||
                        state.ship.damage.toFloat() / state.ship.capacity * 100 > 80
                    )
                ) {
                    i = 1

                    state.ship.loseGun()

                    fightStats(state, numShips, orders)
                    view.showBattleStatus(Strings.TheBuggersHitAGun)
                    fightStats(state, numShips, orders)

                    skippableDelay(3)
                }

                state.ship.takeDamage(state.random.nextInt((state.enemyDamageMultiplier * i * battleType.value).toInt()) + i / 2)
                if (battleType != BattleType.LiYuen && state.random.nextInt(20) == 0) {
                    return Pair(BattleResult.EnemyChasedOffByLiYuen, booty)
                }
            }
        }

        orders = view.checkForOrders()
        return if (orders == Orders.Fight) {
            fightStats(state, numShips, orders)
            view.showBattleStatus(Strings.WeGotEmAll)
            skippableDelay(3)

            Pair(BattleResult.Victory, booty)
        } else {
            Pair(BattleResult.Retreat, booty)
        }
    }

    private fun fightStats(state: GameState, ships: Int, orders: Orders) {
        val ordersDescription = when (orders) {
            Orders.Fight -> Strings.Orders_Fight
            Orders.Run -> Strings.Orders_Run
            Orders.ThrowCargo -> Strings.Orders_ThrowCargo
        }

        view.showShipCount(ships)

        // TODO: Big .NET problem with internationalisation of plural forms. Can ICU help here?
        view.showShipsAttacking(
            if (ships == 1) {
                MessageFormat.format(Strings.ShipsAttacking_One, ships)
            } else {
                MessageFormat.format(Strings.ShipsAttacking_Other, ships)
            }
        )

        view.showCurrentOrders(MessageFormat.format(Strings.YourOrdersAreTo, ordersDescription))
        view.showShipGuns(state.ship.guns)
    }

    fun gameOver(state: GameState) {
        val years = state.calendar.yearsSinceStart
        val months = state.calendar.monthsSinceYearStart
        val time = state.calendar.monthsSinceStart

        val totalEquity = state.cash + state.cashAtBank - state.debt
        val finalScore = totalEquity.Amount / 100 / time
        val rating = Rating.forFinalCash(state.cash)

        // TODO: More pluralisation woes
        view.showFinalStatsScreen(
            state.cash.fancyNumbers(),
            state.ship.capacity,
            state.ship.guns,
            years,
            months,
            finalScore,
            rating
        )

        val choice = view.askYesNo()
        if (choice == YesNo.Yes) {
            main()
        }
    }

    fun skippableDelay(seconds: Int) {
        view.waitSecondsUnlessAKeyIsPressed(seconds)
    }
}
