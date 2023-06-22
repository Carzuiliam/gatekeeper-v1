package model

import enumerable.OperatorType

abstract class DataEntity(val name: String) {
    var dataAttributes: List<DataAttribute> = listOf()
    var dataResults: MutableList<DataRecord> = mutableListOf()
    var dataFilter: MutableList<DataFilter> = mutableListOf()
    var dataRecord: DataRecord = DataRecord()

    fun setAttributes(function: () -> List<DataAttribute>) {
        dataAttributes = function.invoke()
    }

    fun setValue(field: String, value: Any?) {
        val index = dataRecord.dataFields.indices.find {
            dataRecord.dataFields[it].dataAttribute.name == field
        }

        if (index != null) {
            dataRecord.dataFields[index].value = value
        } else {
            val attr = dataAttributes.find { att ->
                att.name == field
            }

            if (attr != null) {
                dataRecord.dataFields.add(DataField(attr, value))
            }
        }
    }

    fun setFilter(field: String, value: Any?, operatorType: OperatorType = OperatorType.EQUALS) {
        val attr = dataAttributes.find {
            it.name == field
        }

        if (attr != null) {
            dataFilter.add(DataFilter(attr, operatorType, value))
        }
    }

    fun clear() {
        dataResults.clear()
        dataFilter.clear()
        dataRecord.dataFields.clear()
    }

    operator fun set(field: String, value: Any?) {
        setValue(field, value)
    }
}