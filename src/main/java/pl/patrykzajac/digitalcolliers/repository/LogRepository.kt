package pl.patrykzajac.digitalcolliers.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import pl.patrykzajac.digitalcolliers.entity.mongo.CalculationLog

@Component
interface LogRepository : MongoRepository<CalculationLog, ObjectId> {
}