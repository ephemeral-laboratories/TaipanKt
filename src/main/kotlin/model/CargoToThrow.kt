package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class CargoToThrow(val localizedName: String) {
    Opium(Strings.Item_Opium),
    Silk(Strings.Item_Silk),
    Arms(Strings.Item_Arms),
    General(Strings.Item_General),
    Everything(Strings.Item_Everything),
    ;

    fun toCargoType(): CargoType {
        if (this == Everything) {
            throw IllegalStateException("Cannot convert 'Everything' to a CargoType")
        }
        return CargoType.values()[ordinal]
    }
}
