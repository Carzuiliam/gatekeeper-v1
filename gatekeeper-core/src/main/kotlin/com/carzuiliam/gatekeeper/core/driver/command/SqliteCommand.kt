package main.kotlin.com.carzuiliam.gatekeeper.core.driver.command

import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityAttribute
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityAttributeType
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.AttributeOperatorType
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityClass
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityField
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityFilter
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityRelation
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityRelationType
import java.util.Date

class SqliteCommand : SqlCommand() {
    override fun create(dataEntity: EntityClass): String {
        val attributesList = dataEntity.entityAttributes
        val primaryKeysList = dataEntity.entityAttributes.filter { attr -> attr.primaryKey }
        val foreignKeysList = dataEntity.entityAttributes.filter { attr -> attr.foreignKey }

        val sqlStrAttributes = StringBuilder()
        val sqlStrPrimaryKeys = StringBuilder()
        val sqlStrForeignKeys = StringBuilder()

        attributesList.forEachIndexed { index, attr ->

            sqlStrAttributes.append("  ${attr.name} ${typeToSQL(attr)}")

            if (attr.notNull) {
                sqlStrAttributes.append(" NOT NULL")
            }

            if (attr.defaultValue != null) {
                sqlStrAttributes.append(" DEFAULT ${defaultValueToSQL(attr)}")
            }

            if (index != attributesList.size - 1 || primaryKeysList.isNotEmpty() || foreignKeysList.isNotEmpty()) {
                sqlStrAttributes.append(",${System.lineSeparator()}")
            }
        }

        if (primaryKeysList.isNotEmpty()) {
            sqlStrPrimaryKeys.append("  PRIMARY KEY (")

            primaryKeysList.forEachIndexed { index, attr ->
                sqlStrPrimaryKeys.append(attr.name)

                if (index != primaryKeysList.size - 1) {
                    sqlStrPrimaryKeys.append(", ")
                }
            }

            sqlStrPrimaryKeys.append(")")

            if (foreignKeysList.isNotEmpty()) {
                sqlStrPrimaryKeys.append(",")
            }
        }

        if (foreignKeysList.isNotEmpty()) {
            foreignKeysList.forEachIndexed { index, attr ->
                sqlStrForeignKeys.append("  FOREIGN KEY (${attr.name}) REFERENCES ${attr.foreignTable} (${attr.name})")

                if (index != foreignKeysList.size - 1) {
                    sqlStrForeignKeys.append(",${lineSeparator}")
                }
            }
        }

        sqlStrBuilder.clear()
        sqlStrBuilder.appendLine("CREATE TABLE IF NOT EXISTS ${dataEntity.name} (")
        sqlStrBuilder.appendLine("$sqlStrAttributes")
        sqlStrBuilder.appendLine("$sqlStrPrimaryKeys")
        sqlStrBuilder.appendLine("$sqlStrForeignKeys")
        sqlStrBuilder.appendLine(");")

        return sqlStrBuilder.toString()
    }

    override fun insert(dataEntity: EntityClass): String {
        val dataFieldsList = dataEntity.entityRecord.entityFields

        val sqlStrColumns = StringBuilder()
        val sqlStrValues = StringBuilder()

        dataFieldsList.forEachIndexed { index, field ->
            sqlStrColumns.append(field.entityAttribute.name)

            if (index != dataFieldsList.size - 1) {
                sqlStrColumns.append(", ")
            }
        }

        dataFieldsList.forEachIndexed { index, field ->
            sqlStrValues.append(fieldValueToSQL(field))

            if (index != dataFieldsList.size - 1) {
                sqlStrValues.append(",")
            }
        }

        sqlStrBuilder.clear()
        sqlStrBuilder.appendLine("INSERT INTO ${dataEntity.name}")
        sqlStrBuilder.appendLine("  (${sqlStrColumns})")
        sqlStrBuilder.appendLine("VALUES")
        sqlStrBuilder.appendLine("  (${sqlStrValues});")

        return sqlStrBuilder.toString()
    }

    override fun update(dataEntity: EntityClass): String {
        val dataFieldsList = dataEntity.entityRecord.entityFields
        val dataFilterList = dataEntity.entityFilter

        val sqlStrValues = StringBuilder()
        val sqlStrFilter = StringBuilder()

        dataFieldsList.forEachIndexed { index, field ->
            sqlStrValues.append("  ${field.entityAttribute.name} = ${fieldValueToSQL(field)}")

            if (index != dataFieldsList.size - 1) {
                sqlStrValues.append(", ")
            }
        }

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("${filter.entityAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

                if (index != dataFilterList.size - 1) {
                    sqlStrFilter.append("$lineSeparator  AND ")
                }
            }
        }

        sqlStrBuilder.clear()
        sqlStrBuilder.appendLine("UPDATE ${dataEntity.name} SET")
        sqlStrBuilder.appendLine("$sqlStrValues")
        sqlStrBuilder.appendLine("${sqlStrFilter};")

        return sqlStrBuilder.toString()
    }

    override fun delete(dataEntity: EntityClass): String {
        val dataFilterList = dataEntity.entityFilter

        val sqlStrFilter = StringBuilder()

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("${filter.entityAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

                if (index != dataFilterList.size - 1) {
                    sqlStrFilter.append("$lineSeparator  AND ")
                }
            }
        }

        sqlStrBuilder.clear()
        sqlStrBuilder.appendLine("DELETE FROM ${dataEntity.name}")
        sqlStrBuilder.appendLine("$sqlStrFilter")
        sqlStrBuilder.appendLine(";")

        return sqlStrBuilder.toString()
    }

    override fun select(dataEntity: EntityClass): String {
        val dataFilterList = dataEntity.entityFilter

        val sqlStrFilter = StringBuilder()

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("${filter.entityAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

                if (index != dataFilterList.size - 1) {
                    sqlStrFilter.append("$lineSeparator  AND ")
                }
            }
        }

        sqlStrBuilder.clear()
        sqlStrBuilder.appendLine("SELECT * FROM ${dataEntity.name}")
        sqlStrBuilder.appendLine("$sqlStrFilter")
        sqlStrBuilder.appendLine(";")

        return sqlStrBuilder.toString()
    }

    override fun join(dataEntity: EntityClass): String {
        val dataRelationList = dataEntity.entityRelations
        val dataFilterList = dataEntity.entityFilter

        val sqlStrJoin = StringBuilder()
        val sqlStrFilter = StringBuilder()

        if (dataRelationList.isNotEmpty()) {
            dataRelationList.forEachIndexed { index, rlt ->
                val primaryKeys = rlt.entityClass.entityAttributes.filter { attr -> attr.primaryKey }

                if (primaryKeys.isNotEmpty()) {
                    sqlStrJoin.append("  ${joinToSQL(rlt)} ${rlt.entityClass.name} t${index + 2}")

                    primaryKeys.forEachIndexed { idx, key ->
                        if (idx == 0) {
                            sqlStrJoin.append(" ON t1.${key.name} = t${index + 2}.${key.name}")
                        } else {
                            sqlStrJoin.append(" AND t1.${key.name} = t${index + 2}.${key.name}")
                        }

                        if (idx != primaryKeys.size - 1) {
                            sqlStrJoin.append(lineSeparator)
                        }
                    }
                }

                if (index != dataRelationList.size - 1) {
                    sqlStrJoin.append(lineSeparator)
                }
            }
        }

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("t1.${filter.entityAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

                if (index != dataFilterList.size - 1) {
                    sqlStrFilter.append("$lineSeparator  AND ")
                }
            }
        }

        sqlStrBuilder.clear()
        sqlStrBuilder.appendLine("SELECT * FROM ${dataEntity.name} t1")
        sqlStrBuilder.appendLine("$sqlStrJoin")
        sqlStrBuilder.appendLine("$sqlStrFilter")
        sqlStrBuilder.appendLine(";")

        return sqlStrBuilder.toString()
    }

    override fun typeToSQL(entityAttribute: EntityAttribute): String {
        return when (entityAttribute.type) {
            EntityAttributeType.INTEGER -> "INTEGER"
            EntityAttributeType.DOUBLE -> "DECIMAL(${entityAttribute.size})"
            EntityAttributeType.STRING -> "TEXT"
            EntityAttributeType.DATETIME -> "DATETIME"
        }
    }

    override fun defaultValueToSQL(entityAttribute: EntityAttribute): String {
        return when (entityAttribute.type) {
            EntityAttributeType.INTEGER,
            EntityAttributeType.DOUBLE -> {
                if (entityAttribute.defaultValue != null) {
                    entityAttribute.defaultValue.toString()
                } else {
                    "NULL"
                }
            }
            EntityAttributeType.STRING,
            EntityAttributeType.DATETIME -> {
                if (entityAttribute.defaultValue != null) {
                    "\'${entityAttribute.defaultValue}\'"
                } else {
                    "NULL"
                }
            }
        }
    }

    override fun fieldValueToSQL(entityField: EntityField): String {
        return when (entityField.entityAttribute.type) {
            EntityAttributeType.INTEGER,
            EntityAttributeType.DOUBLE -> {
                if (entityField.value != null) {
                    entityField.value.toString()
                } else {
                    "NULL"
                }
            }
            EntityAttributeType.STRING -> {
                if (entityField.value != null) {
                    "\'${entityField.value}\'"
                } else {
                    "NULL"
                }
            }
            EntityAttributeType.DATETIME -> {
                if (entityField.value != null) {
                    "${(entityField.value as Date).toInstant().toEpochMilli()}"
                } else {
                    "NULL"
                }
            }
        }
    }

    override fun filterValueToSQL(entityFilter: EntityFilter): String {
        return when (entityFilter.entityAttribute.type) {
            EntityAttributeType.INTEGER,
            EntityAttributeType.DOUBLE -> {
                if (entityFilter.value != null) {
                    entityFilter.value.toString()
                } else {
                    "NULL"
                }
            }
            EntityAttributeType.STRING -> {
                if (entityFilter.value != null) {
                    "\'${entityFilter.value}\'"
                } else {
                    "NULL"
                }
            }
            EntityAttributeType.DATETIME -> {
                if (entityFilter.value != null) {
                    "${(entityFilter.value as Date).toInstant().toEpochMilli()}"
                } else {
                    "NULL"
                }
            }
        }
    }

    override fun operatorToSQL(entityFilter: EntityFilter): String {
        return when (entityFilter.attributeOperatorType) {
            AttributeOperatorType.EQUALS -> if (entityFilter.value != null) "=" else "IS"
            AttributeOperatorType.NOT_EQUALS -> "<>"
            AttributeOperatorType.LESS_THAN -> "<"
            AttributeOperatorType.LESS_OR_EQUALS_THAN -> "<="
            AttributeOperatorType.GREATER_THAN -> ">"
            AttributeOperatorType.GREATER_OR_EQUALS_THAN -> ">="
        }
    }

    override fun joinToSQL(entityRelation: EntityRelation): String {
        return when (entityRelation.entityRelationType) {
            EntityRelationType.INNER -> "INNER JOIN"
            EntityRelationType.LEFT -> "LEFT JOIN"
        }
    }
}