package garden.ephemeral.games.taipan.model.portactions

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.CargoType
import garden.ephemeral.games.taipan.model.GameLogic
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class TransferCargoAction : PortAction {
    override fun run(state: GameState, view: View) {
        if (state.hongKongWarehouse.isEmpty && state.ship.isEmpty) {
            view.showFeedback(Strings.YouHaveNoCargo)
            GameLogic.skippableDelay(5)
            return
        }

        for (type in CargoType.values()) {
            if (state.ship.hasStored(type)) {
                while (true) {
                    view.showTitle(Strings.CompradorsReport)
                    view.showDetail(MessageFormat.format(Strings.HowMuchToMove, type.localizedName))

                    var amount = view.askUserForNumber()
                    if (amount == -1) {
                        amount = state.ship.unitsStored(type)
                    }

                    if (amount <= state.ship.unitsStored(type)) {
                        val inUse = state.hongKongWarehouse.totalUnitsStored
                        if (inUse + amount <= 10000) {
                            state.ship.removeCargo(type, amount)
                            state.hongKongWarehouse.addCargo(type, amount)
                            break
                        }

                        if (inUse == 10000) {
                            view.showFeedback(Strings.WarehouseIsFull)
                        } else {
                            view.showFeedback(MessageFormat.format(Strings.WarehouseWillOnlyHoldX, 10000 - inUse))
                            GameLogic.skippableDelay(5)
                        }
                    } else {
                        view.showDetail(MessageFormat.format(Strings.YouHaveOnlyX, state.ship.unitsStored(type)))
                        GameLogic.skippableDelay(5)
                    }
                }

                GameLogic.updatePortStatistics(state)
            }

            if (state.hongKongWarehouse.hasStored(type)) {
                while (true) {
                    view.showTitle(Strings.CompradorsReport)
                    view.showDetail(MessageFormat.format(Strings.HowMuchToMoveAboard, type.localizedName))

                    var amount = view.askUserForNumber()
                    if (amount == -1) {
                        amount = state.hongKongWarehouse.unitsStored(type)
                    }

                    if (amount <= state.hongKongWarehouse.unitsStored(type)) {
                        state.hongKongWarehouse.removeCargo(type, amount)
                        state.ship.addCargo(type, amount)
                        break
                    }

                    view.showDetail(
                        MessageFormat.format(
                            Strings.YouHaveOnlyX,
                            state.hongKongWarehouse.unitsStored(type)
                        )
                    )
                    GameLogic.skippableDelay(5)
                }

                GameLogic.updatePortStatistics(state)
            }
        }
    }
}
