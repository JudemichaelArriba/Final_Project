package service

import model.Cars
import model.Users


class AdminService(
    private val carService: CarService, private val carList: MutableList<Cars>,
    private val availableCarList: MutableList<Cars>, private val notAvailableCarList: MutableList<Cars>,
    private val userService: UserService, private val rentalHistory: HashMap<String, MutableList<Cars>>,
    private val userAccounts: MutableList<Users>
) {

    private var choice: Int? = null


    fun adminChoice() {

        while (true) {
            println(
                """
                    1. Add Car
                    2. Delete Car
                    3. Show all Cars
                    4. Show all Available Cars
                    5. Show all Not Available Cars
                    6. Show Users
                    7. Clear all cars
                    8. Return Car
                    9. Log out
                """.trimIndent()
            )
            println("Enter a number: ")
            val enter = readlnOrNull()
            choice = enter?.toIntOrNull()
            if (choice == null || choice !in 1..9) {
                println("Invalid input! Please enter a number between 1 and 8.")
                println()
                continue
            } else {

                when (choice) {
                    1 -> {
                        carService.addCar(carList, availableCarList)
                    }

                    2 -> {
                        carService.deleteCar(carList, availableCarList)

                    }

                    3 -> {
                        carService.showAllCars(carList)

                    }

                    4 -> {

                        carService.showAvailableCars(availableCarList)
                    }

                    5 -> {

                        carService.showNotAvailableCar(notAvailableCarList)
                    }

                    6 -> {

                        userService.showUser()
                    }

                    7 -> {
                        carService.clear(availableCarList, carList)

                    }

                    8 -> {

                        carService.returnAdmin(
                            carList,
                            notAvailableCarList,
                            rentalHistory,
                            availableCarList,
                            userAccounts
                        )

                    }

                    9 -> {

                        break
                    }

                    else -> {
                        println("Invalid input! Please enter a number between 1 and 8.")
                        println()
                    }
                }


            }
        }
    }


}