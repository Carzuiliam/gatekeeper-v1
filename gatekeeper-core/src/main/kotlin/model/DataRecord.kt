package model

class DataRecord {
    val dataFields: MutableList<DataField> = mutableListOf()

    fun setValue(field: String, value: Any?) {
        val index = dataFields.indices.find {
            dataFields[it].dataAttribute.name == field
        }

        if (index != null) {
            dataFields[index].value = value
        }
    }

    fun getValue(field: String): Any? {
        val index = dataFields.indices.find {
            dataFields[it].dataAttribute.name == field
        }

        return if (index != null) {
            dataFields[index].value
        } else {
            null
        }
    }

    operator fun set(field: String, value: Any?) {
        setValue(field, value)
    }

    operator fun get(field: String): Any? {
        return getValue(field)
    }
}