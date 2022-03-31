package garden.ephemeral.games.taipan.model.portactions

import garden.ephemeral.games.taipan.model.GameState
import garden.ephemeral.games.taipan.view.View

class SellAction : PortAction {
    override fun run(state: GameState, view: View) {
        // TODO: Move to strings
        view.showDetail("What do you wish me to sell, Taipan? ")

        val type = view.askUserForCargoType()

        var quantity: Int
        while (true) {
            view.showDetail("How much %s shall I sell, Taipan: ", type.localizedName)
            quantity = view.askUserForNumber()
            if (quantity == -1) {
                quantity = state.ship.unitsStored(type)
            }

            if (state.ship.unitsStored(type) < quantity) {
                continue
            }

            state.ship.removeCargo(type, quantity)
            break
        }

        state.cash += state.prices[type]!! * quantity
    }
}
