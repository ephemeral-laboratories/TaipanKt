package garden.ephemeral.games.taipan.view

import garden.ephemeral.games.taipan.model.*

interface View {
    fun clearScreen()

    // Show splash screen, picture of a ship, press any key to start, you get the idea.
    // Kept information:
    // Created by: Art Canfil
    // TRS80 version by: Art Canfil
    // Programmed by: Jay Link jaylink1971@gmail.com
    fun showSplashUntilKeyPressed()

    // Port stats layout:
    // Firm: %s, Hong Kong\n", firm
    //  ______________________________________
    // |Hong Kong Warehouse                   |     Date
    // |   Opium     [   ] In Use:            |
    // |   Silk      [   ]                    |
    // |   Arms      [   ] Vacant:            |   Location
    // |   General   [   ]                    |
    // |______________________________________|
    // |Hold               Guns               |     Debt
    // |   Opium     [   ]                    |
    // |   Silk      [   ]                    |
    // |   Arms      [   ]                    |  Ship Status
    // |   General   [   ]                    |
    // |______________________________________|
    // Cash:               Bank:
    // ________________________________________
    fun showPortStatsScreen()

    fun askForFirmName(vararg questionLines: String): String

    fun showTitle(heading: String)

    fun showDetail(vararg detailRows: String)

    fun showFeedback(detail: String)

    fun showBattleStatus(detail: String)

    fun drawBlast(position: LorchaPosition)

    fun drawLorcha(position: LorchaPosition, ship: Lorcha)

    fun clearLorcha(position: LorchaPosition)

    // Trigger animation sinking the Lorcha.
    // In the original, 1/20 of the time, the animation runs 2x slower
    fun sinkLorcha(position: LorchaPosition)

    fun pollForInputKeystroke(): Int

    fun askUserForNumber(): Int

    fun askUserForCargoType(): CargoType

    fun askUserForCargoTypeOrAll(): CargoToThrow

    fun askYesNo(): YesNo

    fun selectInitialState(question: String, a: InitialState, b: InitialState): InitialState

    fun showPrice(cargoType: CargoType, price: Money)

    fun waitSecondsUnlessAKeyIsPressed(seconds: Int)

    fun checkForOrders(): Orders

    fun showShipCount(ships: Int)

    fun showShipsAttacking(shipsAttackingOne: String)

    fun askUserForPort(): Port

    fun askUserForPortActivity(question: String): PortActivity

    fun askUserForHomePortActivity(question: String): PortActivity

    fun askUserForHomePortActivityIncludingRetirement(question: String): PortActivity

    // Renders the status string in inverse if status is critical or poor
    fun showShipStatus(status: Status, statusPercentage: Int)

    fun showMoreShipsIndicator()

    fun hideMoreShipsIndicator()

    fun showPort(port: Port)

    fun showWarehouseCargoQuantity(type: CargoType, quantity: Int)

    fun showShipCargoQuantity(type: CargoType, quantity: Int)

    fun showShipAvailable(available: Int)

    fun showShipOverloaded()

    fun showWarehouseAvailable(available: Int)

    fun showWarehouseInUse(inUse: Int)

    fun showCash(cash: String)

    fun showCurrentOrders(message: String)

    fun showShipGuns(shipGuns: Int)

    fun showCashAtBank(cash: String)

    fun showDebt(cash: String)

    // In the original, displays in inverse,
    //
    // Y o u ' r e    a
    //
    // M I L L I O N A I R E !
    //
    fun showRetirement()

    // Classically, the screen flashes
    fun showBeingHit()

    fun askUserForMoneyAmount(): Money

    // Your final status:
    // Net cash:  %s
    // Ship size: %d units with %d guns
    // You traded for %d year(s) and %d month(s)
    // Your score is %d.
    //
    // If Worse rating: "Have you considered a land based job?"
    // If Worst rating: "The crew has requested that you stay on shore for their safety!!"
    // Otherwise, you get your position in the rating table highlighted:
    //   Your Rating:
    //    _______________________________
    //   |Ma Tsu         50,000 and over |
    //   |Master Taipan   8,000 to 49,999|
    //   |Taipan          1,000 to  7,999|
    //   |Compradore        500 to    999|
    //   |Galley Hand       less than 500|
    //   |_______________________________|
    //
    //   Play again?
    fun showFinalStatsScreen(
        finalEquity: String,
        finalShipCapacity: Int,
        finalShipGuns: Int,
        years: Int,
        months: Int,
        finalScore: Long,
        rating: Rating
    )
}
