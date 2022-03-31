package garden.ephemeral.games.taipan.model

// The ship's hold can be used for storing cargo. It also holds guns.
// The more guns you have, the less space you have for cargo.
class Ship(capacity: Int) : CargoStorage(capacity) {
    var damage = 0
        private set

    var guns = 0
        private set

    var port = Port.HongKong

    override val available = super.available - guns * 10

    val isOverloaded = available < 0

    fun addGun() {
        addGuns(1)
    }

    fun addGuns(number: Int) {
        guns += number
    }

    fun loseGun() {
        guns -= 1
    }

    fun expandHoldBy(extra: Int) {
        capacity += extra
    }

    fun dropCargo(choice: CargoType, amount: Int) {
        removeCargo(choice, amount)
    }

    fun takeDamage(amount: Int) {
        damage += amount
    }

    fun repair(amount: Int) {
        damage = (damage - amount).coerceAtLeast(0)
    }

    fun resetDamage() {
        damage = 0
    }
}
