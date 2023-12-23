package main.kotlin.com.carzuiliam.gatekeeper.core.entity

import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityAttributeType
import main.kotlin.com.carzuiliam.gatekeeper.core.generic.Configurable

class EntityAttribute(val name: String, val type: EntityAttributeType, initFunction: ((EntityAttribute) -> Unit)? = null) : Configurable<EntityAttribute>(initFunction) {
    var primaryKey: Boolean = false
    var foreignKey: Boolean = false
    var foreignTable: String? = null
    var defaultValue: Any? = null
    var notNull: Boolean = false
    var size: String? = null

    init {
        initializeInstance(this)
    }
}