package entity

import enumerable.AttributeType
import generic.Configurable

class EntityAttribute(val name: String, val type: AttributeType, initFunction: ((EntityAttribute) -> Unit)? = null) : Configurable<EntityAttribute>(initFunction) {
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