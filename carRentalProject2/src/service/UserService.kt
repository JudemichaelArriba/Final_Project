package service


import model.Cars
import model.Users
import java.io.File

class UserService(
    private val userAccounts: MutableList<Users>,
    private val carService: CarService,
    private val carList: MutableList<Cars>,
    private val availableCarList: MutableList<Cars>,
    private val rentalHistory: HashMap<String, MutableList<Cars>>,
    private val notAvailableCarList: MutableList<Cars>
) {

    private var choice: Int? = null


    fun userSignup() {
        println("Enter a Username: ")
        val username = readlnOrNull()?.trim()
        if (username.isNullOrEmpty()) {
            println("enter a valid name")
            return userSignup()
        }
        println("Enter a Password: ")
        val password = readlnOrNull()?.trim()
        if (password.isNullOrEmpty()) {
            println("enter a valid name")
            return userSignup()
        }
        val itContains = userAccounts.find { it.getUsername() == username }
        val itContains1 = userAccounts.find { it.getPassword() == password }
        if (itContains == null && itContains1 == null) {
            val user = Users(username, password)
            userAccounts.add(user)
            saveUsersToFile()
            println("Successfully Signed up")
        } else {
            println("Username or Password is already taken")
        }
    }


    fun userChoice(username: String) {
        rentalHistory.putIfAbsent(username, mutableListOf())
        while (true) {
            println(
                """
                1. Show Available Cars
                2. Rent a Car
                3. Show rented Car
                4. Return a Car
                5. Logout
                """.trimIndent()
            )
            val enter = readlnOrNull()
            choice = enter?.toIntOrNull()

            if (choice == null || choice !in 1..5) {
                println("Invalid input! Please enter a number between 1 and 5.")
                continue
            } else {

                when (choice) {
                    1 -> {
                        carService.showAvailableCars(availableCarList)

                    }

                    2 -> {
                        carService.rentCar(carList, availableCarList, notAvailableCarList, rentalHistory, username)

                    }

                    3 -> {
                        showRentalHistory(username)

                    }

                    4 -> {

                        carService.returnCar(carList, notAvailableCarList, rentalHistory, username, availableCarList)
                    }

                    5 -> {
                        break
                    }

                    else -> {
                        println("Invalid input! Please enter a number between 1 and 4.")
                        println()
                    }
                }
            }

        }

    }

    private fun showRentalHistory(username: String) {
        val history = rentalHistory[username]

        if (history.isNullOrEmpty()) {
            println("No rental history found.")
            println()
        } else {
            println("Rental History for $username:")


            history.forEach { car ->
                println("Car: ${car.getCarName()}, Brand: ${car.getCarBrand()}, ID: ${car.getCarId()}")
            }

            println()
        }
    }


    fun showUser() {

        if (userAccounts.isNotEmpty()) {
            println()
            for (userAccounts in userAccounts) {
                println()
                println("Username: ${userAccounts.getUsername()}")
                println("Rent history: ")

                val history = rentalHistory[userAccounts.getUsername()]

                if (history.isNullOrEmpty()) {
                    println("No rental history found.")
                } else {

                    history.forEach { car ->
                        println("Car: ${car.getCarName()}, Brand: ${car.getCarBrand()}, ID: ${car.getCarId()}")

                    }
                }
            }
            println()
        } else {
            println("There are no Users")
            println()
        }
    }


    private fun saveUsersToFile(filename: String = "users.txt") {
        val file = File(filename)
        file.printWriter().use { writer ->
            userAccounts.forEach { user ->
                writer.println("${user.getUsername()},${user.getPassword()}")
            }
        }
    }


    fun loadUsersFromFile(filename: String = "users.txt"): MutableList<Users> {
        val file = File(filename)
        val loadedUsers = mutableListOf<Users>()

        if (!file.exists()) return loadedUsers

        file.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                val username = parts[0]
                val password = parts[1]
                loadedUsers.add(Users(username, password))
            }
        }
        return loadedUsers
    }


    fun loadRentalHistoryFromFile(
        rentalHistory: HashMap<String, MutableList<Cars>>,
        carList: MutableList<Cars>,
        filename: String = "rental_history.txt"
    ) {
        val file = File(filename)
        if (!file.exists()) return

        file.forEachLine { line ->
            val parts = line.split(":")
            if (parts.size == 2) {
                val username = parts[0]
                val carIds = parts[1].split("|").mapNotNull { it.toIntOrNull() }


                val rentedCars = carIds.mapNotNull { carId -> carList.find { it.getCarId() == carId } }
                rentalHistory[username] = rentedCars.toMutableList()
            }
        }
    }


}