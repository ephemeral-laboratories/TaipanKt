package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class Rating(val localizedName: String) {
    MaTsu(Strings.Rating_MaTsu),
    MasterTaipan(Strings.Rating_MasterTaipan),
    Taipan(Strings.Rating_Taipan),
    Compradore(Strings.Rating_Compradore),
    GalleyHand(Strings.Rating_GalleyHand),
    Worse(""),
    Worst(""),
    ;

    companion object {
        fun forFinalCash(amount: Money): Rating {
            return when {
                amount >= 50000 -> MaTsu
                amount >= 8000 -> MasterTaipan
                amount >= 1000 -> Taipan
                amount >= 500 -> Compradore
                amount >= 100 -> GalleyHand
                amount >= 0 -> Worse
                else -> Worst
            }
        }
    }
}
