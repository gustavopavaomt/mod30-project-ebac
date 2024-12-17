package br.com.gpavao.jdbc;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

    @Test
    void shouldConnectToDatabase() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            assertNotNull(connection);
            assertTrue(connection.isValid(1));
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    void shouldManageTransactionCorrectly() {
        try (ConnectionManager manager = new ConnectionManager()) {
            manager.beginTransaction();

            // Simula algumas operações
            Connection conn = manager.getConnection();
            assertFalse(conn.getAutoCommit());

            manager.commit();

            assertTrue(conn.isValid(1));
        } catch (SQLException e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
