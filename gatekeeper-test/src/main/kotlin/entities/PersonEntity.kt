package main.kotlin.entities

import model.DataAttribute
import enumerable.AttributeType
import model.DataEntity

class PersonEntity: DataEntity(PERSON) {
    companion object {
        const val PERSON = "PERSON"
        const val PRS_ID = "PRS_ID"
        const val PRS_NAME = "PRS_NAME"
        const val PRS_AGE = "PRS_AGE"
    }

    init {
        setAttributes {
            listOf(
                DataAttribute(PRS_ID, AttributeType.INTEGER) {
                    it.primaryKey = true
                },
                DataAttribute(PRS_NAME, AttributeType.STRING) {
                    it.defaultValue = "Unknown"
                },
                DataAttribute(PRS_AGE, AttributeType.INTEGER) {
                    it.notNull = false
                    it.defaultValue = 0
                },
                DataAttribute(CarEntity.CAR_ID, AttributeType.INTEGER) {
                    it.foreignKey = true
                    it.foreignTable = CarEntity.CAR
                }
            )
        }
    }
}