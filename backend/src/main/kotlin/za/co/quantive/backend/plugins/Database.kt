package za.co.quantive.backend.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import za.co.quantive.backend.database.tables.AuditLogs
import za.co.quantive.backend.database.tables.RefreshTokens
import za.co.quantive.backend.database.tables.Users
import org.flywaydb.core.Flyway

fun Application.configureDatabase() {
    val config = environment.config

    val dbHost = config.property("database.host").getString()
    val dbPort = config.property("database.port").getString()
    val dbName = config.property("database.name").getString()
    val dbUser = config.property("database.user").getString()
    val dbPassword = config.property("database.password").getString()
    val maxPoolSize = config.property("database.maxPoolSize").getString().toInt()

    val jdbcUrl = "jdbc:postgresql://$dbHost:$dbPort/$dbName"

    // Configure HikariCP connection pool
    val hikariConfig = HikariConfig().apply {
        this.jdbcUrl = jdbcUrl
        this.username = dbUser
        this.password = dbPassword
        this.driverClassName = "org.postgresql.Driver"
        this.maximumPoolSize = maxPoolSize
        this.isAutoCommit = false
        this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        // Connection pool settings
        this.connectionTimeout = 30000 // 30 seconds
        this.idleTimeout = 600000 // 10 minutes
        this.maxLifetime = 1800000 // 30 minutes
        this.leakDetectionThreshold = 60000 // 1 minute

        // Performance optimizations
        validate()
    }

    val dataSource = HikariDataSource(hikariConfig)

    // Connect Exposed to the database
    Database.connect(
        datasource = dataSource,
        databaseConfig = DatabaseConfig {
            useNestedTransactions = true
        }
    )

    log.info("Database connected successfully to: $jdbcUrl")

    // Run Flyway migrations
    runMigrations(dataSource)
}

private fun Application.runMigrations(dataSource: HikariDataSource) {
    try {
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load()

        val migrationsExecuted = flyway.migrate()
        log.info("Flyway migrations executed: ${migrationsExecuted.migrationsExecuted} migrations")
    } catch (e: Exception) {
        log.error("Failed to run database migrations", e)
        throw e
    }
}
