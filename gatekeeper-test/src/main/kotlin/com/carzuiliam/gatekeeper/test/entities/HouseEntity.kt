package com.carzuiliam.gatekeeper.test.entities

import com.carzuiliam.gatekeeper.core.entity.EntityAttribute
import com.carzuiliam.gatekeeper.core.entity.EntityClass
import com.carzuiliam.gatekeeper.core.enumerable.EntityAttributeType

class HouseEntity: EntityClass(HOUSE) {
    companion object {
        const val HOUSE = "HOUSE"
        const val HOU_ID = "HOU_ID"
        const val HOU_ADDRESS = "HOU_ADDRESS"
        const val HOU_NUMBER = "HOU_NUMBER"
    }

    init {
        setAttributes {
            listOf(
                EntityAttribute(HOU_ID, EntityAttributeType.INTEGER) {
                    it.primaryKey = true
                },
                EntityAttribute(HOU_ADDRESS, EntityAttributeType.STRING) {
                    it.defaultValue = null
                },
                EntityAttribute(HOU_NUMBER, EntityAttributeType.STRING) {
                    it.defaultValue = null
                }
            )
        }
    }
}