package main.kotlin.com.carzuiliam.gatekeeper.core.entity

import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.AttributeOperatorType
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityRelationType

open class EntityClass(val name: String) {
    var entityAttributes: List<EntityAttribute> = listOf()
    var entityRelations: MutableList<EntityRelation> = mutableListOf()
    var entityFilter: MutableList<EntityFilter> = mutableListOf()
    var entityRecord: EntityRecord = EntityRecord()

    fun setAttributes(function: () -> List<EntityAttribute>) {
        entityAttributes = function.invoke()
    }

    fun setValue(field: String, value: Any?) {
        val index = entityRecord.entityFields.indices.find {
            entityRecord.entityFields[it].entityAttribute.name == field
        }

        if (index != null) {
            entityRecord.entityFields[index].value = value
        } else {
            val attr = entityAttributes.find { att ->
                att.name == field
            }

            if (attr != null) {
                entityRecord.entityFields.add(EntityField(attr, value))
            }
        }
    }

    fun setFilter(field: String, value: Any?, attributeOperatorType: AttributeOperatorType = AttributeOperatorType.EQUALS) {
        val attr = entityAttributes.find {
            it.name == field
        }

        if (attr != null) {
            entityFilter.add(EntityFilter(attr, attributeOperatorType, value))
        }
    }

    fun setRelation(entityClass: EntityClass, entityRelationType: EntityRelationType) {
        entityRelations.add(EntityRelation(entityClass, entityRelationType))
    }

    fun getValue(field: String): Any? {
        val index = entityRecord.entityFields.indices.find {
            entityRecord.entityFields[it].entityAttribute.name == field
        }

        return if (index != null) {
            entityRecord.entityFields[index].value
        } else {
            null
        }
    }

    fun clear() {
        entityRelations.clear()
        entityFilter.clear()

        entityRecord.entityFields.clear()
    }

    operator fun set(field: String, value: Any?) {
        setValue(field, value)
    }

    operator fun get(field: String): Any? {
        return getValue(field)
    }
}