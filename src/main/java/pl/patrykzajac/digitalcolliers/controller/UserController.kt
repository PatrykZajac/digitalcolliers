package pl.patrykzajac.digitalcolliers.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.patrykzajac.digitalcolliers.dto.Customer
import pl.patrykzajac.digitalcolliers.service.UserService

@RestController
@RequestMapping("/customers")
class UserController(private val userService: UserService) {

    @GetMapping("/getById")
    fun getData(@RequestParam("customerId") customer_id: List<String>): MutableList<Customer>? = userService.getCustomers(customer_id)
}