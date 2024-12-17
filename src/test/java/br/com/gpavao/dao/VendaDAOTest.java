package br.com.gpavao.dao;

import br.com.gpavao.domain.*;
import br.com.gpavao.jdbc.ConnectionManager;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VendaDAOTest {
    private VendaDAO vendaDAO;
    private ClienteDAO clienteDAO;
    private ProdutoDAO produtoDAO;
    private ConnectionManager manager;

    @BeforeEach
    void setUp() throws SQLException {
        vendaDAO = new VendaDAO();
        clienteDAO = new ClienteDAO();
        produtoDAO = new ProdutoDAO();
        manager = new ConnectionManager();
        manager.beginTransaction();
    }

    @AfterEach
    void tearDown() throws SQLException {
        manager.rollback();
        manager.close();
    }

    @Test
    void deveCadastrarVendaCompleta() throws SQLException {
        // Cadastra cliente
        Cliente cliente = new Cliente();
        cliente.setNome("Test User");
        cliente.setEmail("test@test.com");
        cliente.setCpf("111.222.333-00");
        cliente = clienteDAO.cadastrar(cliente, manager);

        // Cadastra produto
        Produto produto = new Produto();
        produto.setNome("Test Product");
        produto.setPreco(100.0);
        produto.setQuantidadeEstoque(10);
        produto = produtoDAO.cadastrar(produto, manager);

        // Cria venda
        Venda venda = new Venda();
        venda.setClienteId(cliente.getId());
        venda.setDataVenda(LocalDateTime.now());
        venda.setValorTotal(200.0);

        // Cria item da venda
        ProdutoQuantidade item = new ProdutoQuantidade();
        item.setProdutoId(produto.getId());
        item.setQuantidade(2);
        item.setValorUnitario(100.0);
        item.setValorTotal(200.0);

        // Cadastra venda completa
        Venda vendaSalva = vendaDAO.cadastrarVendaCompleta(venda, Arrays.asList(item), manager);

        // Busca venda completa
        Optional<Venda> vendaBuscada = vendaDAO.buscarVendaCompleta(vendaSalva.getId(), manager);

        assertTrue(vendaBuscada.isPresent());
        assertEquals(1, vendaBuscada.get().getItems().size());
        assertEquals(200.0, vendaBuscada.get().getValorTotal());
    }
}