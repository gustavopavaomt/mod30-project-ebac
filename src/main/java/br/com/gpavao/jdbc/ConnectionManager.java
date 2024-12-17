package br.com.gpavao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager implements AutoCloseable {
    private final Connection connection;
    private boolean transactionActive;

    public ConnectionManager() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
        this.connection.setAutoCommit(false);
        this.transactionActive = false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void beginTransaction() throws SQLException {
        if (transactionActive) {
            throw new SQLException("Transação já está ativa");
        }
        transactionActive = true;
    }

    public void commit() throws SQLException {
        if (!transactionActive) {
            throw new SQLException("Nenhuma transação ativa para commit");
        }
        connection.commit();
        transactionActive = false;
    }

    public void rollback() throws SQLException {
        if (!transactionActive) {
            throw new SQLException("Nenhuma transação ativa para rollback");
        }
        connection.rollback();
        transactionActive = false;
    }

    @Override
    public void close() {
        try {
            if (transactionActive) {
                rollback();
            }
            if (connection != null && !connection.isClosed()) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão", e);
        }
    }
}