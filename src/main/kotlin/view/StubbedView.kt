package garden.ephemeral.games.taipan.view

import garden.ephemeral.games.taipan.model.*

class StubbedView : View {
    override fun showTitle(heading: String) {
        throw NotImplementedError()
    }

    override fun showDetail(vararg detailRows: String) {
        throw NotImplementedError()
    }

    override fun showSplashUntilKeyPressed() {
        throw NotImplementedError()
    }

    override fun sinkLorcha(position: LorchaPosition) {
        throw NotImplementedError()
    }

    override fun pollForInputKeystroke(): Int {
        throw NotImplementedError()
    }

    override fun askUserForNumber(): Int {
        throw NotImplementedError()
    }

    override fun askYesNo(): YesNo {
        throw NotImplementedError()
    }

    override fun selectInitialState(question: String, a: InitialState, b: InitialState): InitialState {
        throw NotImplementedError()
    }

    override fun clearScreen() {
        throw NotImplementedError()
    }

    override fun showPortStatsScreen() {
        throw NotImplementedError()
    }

    override fun drawBlast(position: LorchaPosition) {
        throw NotImplementedError()
    }

    override fun drawLorcha(position: LorchaPosition, ship: Lorcha) {
        throw NotImplementedError()
    }

    override fun clearLorcha(position: LorchaPosition) {
        throw NotImplementedError()
    }

    override fun askUserForCargoType(): CargoType {
        throw NotImplementedError()
    }

    override fun askUserForCargoTypeOrAll(): CargoToThrow {
        throw NotImplementedError()
    }

    override fun showPrice(cargoType: CargoType, price: Money) {
        throw NotImplementedError()
    }

    override fun waitSecondsUnlessAKeyIsPressed(seconds: Int) {
        throw NotImplementedError()
    }

    override fun checkForOrders(): Orders {
        throw NotImplementedError()
    }

    override fun showShipCount(ships: Int) {
        throw NotImplementedError()
    }

    override fun showShipsAttacking(shipsAttackingOne: String) {
        throw NotImplementedError()
    }

    override fun askUserForPort(): Port {
        throw NotImplementedError()
    }

    override fun askUserForPortActivity(question: String): PortActivity {
        throw NotImplementedError()
    }

    override fun askUserForHomePortActivity(question: String): PortActivity {
        throw NotImplementedError()
    }

    override fun askUserForHomePortActivityIncludingRetirement(question: String): PortActivity {
        throw NotImplementedError()
    }

    override fun showShipStatus(status: Status, statusPercentage: Int) {
        throw NotImplementedError()
    }

    override fun showMoreShipsIndicator() {
        throw NotImplementedError()
    }

    override fun hideMoreShipsIndicator() {
        throw NotImplementedError()
    }

    override fun showPort(port: Port) {
        throw NotImplementedError()
    }

    override fun showWarehouseCargoQuantity(type: CargoType, quantity: Int) {
        throw NotImplementedError()
    }

    override fun showShipCargoQuantity(type: CargoType, quantity: Int) {
        throw NotImplementedError()
    }

    override fun showShipAvailable(available: Int) {
        throw NotImplementedError()
    }

    override fun showShipOverloaded() {
        throw NotImplementedError()
    }

    override fun showWarehouseAvailable(available: Int) {
        throw NotImplementedError()
    }

    override fun showWarehouseInUse(inUse: Int) {
        throw NotImplementedError()
    }

    override fun showCash(cash: String) {
        throw NotImplementedError()
    }

    override fun showShipGuns(shipGuns: Int) {
        throw NotImplementedError()
    }

    override fun showCashAtBank(cash: String) {
        throw NotImplementedError()
    }

    override fun showDebt(cash: String) {
        throw NotImplementedError()
    }

    override fun showRetirement() {
        throw NotImplementedError()
    }

    override fun showBeingHit() {
        throw NotImplementedError()
    }

    override fun askUserForMoneyAmount(): Money {
        throw NotImplementedError()
    }

    override fun showFinalStatsScreen(
        finalEquity: String,
        finalShipCapacity: Int,
        finalShipGuns: Int,
        years: Int,
        months: Int,
        finalScore: Long,
        rating: Rating,
    ) {
        throw NotImplementedError()
    }

    override fun askForFirmName(vararg questionLines: String): String {
        throw NotImplementedError()
    }

    override fun showFeedback(detail: String) {
        throw NotImplementedError()
    }

    override fun showBattleStatus(detail: String) {
        throw NotImplementedError()
    }

    override fun showCurrentOrders(message: String) {
        throw NotImplementedError()
    }
}
