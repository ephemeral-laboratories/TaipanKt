package garden.ephemeral.games.taipan.model

// An enemy pirate ship
// https://en.wikipedia.org/wiki/Lorcha_(boat)
class Lorcha(private var hitPoints: Int) {
    val isSunk = hitPoints <= 0

    fun takeDamage(damage: Int) {
        hitPoints -= damage
    }
}
