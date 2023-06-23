package main.kotlin

import controller.DatabaseEntity
import entity.EntityClass
import enumerable.RelationType
import main.kotlin.config.AppConfig
import main.kotlin.entities.CarEntity
import main.kotlin.entities.HouseEntity
import main.kotlin.entities.PersonEntity

val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
val prsEntity = PersonEntity()
val houEntity = HouseEntity()
val carEntity = CarEntity()

fun main(args: Array<String>) {
    //create()
    //insert()
    //update()
    //delete()
    transaction()

    printQuery(select())
    //printQuery(join())
}

fun create() {
    entityDB.create(houEntity)
    entityDB.create(carEntity)
    entityDB.create(prsEntity)
}

fun insert() {
    // Person 1
    houEntity[HouseEntity.HOU_ADDRESS] = "Rua dos Bobos"
    houEntity[HouseEntity.HOU_NUMBER] = 100

    entityDB.insert(houEntity)
    houEntity.clear()

    carEntity[CarEntity.CAR_MODEL] = "Monza"
    carEntity[CarEntity.CAR_YEAR] = 1989
    carEntity[CarEntity.CAR_PRICE] = 5000.00

    entityDB.insert(carEntity)
    carEntity.clear()

    prsEntity[PersonEntity.PRS_NAME] = "Carlos 1"
    prsEntity[PersonEntity.PRS_AGE] = 31
    prsEntity[HouseEntity.HOU_ID] = 1
    prsEntity[CarEntity.CAR_ID] = 1

    entityDB.insert(prsEntity)
    prsEntity.clear()

    // Person 2
    houEntity[HouseEntity.HOU_ADDRESS] = "Rua dos Malucos"
    houEntity[HouseEntity.HOU_NUMBER] = 150

    entityDB.insert(houEntity)
    houEntity.clear()

    carEntity[CarEntity.CAR_MODEL] = "Passat"
    carEntity[CarEntity.CAR_YEAR] = 1977
    carEntity[CarEntity.CAR_PRICE] = 2500.00

    entityDB.insert(carEntity)
    carEntity.clear()

    prsEntity[PersonEntity.PRS_NAME] = "Carlos 2"
    prsEntity[PersonEntity.PRS_AGE] = 30
    prsEntity[HouseEntity.HOU_ID] = 1

    entityDB.insert(prsEntity)
    prsEntity.clear()

    // Person 3
    prsEntity[PersonEntity.PRS_NAME] = "Carlos 3"
    prsEntity[PersonEntity.PRS_AGE] = 15

    entityDB.insert(prsEntity)
    prsEntity.clear()
}

fun update() {
    prsEntity[PersonEntity.PRS_AGE] = 31
    prsEntity.setFilter(PersonEntity.PRS_ID, 2)

    entityDB.update(prsEntity)
    prsEntity.clear()
}

fun delete() {
    prsEntity.setFilter(PersonEntity.PRS_ID, 3)

    entityDB.delete(prsEntity)
    prsEntity.clear()
}

fun transaction() {
    entityDB.transaction()

    prsEntity[PersonEntity.PRS_AGE] = 100
    prsEntity.setFilter(PersonEntity.PRS_ID, 3)

    entityDB.update(prsEntity)
    prsEntity.clear()

    entityDB.commit()
}

fun select(): List<EntityClass> {
    return entityDB.select(prsEntity)
}

fun join(): List<EntityClass> {
    prsEntity.setRelation(houEntity, RelationType.INNER)
    prsEntity.setRelation(carEntity, RelationType.INNER)
    prsEntity.setFilter(PersonEntity.PRS_ID, 1)
    return entityDB.join(prsEntity)
}

fun printQuery(resultList: List<EntityClass>) {
    resultList.forEach { prs ->
        println("===== ${prs.name} =====")
        println("---> Basic info")
        println("  ID     : " + prs[PersonEntity.PRS_ID])
        println("  Name   : " + prs[PersonEntity.PRS_NAME])
        println("  Age    : " + prs[PersonEntity.PRS_AGE])

        prs.entityRelations.filter { rlt -> rlt.entityClass.name == HouseEntity.HOUSE }.forEach { car ->
            println("---> House")
            println("  ID     : " + car.entityClass[HouseEntity.HOU_ID])
            println("  Address: " + car.entityClass[HouseEntity.HOU_ADDRESS])
            println("  Number : " + car.entityClass[HouseEntity.HOU_NUMBER])
        }

        prs.entityRelations.filter { rlt -> rlt.entityClass.name == CarEntity.CAR }.forEach { car ->
            println("---> Car")
            println("  ID     : " + car.entityClass[CarEntity.CAR_ID])
            println("  Name   : " + car.entityClass[CarEntity.CAR_MODEL])
            println("  Age    : " + car.entityClass[CarEntity.CAR_YEAR])
            println("  Price  : " + car.entityClass[CarEntity.CAR_PRICE])
        }

        println()
        println()
    }
}