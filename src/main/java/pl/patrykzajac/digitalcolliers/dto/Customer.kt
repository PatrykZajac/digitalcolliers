package pl.patrykzajac.digitalcolliers.dto

import lombok.Data
import lombok.ToString
import java.math.BigDecimal
import java.util.*

@Data
@ToString
class Customer(val customerId: String, val firstName: String, val lastName: String, val numberOfTransactions: Int, val totalValueOfTransactions: BigDecimal, val transactionFeeValue: BigDecimal, val lastTransactionDate: Date)