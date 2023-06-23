package entity

class EntityField(val entityAttribute: EntityAttribute, var value: Any?) {
    var entityRecord: EntityRecord? = null
}