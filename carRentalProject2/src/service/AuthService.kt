package service

import model.Admin
import model.Cars
import model.Users

class AuthService(
    carService: CarService, carList: MutableList<Cars>, availableCarList: MutableList<Cars>,
    userAccounts: MutableList<Users>, rentalHistory: HashMap<String, MutableList<Cars>>,
    notAvailableCarList: MutableList<Cars>, userService: UserService
) {

    private val admin = Admin()
    private val adminService = AdminService(
        carService, carList, availableCarList, notAvailableCarList, userService, rentalHistory, userAccounts
    )

    private val userService = UserService(
        userAccounts, carService, carList,
        availableCarList, rentalHistory, notAvailableCarList
    )

    fun adminLogin() {
        println("Enter the Admin Name: ")
        val adminName = readlnOrNull()?.trim()
        if (adminName.isNullOrEmpty()) {
            println("Invalid admin name, Enter a valid admin name")
            println()
            return adminLogin()
        }
        println("Enter the Admin password: ")
        val adminPassword = readlnOrNull()?.trim()
        if (adminPassword.isNullOrEmpty()) {
            println("Invalid admin password, Enter a valid admin password")
            println()
            return adminLogin()
        }
        if (admin.login(adminName, adminPassword)) {
            adminService.adminChoice()

        } else {
            println("Wrong Admin name or password!")
            println()

        }
    }


    fun userLogin(userAccounts: MutableList<Users>) {
        println("Enter Username: ")
        val username = readlnOrNull()?.trim()
        if (username.isNullOrEmpty()) {
            println("Enter a valid username")
            println()
            return userLogin(userAccounts)
        }
        println("Enter Password: ")
        val password = readlnOrNull()?.trim()
        if (password.isNullOrEmpty()) {
            println("Invalid password, Please Enter a valid password")
            println()
            return userLogin(userAccounts)
        }

        val findingUser = userAccounts.find { it.getUsername() == username && it.getPassword() == password }
        if (findingUser != null) {
            println("Successfully Logged in!")
            println()
            userService.userChoice(username)

        } else {
            println("Wrong username or password")
            println()
        }
    }

}