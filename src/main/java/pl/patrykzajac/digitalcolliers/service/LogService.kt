package pl.patrykzajac.digitalcolliers.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import pl.patrykzajac.digitalcolliers.dto.Customer
import pl.patrykzajac.digitalcolliers.entity.mongo.CalculationLog
import pl.patrykzajac.digitalcolliers.repository.LogRepository
import java.time.LocalDateTime

@Service
open class LogService(private val logRepository: LogRepository)  {

    open fun processLog(customers: MutableList<Customer>?) {
        customers?.forEach { logRepository.save(transformCustomerToLog(it)) };
    }

    private fun transformCustomerToLog(customer: Customer): CalculationLog {
        return CalculationLog(ObjectId(), customer.customerId, customer.transactionFeeValue, LocalDateTime.now())
    }
}