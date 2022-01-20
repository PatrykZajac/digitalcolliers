package pl.patrykzajac.digitalcolliers.service

import org.springframework.stereotype.Service
import pl.patrykzajac.digitalcolliers.dto.Customer
import pl.patrykzajac.digitalcolliers.repository.UserRepository

@Service
class UserService(private val userRepository: UserRepository, private val logService: LogService) {


    fun getCustomers(ids: List<String>): MutableList<Customer>? {
        val customerDataList = if (ids.contains("ALL")) {
            userRepository.allUserData()
        } else {
            userRepository.userDataById(ids)
        }
        logService.processLog(customerDataList)

        return customerDataList
    }
}