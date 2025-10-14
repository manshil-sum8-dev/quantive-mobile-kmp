-- Create audit_logs table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NULL REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(100) NULL,
    metadata TEXT NULL,
    ip_address VARCHAR(45) NULL,
    user_agent VARCHAR(500) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on user_id for user activity queries
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);

-- Create index on action for filtering
CREATE INDEX idx_audit_logs_action ON audit_logs(action);

-- Create index on entity_type and entity_id for entity activity queries
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);

-- Create index on created_at for time-based queries
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
