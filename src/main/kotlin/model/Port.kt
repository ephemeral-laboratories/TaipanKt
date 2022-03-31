package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class Port(val localizedName: String) {
    // Special value for when not at a port of course
    AtSea(Strings.Location_AtSea),

    HongKong(Strings.Location_HongKong),
    ShangHai(Strings.Location_Shanghai),
    Nagasaki(Strings.Location_Nagasaki),
    Saigon(Strings.Location_Saigon),
    Manila(Strings.Location_Manila),
    Singapore(Strings.Location_Singapore),
    Batavia(Strings.Location_Batavia),
    ;
}
