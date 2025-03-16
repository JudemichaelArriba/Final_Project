package service

import model.Cars
import model.Users
import java.io.File


import kotlin.random.Random

class CarService {


    private fun generateRandomIdNumber(): String {
        val rand = Random
        return (1..6)
            .map { rand.nextInt(0, 10) }
            .joinToString("")
    }


    fun addCar(carList: MutableList<Cars>, availableCar: MutableList<Cars>) {
        println("Enter a Car Name: ")
        val carName = readlnOrNull()?.trim()

        val locateCar = carList.find { it.getCarName() == carName }
        if (locateCar != null) {
            println("Car already exist!")
            return addCar(carList, availableCar)
        }
        if (carName.isNullOrEmpty()) {
            println("Please enter a valid car name!")
            println()
            return addCar(carList, availableCar)
        }

        println("Enter a the Car Brand: ")
        val carBrand = readlnOrNull()?.trim()
        if (carBrand.isNullOrEmpty()) {
            println("Please enter a valid car name!")
            println()
            return addCar(carList, availableCar)
        }

        val carId = generateRandomIdNumber().toInt()
        val cars = Cars(carName, carBrand, carId)
        carList.add(cars)
        availableCar.add(cars)
        println("The car $carName has been added")

        textFileCarList(carList)
        textFileAvailCar(availableCar)
    }


    fun showAllCars(carList: MutableList<Cars>) {
        if (carList.isNotEmpty()) {
            println("All Cars: ")

            for (cars in carList) {
                println("Car Name : ${cars.getCarName()}")
                println("Car Brand: ${cars.getCarBrand()}")
                println("Car Id   : ${cars.getCarId()}")
                println()
            }
        } else {
            println("There are no Cars")
            println()
        }
    }


    fun deleteCar(carList: MutableList<Cars>, availableCarList: MutableList<Cars>) {
        if (carList.isNotEmpty()) {
            println("Enter the Car Came: ")
            val carDelete = readlnOrNull()?.trim()
            if (carDelete.isNullOrEmpty()) {
                println("Please enter a valid car name!")
                println()
                return deleteCar(carList, availableCarList)
            }
            val carRemove = carList.find { it.getCarName() == carDelete }


            if (carRemove != null) {


                println(
                    """
                Are you sure you want to delete?
                1. Yes
                2. No
                Enter choice: 
            """.trimIndent()
                )
                val choice = readln().toIntOrNull()
                if (choice == null) {
                    println("Please enter a valid choice")
                    println()
                    return deleteCar(carList, availableCarList)
                }
                when (choice) {
                    1 -> {
                        carList.remove(carRemove)
                        availableCarList.remove(carRemove)
                        textFileCarList(carList)
                        textFileAvailCar(availableCarList)


                        println("The car $carDelete with has been remove")
                    }

                    2 -> {
                        println("Cancelled")
                    }

                    else -> {
                        println("Invalid choice, Please enter a valid choice")
                        return deleteCar(carList, availableCarList)
                    }


                }
            } else {
                println("THe car $carDelete are not on the list")
                println()
            }

        } else {
            println("There are no Cars")
            println()
        }

    }

    fun showAvailableCars(availableCar: MutableList<Cars>) {
        if (availableCar.isNotEmpty()) {
            println("Available Cars: ")
            for (cars in availableCar) {
                println("Car Name: ${cars.getCarName()}")
                println("Car Brand: ${cars.getCarBrand()}")
                println("Car id: ${cars.getCarId()}")
                println()
            }
        } else {
            println("There are no available car")
            println()
        }
    }

    fun rentCar(
        carList: MutableList<Cars>,
        availableCarList: MutableList<Cars>,
        notAvailableCarList: MutableList<Cars>,
        rentalHistory: HashMap<String, MutableList<Cars>>,
        username: String
    ) {
        if (carList.isNotEmpty()) {
            println("Enter the name of the car: ")
            val carName = readlnOrNull()?.trim()
            if (carName.isNullOrEmpty()) {
                println("Please enter a valid car name!")
                println()
                return rentCar(carList, availableCarList, notAvailableCarList, rentalHistory, username)
            }

            val locateCar = availableCarList.find { it.getCarName() == carName }
            val payment = durationAndPayment()
            if (locateCar != null) {

                if (payment) {
                    notAvailableCarList.add(locateCar)
                    rentalHistory[username]?.add(locateCar)
                    availableCarList.remove(locateCar)
                    textFileAvailCar(availableCarList)
                    textFileNotAvail(notAvailableCarList)
                    rentalHistory.putIfAbsent(username, mutableListOf())
                    saveRentalHistoryToFile(rentalHistory)
                    println("The $carName Successfully rented")
                    println()
                } else {
                    println("Rental process cancelled due to payment failure or cancelled.")
                    println()
                }


            } else {
                println("The $carName is not available or not on the car list")
            }


        } else {
            println("The list is empty")
        }

    }


    fun showNotAvailableCar(notAvailableCarList: MutableList<Cars>) {

        if (notAvailableCarList.isNotEmpty()) {
            println("Not Available Cars: ")
            for (cars in notAvailableCarList) {
                println("Car Name: ${cars.getCarName()}")
                println("Car Brand: ${cars.getCarBrand()}")
                println("Car id: ${cars.getCarId()}")
                println()
            }
        } else {
            println("There are no car")
            println()
        }


    }


    private fun durationAndPayment(): Boolean {
        println(
            """
        Choose Duration:
        
        1.  1 Day    --> ₱1,500
        2.  2 Days   --> ₱3,000
        3.  3 Days   --> ₱4,500
        4.  4 Days   --> ₱6,000
        5.  5 Days   --> ₱7,500
        6.  6 Days   --> ₱9,000
        7.  1 Week   --> ₱9,000
        8.  2 Weeks  --> ₱18,000
        9.  3 Weeks  --> ₱27,000
        10. 4 Weeks  --> ₱36,000
        
        Enter Selection: 
    """.trimIndent()
        )

        val choice = readln().toIntOrNull()
        if (choice == null || choice !in 1..13) {
            println("Invalid choice. Please select a number from 1 to 13.")
            println()
            return durationAndPayment()
        }

        val price: Map<Int, Double> = mapOf(
            1 to 1500.0, 2 to 3000.0, 3 to 4500.0, 4 to 6000.0, 5 to 7500.0, 6 to 9000.0,
            7 to 9000.0, 8 to 13500.0, 9 to 18000.0, 10 to 22500.0, 11 to 27000.0,
            12 to 31500.0, 13 to 36000.0
        )

        val totalPrice = price[choice]!!
        println("Total cost: ₱$totalPrice")

        println(
            """
        Proceed with payment?
        1. Yes
        2. No/Cancel
    """.trimIndent()
        )

        val choice2 = readln().toIntOrNull()
        if (choice2 == null || choice2 !in 1..2) {
            println("Invalid choice. Please select 1 or 2.")
            println()
            return durationAndPayment()
        }

        if (choice2 == 2) {
            println("Transaction cancelled.")
            println()
            return false
        }


        var payment: Double?

        while (true) {
            print("Enter amount: ")
            payment = readln().toDoubleOrNull()

            if (payment == null) {
                println("Invalid input. Please enter a valid number.")
                println()
                continue
            }

            if (payment < totalPrice) {
                println("Insufficient payment! You need at least ₱$totalPrice.")
                println()
                continue
            }

            val change = payment - totalPrice
            println("Payment successful! You paid ₱$payment.")
            println()

            if (change > 0) {
                println("Your change: ₱$change")
                println()
            }

            return true
        }
    }


    private fun textFileCarList(carList: MutableList<Cars>, filename: String = "cars.txt") {
        val file = File(filename)
        file.printWriter().use { writer ->
            carList.forEach { car ->
                writer.println("${car.getCarId()},${car.getCarName()},${car.getCarBrand()}")
            }
        }
    }

    private fun textFileAvailCar(availableCarList: MutableList<Cars>, filename: String = "available_cars.txt") {
        val file = File(filename)
        file.printWriter().use { writer ->
            availableCarList.forEach { car ->
                writer.println("${car.getCarId()},${car.getCarName()},${car.getCarBrand()}")
            }
        }
    }

    private fun textFileNotAvail(notAvailableCarList: MutableList<Cars>, filename: String = "not_available_cars.txt") {
        val file = File(filename)
        file.printWriter().use { writer ->
            notAvailableCarList.forEach { car ->
                writer.println("${car.getCarId()},${car.getCarName()},${car.getCarBrand()}")
            }
        }
    }


    fun loadNotAvailableCarsFromFile(filename: String = "not_available_cars.txt"): MutableList<Cars> {
        val file = File(filename)
        val notAvailableCarList = mutableListOf<Cars>()

        if (!file.exists()) return notAvailableCarList

        file.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val carId = parts[0].toInt()
                val carName = parts[1]
                val carBrand = parts[2]
                notAvailableCarList.add(Cars(carName, carBrand, carId))
            }
        }
        return notAvailableCarList
    }


    fun loadAvailableCarsFromFile(filename: String = "available_cars.txt"): MutableList<Cars> {
        val file = File(filename)
        val availableCarList = mutableListOf<Cars>()

        if (!file.exists()) return availableCarList

        file.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val carId = parts[0].toInt()
                val carName = parts[1]
                val carBrand = parts[2]
                availableCarList.add(Cars(carName, carBrand, carId))
            }
        }
        return availableCarList
    }


    fun loadAllCarsFromFile(filename: String = "cars.txt"): MutableList<Cars> {
        val file = File(filename)
        val carList = mutableListOf<Cars>()

        if (!file.exists()) {

            return carList
        }

        file.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val carId = parts[0].toIntOrNull()
                val carName = parts[1]
                val carBrand = parts[2]

                if (carId != null) {
                    carList.add(Cars(carName, carBrand, carId))
                }
            }
        }

        return carList
    }


    fun returnAdmin(
        carList: MutableList<Cars>,
        notAvailableCarList: MutableList<Cars>,
        rentalHistory: HashMap<String, MutableList<Cars>>,
        availableCarList: MutableList<Cars>,
        userAccounts: MutableList<Users>
    ) {


        if (carList.isNotEmpty()) {
            println("Enter Client name: ")
            val nameUser = readlnOrNull()?.trim()
            if (nameUser.isNullOrEmpty()) {
                println("Invalid name please enter a valid name!")
                println()
                return returnAdmin(carList, notAvailableCarList, rentalHistory, availableCarList, userAccounts)
            }

            val locateUser = userAccounts.find { it.getUsername()?.trim() == nameUser.trim() }
            if (locateUser == null) {
                println("There are no $nameUser client exist")
                println()
                return returnAdmin(carList, notAvailableCarList, rentalHistory, availableCarList, userAccounts)
            }

            if (!rentalHistory[nameUser].isNullOrEmpty()) {
                println("Enter a car to return")
                val name = readlnOrNull()?.trim()
                if (name.isNullOrEmpty()) {
                    println("Invalid car name, please enter a valid car name to return")
                    println()
                    return returnAdmin(carList, notAvailableCarList, rentalHistory, availableCarList, userAccounts)
                }
                val locateCar = notAvailableCarList.find { it.getCarName() == name }
                val locateIndex = notAvailableCarList.indexOfFirst { it.getCarName() == name }

                if (locateCar == null) {
                    println("No history of renting $name")
                    println()
                }
                if (locateIndex == -1) {
                    println("No history of renting $name")
                    println()
                }
                val car = notAvailableCarList.removeAt(locateIndex)

                availableCarList.add(car)

                if (!carList.contains(car)) {
                    carList.add(car)
                }
                rentalHistory[nameUser]?.removeIf { it.getCarName() == name }


                if (rentalHistory[nameUser].isNullOrEmpty()) {
                    rentalHistory.remove(name)
                }
                textFileAvailCar(availableCarList)
                textFileNotAvail(notAvailableCarList)
                saveRentalHistoryToFile(rentalHistory)
                println("The car $name has successfully returned")
                println()
            } else {
                println("The client $nameUser has no transaction")
                println()
            }


        } else {
            println("The list is empty")
            println()
        }


    }


    fun clear(availableCarList: MutableList<Cars>, carList: MutableList<Cars>) {
        if (carList.isNotEmpty()) {
            println(
                """
                    Do you really want to delete?
                    1. Yes
                    2. No
                """.trimIndent()
            )
            val enter = readln().toIntOrNull()
            if (enter == null) {
                println("Invalid choice please enter a valid choice")
                println()
            }
            when (enter) {
                1 -> {
                    carList.clear()
                    availableCarList.clear()
                    textFileAvailCar(availableCarList)
                    textFileCarList(carList)
                    println("Successfully cleared")
                    println()
                }

                2 -> {
                    return
                }

                else -> {
                    println("Invalid choice please enter a valid choice")
                    println()
                }
            }


        } else {
            println("The list is empty")
            println()

        }


    }


    fun returnCar(
        carList: MutableList<Cars>,
        notAvailableCarList: MutableList<Cars>,
        rentalHistory: HashMap<String, MutableList<Cars>>,
        username: String, availableCarList: MutableList<Cars>
    ) {
        if (carList.isNotEmpty()) {
            if (!rentalHistory[username].isNullOrEmpty()) {
                println("Enter a car to return")
                val name = readlnOrNull()?.trim()
                if (name.isNullOrEmpty()) {
                    println("Invalid car name, please enter a valid car name to return")
                    println()
                    return returnCar(carList, notAvailableCarList, rentalHistory, username, availableCarList)
                }
                val locateCar = notAvailableCarList.find { it.getCarName() == name }
                val locateIndex = notAvailableCarList.indexOfFirst { it.getCarName() == name }

                if (locateCar == null) {
                    println("You have no history of renting $name")
                    println()
                }
                if (locateIndex == -1) {
                    println("You have no history of renting $name")
                    println()
                }
                val car = notAvailableCarList.removeAt(locateIndex)

                availableCarList.add(car)

                if (!carList.contains(car)) {
                    carList.add(car)
                }
                rentalHistory[username]?.removeIf { it.getCarName() == name }


                if (rentalHistory[username].isNullOrEmpty()) {
                    rentalHistory.remove(username)
                }
                textFileAvailCar(availableCarList)
                textFileNotAvail(notAvailableCarList)
                saveRentalHistoryToFile(rentalHistory)
                println("The car $name has successfully returned")
                println()
            } else {
                println("No rented cars")
                println()

            }
        } else {
            println("The list is empty")
            println()
        }


    }


    private fun saveRentalHistoryToFile(
        rentalHistory: HashMap<String, MutableList<Cars>>,
        filename: String = "rental_history.txt"
    ) {
        val file = File(filename)
        file.printWriter().use { writer ->
            rentalHistory.forEach { (username, cars) ->
                val carIds = cars.joinToString("|") { it.getCarId().toString() }
                writer.println("$username:$carIds")
            }
        }
    }


}