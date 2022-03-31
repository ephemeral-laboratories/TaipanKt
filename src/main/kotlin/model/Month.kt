package garden.ephemeral.games.taipan.model

import garden.ephemeral.games.taipan.Strings

enum class Month(val localizedName: String) {
    Jan(Strings.Months_1),
    Feb(Strings.Months_2),
    Mar(Strings.Months_3),
    Apr(Strings.Months_4),
    May(Strings.Months_5),
    Jun(Strings.Months_6),
    Jul(Strings.Months_7),
    Aug(Strings.Months_8),
    Sep(Strings.Months_9),
    Oct(Strings.Months_10),
    Nov(Strings.Months_11),
    Dec(Strings.Months_12),
    ;

    fun next() = if (this == Dec) {
        Jan
    } else {
        values()[ordinal + 1]
    }
}
