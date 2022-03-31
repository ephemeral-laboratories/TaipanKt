package garden.ephemeral.games.taipan.model

import java.text.MessageFormat

data class Money(val Amount: Long) : Comparable<Money> {
    override fun toString(): String {
        // TODO: Radix
        return Amount.toString()
    }

    operator fun plus(b: Money): Money = Money(Amount + b.Amount)
    operator fun plus(b: Long): Money = Money(Amount + b)
    operator fun minus(b: Money): Money = Money(Amount - b.Amount)
    operator fun minus(b: Long): Money = Money(Amount - b)
    operator fun times(b: Double): Money = Money((Amount * b).toLong())
    operator fun times(b: Int): Money = Money(Amount * b)
    operator fun div(b: Double): Money = Money((Amount / b).toLong())
    operator fun div(b: Int): Money = Money(Amount / b)
    operator fun div(b: Money): Long = Amount / b.Amount

    override operator fun compareTo(other: Money): Int = Amount.compareTo(other.Amount)

    operator fun compareTo(other: Long): Int = Amount.compareTo(other)

    fun fancyNumbers(): String {
        val builder = StringBuilder(18)

        // TODO: Format strings are probably wrong below
        // TODO: Move format strings to Strings

        if (Amount >= 100_000_000) {
            val num1 = Amount / 1_000_000
            builder.append(MessageFormat.format("{0,12:N0} Million", num1))
        } else if (Amount >= 10_000_000) {
            val num1 = Amount / 1_000_000
            val num2 = (Amount % 1_000_000) / 100_000
            builder.append(MessageFormat.format("{0,12:N0}", num1))
            if (num2 > 0) {
                builder.append(".")
                builder.append(MessageFormat.format("{0,12:N0}", num2))
            }

            builder.append(" Million")
        } else if (Amount >= 1_000_000) {
            val num1 = Amount / 1_000_000
            val num2 = (Amount % 1_000_000) / 10_000
            builder.append(MessageFormat.format("{0,12:N0}", num1))
            if (num2 > 0) {
                builder.append(".")
                builder.append(MessageFormat.format("{0,12:N0}", num2))
            }

            builder.append(" Million")
        } else {
            builder.append(MessageFormat.format("{0,12:N0}", Amount))
        }

        return builder.toString()
    }

    companion object {
        val Zero = Money(0)
    }
}
