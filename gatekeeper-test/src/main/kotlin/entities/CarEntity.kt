package main.kotlin.entities

import enumerable.AttributeType
import model.DataAttribute
import model.DataEntity

class CarEntity: DataEntity(CAR) {
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
                DataAttribute(CAR_ID, AttributeType.INTEGER) {
                    it.primaryKey = true
                },
                DataAttribute(CAR_MODEL, AttributeType.STRING) {
                    it.notNull = true
                    it.defaultValue = "Unknown"
                },
                DataAttribute(CAR_YEAR, AttributeType.INTEGER) {
                    it.defaultValue = null
                },
                DataAttribute(CAR_PRICE, AttributeType.DOUBLE) {
                    it.size = "10,2"
                    it.defaultValue = 0
                }
            )
        }
    }
}