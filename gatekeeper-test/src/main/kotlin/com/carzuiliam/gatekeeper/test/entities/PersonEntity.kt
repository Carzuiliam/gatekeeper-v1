package com.carzuiliam.gatekeeper.test.entities

import com.carzuiliam.gatekeeper.core.entity.EntityAttribute
import com.carzuiliam.gatekeeper.core.enumerable.EntityAttributeType
import com.carzuiliam.gatekeeper.core.entity.EntityClass

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
                EntityAttribute(PRS_ID, EntityAttributeType.INTEGER) {
                    it.primaryKey = true
                },
                EntityAttribute(PRS_NAME, EntityAttributeType.STRING) {
                    it.defaultValue = "Unknown"
                },
                EntityAttribute(PRS_AGE, EntityAttributeType.INTEGER) {
                    it.notNull = false
                    it.defaultValue = 0
                },
                EntityAttribute(PRS_BIRTHDAY, EntityAttributeType.DATETIME) {
                    it.notNull = false
                },
                EntityAttribute(HouseEntity.HOU_ID, EntityAttributeType.INTEGER) {
                    it.foreignKey = true
                    it.foreignTable = HouseEntity.HOUSE
                },
                EntityAttribute(CarEntity.CAR_ID, EntityAttributeType.INTEGER) {
                    it.foreignKey = true
                    it.foreignTable = CarEntity.CAR
                }
            )
        }
    }
}