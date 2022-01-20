package pl.patrykzajac.digitalcolliers.entity.mongo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document
data class CalculationLog(
        @Id
        val id: ObjectId = ObjectId.get(),
        val customerId: String,
        val commissionValue: BigDecimal,
        val commissionCalculationDate: LocalDateTime = LocalDateTime.now()
)