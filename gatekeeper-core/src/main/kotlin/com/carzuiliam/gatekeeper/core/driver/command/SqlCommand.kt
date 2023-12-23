package main.kotlin.com.carzuiliam.gatekeeper.core.driver.command

import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityAttribute
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityClass
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityField
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityFilter
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityRelation

abstract class SqlCommand {
    protected val sqlStrBuilder = StringBuilder()
    protected val lineSeparator = System.lineSeparator() ?: ""

    abstract fun create(dataEntity: EntityClass): String
    abstract fun insert(dataEntity: EntityClass): String
    abstract fun update(dataEntity: EntityClass): String
    abstract fun delete(dataEntity: EntityClass): String
    abstract fun select(dataEntity: EntityClass): String
    abstract fun join(dataEntity: EntityClass): String

    abstract fun typeToSQL(entityAttribute: EntityAttribute): String
    abstract fun defaultValueToSQL(entityAttribute: EntityAttribute): String
    abstract fun fieldValueToSQL(entityField: EntityField): String
    abstract fun filterValueToSQL(entityFilter: EntityFilter): String
    abstract fun operatorToSQL(entityFilter: EntityFilter): String
    abstract fun joinToSQL(entityRelation: EntityRelation): String
}