package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class PortActivity(val localizedName: String) {
    Buy(Strings.PortActivity_Buy),
    Sell(Strings.PortActivity_Sell),
    VisitBank(Strings.PortActivity_VisitBank),
    TransferCargo(Strings.PortActivity_TransferCargo),
    QuitTrading(Strings.PortActivity_QuitTrading),
    Retire(Strings.PortActivity_Retire),
    ;
}
