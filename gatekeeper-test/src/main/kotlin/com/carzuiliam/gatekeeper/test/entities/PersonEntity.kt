package com.carzuiliam.gatekeeper.test.entities

import com.carzuiliam.gatekeeper.core.entity.EntityAttribute
import com.carzuiliam.gatekeeper.core.enumerable.AttributeType
import com.carzuiliam.gatekeeper.core.entity.EntityClass
import java.util.Date

class PersonEntity: EntityClass(PERSON) {
    companion object {
        const val PERSON = "PERSON"
        const val PRS_ID = "PRS_ID"
        const val PRS_NAME = "PRS_NAME"
        const val PRS_AGE = "PRS_AGE"
        const val PRS_BIRTHDAY = "PRS_BIRTHDAY"
    }

    init {
        setAttributes {
            listOf(
                EntityAttribute(PRS_ID, AttributeType.INTEGER) {
                    it.primaryKey = true
                },
                EntityAttribute(PRS_NAME, AttributeType.STRING) {
                    it.defaultValue = "Unknown"
                },
                EntityAttribute(PRS_AGE, AttributeType.INTEGER) {
                    it.notNull = false
                    it.defaultValue = 0
                },
                EntityAttribute(PRS_BIRTHDAY, AttributeType.DATETIME) {
                    it.notNull = false
                },
                EntityAttribute(HouseEntity.HOU_ID, AttributeType.INTEGER) {
                    it.foreignKey = true
                    it.foreignTable = HouseEntity.HOUSE
                },
                EntityAttribute(CarEntity.CAR_ID, AttributeType.INTEGER) {
                    it.foreignKey = true
                    it.foreignTable = CarEntity.CAR
                }
            )
        }
    }
}