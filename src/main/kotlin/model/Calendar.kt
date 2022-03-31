package garden.ephemeral.games.taipan.model

class Calendar(year: Int, month: Month) {
    var year = year
        private set
    var month = month
        private set

    val yearsSinceStart = this.year - EpochYear
    val monthsSinceYearStart = this.month.ordinal
    val monthsSinceStart = yearsSinceStart * 12 + monthsSinceYearStart

    fun advance() {
        month = month.next()

        if (month != Month.Jan) return

        year++
    }

    companion object {
        private const val EpochYear = 1860

        fun atStart(): Calendar {
            return Calendar(EpochYear, Month.Jan)
        }
    }
}
