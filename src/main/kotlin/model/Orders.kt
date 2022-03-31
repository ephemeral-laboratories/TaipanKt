package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class Orders(val localizedName: String) {
    Fight(Strings.Orders_Fight),
    Run(Strings.Orders_Run),
    ThrowCargo(Strings.Orders_ThrowCargo),
    ;
}
