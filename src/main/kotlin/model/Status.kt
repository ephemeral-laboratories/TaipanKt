package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class Status(val localizedName: String) {
    Critical(Strings.Status_Critical),
    Poor(Strings.Status_Poor),
    Fair(Strings.Status_Fair),
    Good(Strings.Status_Good),
    Prime(Strings.Status_Prime),
    Perfect(Strings.Status_Perfect),
    ;

    companion object {
        fun forPercentage(percentage: Int): Status {
            return values()[percentage / 20]
        }
    }
}
