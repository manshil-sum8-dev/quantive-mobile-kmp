package za.co.quantive.backend.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AuditLogs : LongIdTable("audit_logs") {
    val userId = reference("user_id", Users).nullable()
    val action = varchar("action", 100)
    val entityType = varchar("entity_type", 50)
    val entityId = varchar("entity_id", 100).nullable()
    val metadata = text("metadata").nullable() // Stored as JSON string
    val ipAddress = varchar("ip_address", 45).nullable()
    val userAgent = varchar("user_agent", 500).nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}
