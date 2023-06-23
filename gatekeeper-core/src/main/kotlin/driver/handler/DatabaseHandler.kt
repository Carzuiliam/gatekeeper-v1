package driver.handler

import driver.command.SqlCommand
import driver.command.SqliteCommand
import driver.database.SqlDatabase
import driver.database.SqliteDatabase
import entity.EntityClass
import entity.EntityField
import entity.EntityRelation
import enumerable.AttributeType
import enumerable.DatabaseType
import java.sql.ResultSet

class DatabaseHandler(databaseType: DatabaseType, connectionString: String) {
    private var sqlCommand: SqlCommand
    private var sqlDatabase: SqlDatabase

    init {
        when (databaseType) {
            DatabaseType.SQLITE -> {
                sqlCommand = SqliteCommand()
                sqlDatabase = SqliteDatabase(connectionString)
            }
        }
    }

    fun transaction(): Boolean {
        return sqlDatabase.beginTransaction()
    }

    fun commit(): Boolean {
        return sqlDatabase.commitTransaction()
    }

    fun rollback(): Boolean {
        return sqlDatabase.rollbackTransaction()
    }

    fun create(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.create(dataEntity))
    }

    fun insert(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.insert(dataEntity))
    }

    fun update(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.update(dataEntity))
    }

    fun delete(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.delete(dataEntity))
    }

    fun select(dataEntity: EntityClass): MutableList<EntityClass> {
        val resultSet = sqlDatabase.performSQLQuery(sqlCommand.select(dataEntity))
        return resultSet?.let { dumpSelectData(dataEntity, it) } ?: mutableListOf()
    }

    fun join(dataEntity: EntityClass): MutableList<EntityClass> {
        val resultSet = sqlDatabase.performSQLQuery(sqlCommand.join(dataEntity))
        return resultSet?.let { dumpJoinData(dataEntity, it) } ?: mutableListOf()
    }

    private fun dumpSelectData(dataEntity: EntityClass, resultSet: ResultSet): MutableList<EntityClass> {
        val entityClasses = mutableListOf<EntityClass>()

        while (resultSet.next()) {
            val entityClass = EntityClass(dataEntity.name)

            dataEntity.entityAttributes.forEachIndexed { index, attr ->
                val value = when (attr.type) {
                    AttributeType.INTEGER ->  resultSet.getInt(index + 1)
                    AttributeType.DOUBLE -> resultSet.getDouble(index + 1)
                    AttributeType.STRING -> resultSet.getString(index + 1)
                    AttributeType.DATETIME -> resultSet.getDate(index + 1)
                }

                entityClass.entityRecord.entityFields.add(EntityField(attr, value))
            }

            entityClasses.add(entityClass)
        }

        return entityClasses
    }

    private fun dumpJoinData(dataEntity: EntityClass, resultSet: ResultSet): MutableList<EntityClass> {
        val entityClasses = mutableListOf<EntityClass>()

        while (resultSet.next()) {
            val entityClass = EntityClass(dataEntity.name)
            var index = 1

            while (index <= dataEntity.entityAttributes.size) {
                dataEntity.entityAttributes.forEach { attr ->
                    val value = when (attr.type) {
                        AttributeType.INTEGER ->  resultSet.getInt(index)
                        AttributeType.DOUBLE -> resultSet.getDouble(index)
                        AttributeType.STRING -> resultSet.getString(index)
                        AttributeType.DATETIME -> resultSet.getDate(index)
                    }

                    entityClass.entityRecord.entityFields.add(EntityField(attr, value))
                    index++
                }
            }

            dataEntity.entityRelations.forEach { rlt ->
                val relatedEntity = EntityClass(rlt.entityClass.name)
                val entityRelation = dataEntity.entityRelations.first { rtp -> rtp.entityClass.name == rlt.entityClass.name }

                rlt.entityClass.entityAttributes.forEach { attr ->
                    val value = when (attr.type) {
                        AttributeType.INTEGER ->  resultSet.getInt(index)
                        AttributeType.DOUBLE -> resultSet.getDouble(index)
                        AttributeType.STRING -> resultSet.getString(index)
                        AttributeType.DATETIME -> resultSet.getDate(index)
                    }

                    relatedEntity.entityRecord.entityFields.add(EntityField(attr, value))
                    index++
                }

                entityClass.entityRelations.add(EntityRelation(relatedEntity, entityRelation.relationType))
            }

            entityClasses.add(entityClass)
        }

        return entityClasses
    }
}