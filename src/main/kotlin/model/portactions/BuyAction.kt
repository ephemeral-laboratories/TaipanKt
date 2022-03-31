package garden.ephemeral.games.taipan.model.portactions

import garden.ephemeral.games.taipan.Strings
import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View
import java.text.MessageFormat

class BuyAction : PortAction {
    override fun run(state: GameState, view: View) {
        view.showDetail(Strings.WhatDoYouWishMeToBuy)

        val type = view.askUserForCargoType()
        val price = state.prices[type]!!

        var quantity: Int
        while (true) {
            val affordable = (state.cash / price).toInt()
            view.showDetail(
                MessageFormat.format(Strings.YouCanAffordX, affordable),
                MessageFormat.format(Strings.HowMuchXShallIBuy, type.localizedName)
            )
            quantity = view.askUserForNumber()
            if (quantity == -1) {
                quantity = affordable
            }

            if (quantity <= affordable) {
                break
            }
        }

        state.cash -= price * quantity
        state.ship.addCargo(type, quantity)
    }
}
