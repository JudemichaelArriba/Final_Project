import model.Cars
import model.Users

import service.AuthService
import service.CarService
import service.UserService
import kotlin.system.exitProcess


fun main() {
    val carService = CarService()
    val rentalHistory = HashMap<String, MutableList<Cars>>()
    val carList = mutableListOf<Cars>()

    val availableCarList = mutableListOf<Cars>()
    val notAvailableCarList = mutableListOf<Cars>()
    val userAccounts = mutableListOf<Users>()
//    val rentedInfo = mutableListOf<Users>()
    val userService = UserService(
        userAccounts, carService, carList, availableCarList, rentalHistory, notAvailableCarList
    )
    var choice: Int?
    var choice2: Int?
    val authService = AuthService(
        carService, carList, availableCarList, userAccounts, rentalHistory, notAvailableCarList, userService
    )




    carList.addAll(carService.loadAllCarsFromFile())
    availableCarList.addAll(carService.loadAvailableCarsFromFile())
    notAvailableCarList.addAll(carService.loadNotAvailableCarsFromFile())
    userAccounts.addAll(userService.loadUsersFromFile())
    userService.loadRentalHistoryFromFile(rentalHistory, carList)
    println("Welcome to the CarRental Hub!")
    while (true) {

        println(
            """
                    1. Admin
                    2. User
                    3. Exit
                """.trimIndent()
        )
        println("Enter a Number: ")
        val enter = readlnOrNull()

        choice = enter?.toIntOrNull()

        if (choice != null) {


            choice.let {


                when (it) {
                    1 -> {

                        authService.adminLogin()

                    }

                    2 -> {
                        println(
                            """
                    1. Login
                    2. signup
                    3. Return
                """.trimIndent()
                        )
                        println("Enter a Number: ")
                        val enter1 = readlnOrNull()
                        choice2 = enter1?.toIntOrNull()
                        if (choice2 != null) {
                            when (choice2) {
                                1 -> {
                                    authService.userLogin(userAccounts)
                                }

                                2 -> {
                                    userService.userSignup()
                                }

                                3 -> {
                                    return@let

                                }

                                else -> {
                                    println("Invalid input! Please enter a number between 1 and 3.")
                                    println()
                                }


                            }
                        } else {
                            println("Invalid input! Please enter a valid number")
                            println()
                        }
                    }

                    3 -> {
                        exitProcess(0)
                    }


                    else -> {
                        println("Invalid input! Please enter a valid number")
                        println()
                    }
                }


            }
        } else {
            println("Invalid input! Please enter a valid number")
            println()
        }
    }

}