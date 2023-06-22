package model

import enumerable.AttributeType
import generic.Configurable

class DataAttribute(val name: String, val type: AttributeType, initFunction: ((DataAttribute) -> Unit)? = null) : Configurable<DataAttribute>(initFunction) {
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