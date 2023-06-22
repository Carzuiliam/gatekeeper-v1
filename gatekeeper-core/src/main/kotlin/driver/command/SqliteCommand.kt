package driver.command

import model.DataAttribute
import enumerable.AttributeType
import enumerable.OperatorType
import model.DataEntity
import model.DataField
import model.DataFilter

class SqliteCommand : SqlCommand() {
    override fun create(dataEntity: DataEntity): String {
        val attributesList = dataEntity.dataAttributes
        val primaryKeysList = dataEntity.dataAttributes.filter { attr -> attr.primaryKey }
        val foreignKeysList = dataEntity.dataAttributes.filter { attr -> attr.foreignKey }

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

            if (index != attributesList.size - 1) {
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

    override fun insert(dataEntity: DataEntity): String {
        val dataFieldsList = dataEntity.dataRecord.dataFields

        val sqlStrColumns = StringBuilder()
        val sqlStrValues = StringBuilder()

        dataFieldsList.forEachIndexed { index, field ->
            sqlStrColumns.append(field.dataAttribute.name)

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

    override fun update(dataEntity: DataEntity): String {
        val dataFieldsList = dataEntity.dataRecord.dataFields
        val dataFilterList = dataEntity.dataFilter

        val sqlStrValues = StringBuilder()
        val sqlStrFilter = StringBuilder()

        dataFieldsList.forEachIndexed { index, field ->
            sqlStrValues.append("  ${field.dataAttribute.name} = ${fieldValueToSQL(field)}")

            if (index != dataFieldsList.size - 1) {
                sqlStrValues.append(", ")
            }
        }

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("${filter.dataAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

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

    override fun delete(dataEntity: DataEntity): String {
        val dataFilterList = dataEntity.dataFilter

        val sqlStrFilter = StringBuilder()

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("${filter.dataAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

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

    override fun select(dataEntity: DataEntity): String {
        val dataFilterList = dataEntity.dataFilter

        val sqlStrFilter = StringBuilder()

        if (dataFilterList.isNotEmpty()) {
            sqlStrFilter.append("WHERE${lineSeparator}  ")

            dataFilterList.forEachIndexed { index, filter ->
                sqlStrFilter.append("${filter.dataAttribute.name} ${operatorToSQL(filter)} ${filterValueToSQL(filter)}")

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

    override fun typeToSQL(dataAttribute: DataAttribute): String {
        return when (dataAttribute.type) {
            AttributeType.INTEGER -> "INTEGER"
            AttributeType.DOUBLE -> "DECIMAL(${dataAttribute.size})"
            AttributeType.STRING -> "TEXT"
            AttributeType.DATETIME -> "DATETIME"
        }
    }

    override fun defaultValueToSQL(dataAttribute: DataAttribute): String {
        return when (dataAttribute.type) {
            AttributeType.INTEGER,
            AttributeType.DOUBLE -> {
                if (dataAttribute.defaultValue != null) {
                    dataAttribute.defaultValue.toString()
                } else {
                    "NULL"
                }
            }
            AttributeType.STRING,
            AttributeType.DATETIME -> {
                if (dataAttribute.defaultValue != null) {
                    "\'${dataAttribute.defaultValue}\'"
                } else {
                    "NULL"
                }
            }
        }
    }

    override fun fieldValueToSQL(dataField: DataField): String {
        return when (dataField.dataAttribute.type) {
            AttributeType.INTEGER,
            AttributeType.DOUBLE -> {
                if (dataField.value != null) {
                    dataField.value.toString()
                } else {
                    "NULL"
                }
            }
            AttributeType.STRING,
            AttributeType.DATETIME -> {
                if (dataField.value != null) {
                    "\'${dataField.value}\'"
                } else {
                    "NULL"
                }
            }
        }
    }

    override fun filterValueToSQL(dataFilter: DataFilter): String {
        return when (dataFilter.dataAttribute.type) {
            AttributeType.INTEGER,
            AttributeType.DOUBLE -> {
                if (dataFilter.value != null) {
                    dataFilter.value.toString()
                } else {
                    "NULL"
                }
            }
            AttributeType.STRING,
            AttributeType.DATETIME -> {
                if (dataFilter.value != null) {
                    "\'${dataFilter.value}\'"
                } else {
                    "NULL"
                }
            }
        }
    }

    override fun operatorToSQL(dataFilter: DataFilter): String {
        return when (dataFilter.operatorType) {
            OperatorType.EQUALS -> "="
            OperatorType.NOT_EQUALS -> "<>"
            OperatorType.LESS_THAN -> "<"
            OperatorType.LESS_OR_EQUALS_THAN -> "<="
            OperatorType.GREATER_THAN -> ">"
            OperatorType.GREATER_OR_EQUALS_THAN -> ">="
        }
    }
}