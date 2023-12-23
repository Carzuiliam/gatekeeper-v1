package main.kotlin.com.carzuiliam.gatekeeper.test.entities

import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityAttributeType
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityAttribute
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityClass

class CarEntity: EntityClass(CAR) {
    companion object {
        const val CAR = "CAR"
        const val CAR_ID = "CAR_ID"
        const val CAR_MODEL = "CAR_MODEL"
        const val CAR_YEAR = "CAR_YEAR"
        const val CAR_PRICE = "CAR_PRICE"
    }

    init {
        setAttributes {
            listOf(
                EntityAttribute(CAR_ID, EntityAttributeType.INTEGER) {
                    it.primaryKey = true
                },
                EntityAttribute(CAR_MODEL, EntityAttributeType.STRING) {
                    it.notNull = true
                    it.defaultValue = "Unknown"
                },
                EntityAttribute(CAR_YEAR, EntityAttributeType.INTEGER) {
                    it.defaultValue = null
                },
                EntityAttribute(CAR_PRICE, EntityAttributeType.DOUBLE) {
                    it.size = "10,2"
                    it.defaultValue = 0
                }
            )
        }
    }
}