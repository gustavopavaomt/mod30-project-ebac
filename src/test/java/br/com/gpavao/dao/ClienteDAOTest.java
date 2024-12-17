package br.com.gpavao.dao;

import br.com.gpavao.domain.Cliente;
import br.com.gpavao.jdbc.ConnectionManager;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOTest {
    private ClienteDAO clienteDAO;
    private ConnectionManager manager;

    @BeforeEach
    void setUp() throws SQLException {
        clienteDAO = new ClienteDAO();
        manager = new ConnectionManager();
        manager.beginTransaction();
    }

    @AfterEach
    void tearDown() throws SQLException {
        manager.rollback();
        manager.close();
    }

    @Test
    void deveCadastrarCliente() throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setNome("Test User");
        cliente.setEmail("test@test.com");
        cliente.setCpf("000.000.000-00");

        Cliente savedCliente = clienteDAO.cadastrar(cliente, manager);

        assertNotNull(savedCliente.getId());
        assertEquals("Test User", savedCliente.getNome());
    }

    @Test
    void deveBuscarClientePorId() throws SQLException {
        // Primeiro cadastra
        Cliente cliente = new Cliente();
        cliente.setNome("Test User");
        cliente.setEmail("test@test.com");
        cliente.setCpf("123.456.789-01");

        Cliente savedCliente = clienteDAO.cadastrar(cliente, manager);

        // Depois busca
        Optional<Cliente> found = clienteDAO.buscarPorId(savedCliente.getId(), manager);

        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getNome());
    }
}
