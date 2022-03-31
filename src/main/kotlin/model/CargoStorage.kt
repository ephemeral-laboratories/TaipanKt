package garden.ephemeral.games.taipan.model

open class CargoStorage(var capacity: Int) {
    private var _storage = mutableMapOf<CargoType, Int>()

    val totalUnitsStored get() = _storage.values.sum()

    open val available get() = capacity - totalUnitsStored

    val isEmpty get() = _storage.values.all { v -> v == 0 }
    val isNotEmpty get() = !isEmpty

    fun hasStored(type: CargoType): Boolean = unitsStored(type) > 0

    fun unitsStored(type: CargoType): Int = _storage[type] ?: 0

    fun addCargo(type: CargoType, amount: Int) {
        _storage[type] = unitsStored(type) + amount
    }

    fun removeCargo(type: CargoType, quantity: Int) {
        if (unitsStored(type) < quantity) {
            throw IllegalArgumentException("Assumes you checked before")
        }

        _storage[type] = unitsStored(type) - quantity
    }

    fun clearAllCargo() {
        _storage.clear()
    }
}
