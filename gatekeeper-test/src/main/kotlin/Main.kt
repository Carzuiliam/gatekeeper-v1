package main.kotlin

import controller.DatabaseEntity
import main.kotlin.config.AppConfig
import main.kotlin.entities.CarEntity
import main.kotlin.entities.PersonEntity

val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
val carEntity = CarEntity()
val prsEntity = PersonEntity()

fun main(args: Array<String>) {
    entityDB.create(carEntity)
    entityDB.create(prsEntity)

    basicOperations()
    //innerJoin()
}

fun basicOperations() {
    // ========== INSERT ==========
//    carEntity[CarEntity.CAR_MODEL] = "Monza"
//    carEntity[CarEntity.CAR_YEAR] = 1989
//    carEntity[CarEntity.CAR_PRICE] = 5000.00
//
//    entityDB.insert(carEntity)
//    carEntity.clear()
//
//    prsEntity[PersonEntity.PRS_NAME] = "Carlos"
//    prsEntity[PersonEntity.PRS_AGE] = 31
//    prsEntity[CarEntity.CAR_ID] = 1
//
//    entityDB.insert(prsEntity)
//    prsEntity.clear()
//
//    prsEntity[PersonEntity.PRS_NAME] = "Carzuiliam"
//    prsEntity[PersonEntity.PRS_AGE] = 30
//
//    entityDB.insert(prsEntity)
//    prsEntity.clear()

    // ========== UPDATE ==========
//    prsEntity[PersonEntity.PRS_AGE] = 31
//    prsEntity.setFilter(PersonEntity.PRS_ID, 2)
//    prsEntity.setFilter(PersonEntity.PRS_AGE, 31)
//
//    entityDB.update(prsEntity)
//    prsEntity.clear()

    // ========== DELETE ==========
//    prsEntity.setFilter(PersonEntity.PRS_ID, 3)
//
//    entityDB.delete(prsEntity)
//    prsEntity.clear()

    // ========== SELECT ==========
    println("----- PERSON -----")
    entityDB.select(prsEntity)
    prsEntity.dataResults.forEach { dtr ->
        println("ID    : " + dtr[PersonEntity.PRS_ID])
        println("Name  : " + dtr[PersonEntity.PRS_NAME])
        println("Age   : " + dtr[PersonEntity.PRS_AGE])
        println("Car ID: " + dtr[CarEntity.CAR_ID])
        println()
    }

    println("------ CARS ------")
    entityDB.select(carEntity)
    carEntity.dataResults.forEach { dtr ->
        println("ID    : " + dtr[CarEntity.CAR_ID])
        println("Model : " + dtr[CarEntity.CAR_MODEL])
        println("Year  : " + dtr[CarEntity.CAR_YEAR])
        println("Price : " + dtr[CarEntity.CAR_PRICE])
        println()
    }
}

fun innerJoin() {

}

fun transaction() {

}