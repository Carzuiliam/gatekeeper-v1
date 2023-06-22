package driver.command

import model.DataAttribute
import model.DataEntity
import model.DataField
import model.DataFilter

abstract class SqlCommand {
    protected val sqlStrBuilder = StringBuilder()
    protected val lineSeparator = System.lineSeparator()

    abstract fun create(dataEntity: DataEntity): String
    abstract fun insert(dataEntity: DataEntity): String
    abstract fun update(dataEntity: DataEntity): String
    abstract fun delete(dataEntity: DataEntity): String
    abstract fun select(dataEntity: DataEntity): String

    abstract fun typeToSQL(dataAttribute: DataAttribute): String
    abstract fun defaultValueToSQL(dataAttribute: DataAttribute): String
    abstract fun fieldValueToSQL(dataField: DataField): String
    abstract fun filterValueToSQL(dataFilter: DataFilter): String
    abstract fun operatorToSQL(dataFilter: DataFilter): String
}