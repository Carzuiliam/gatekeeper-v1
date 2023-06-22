package main.kotlin.config

import enumerable.DatabaseType
import java.io.File

class AppConfig {
    companion object {
        private const val DB_NAME = "gatekeeper.sqlite"
        private const val WORKSPACE_PATH = "D:\\Projects\\Source Codes\\Kotlin\\gatekeeper-v1\\gatekeeper-test\\src\\main\\workspace"

        val DATABASE_TYPE = DatabaseType.SQLITE
        val CONNECTION_STRING = "jdbc:sqlite:$WORKSPACE_PATH${File.separator}$DB_NAME"
    }
}