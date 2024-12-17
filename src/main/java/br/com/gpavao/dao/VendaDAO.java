package br.com.gpavao.dao;

import br.com.gpavao.domain.ProdutoQuantidade;
import br.com.gpavao.domain.Venda;
import br.com.gpavao.jdbc.ConnectionManager;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class VendaDAO extends GenericDAO<Venda, Long> {

    private final ProdutoQuantidadeDAO produtoQuantidadeDAO;
    private final ProdutoDAO produtoDAO;

    public VendaDAO() {
        super(Venda.class);
        this.produtoQuantidadeDAO = new ProdutoQuantidadeDAO();
        this.produtoDAO = new ProdutoDAO();
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO vendas (cliente_id, data_venda, valor_total) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE vendas SET cliente_id = ?, data_venda = ?, valor_total = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParams(PreparedStatement stmt, Venda venda) throws SQLException {
        stmt.setLong(1, venda.getClienteId());
        stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
        stmt.setDouble(3, venda.getValorTotal());
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Venda venda) throws SQLException {
        setInsertParams(stmt, venda);
        stmt.setLong(4, venda.getId());
    }

    @Override
    protected Venda mapToEntity(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getLong("id"));
        venda.setClienteId(rs.getLong("cliente_id"));
        venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
        venda.setValorTotal(rs.getDouble("valor_total"));
        return venda;
    }

    public Venda cadastrarVendaCompleta(Venda venda, List<ProdutoQuantidade> items, ConnectionManager manager) throws SQLException {
        try {
            Venda vendaSalva = cadastrar(venda, manager);
            for (ProdutoQuantidade item : items) {
                item.setVendaId(vendaSalva.getId());

                if (!produtoDAO.atualizarEstoque(item.getProdutoId(), item.getQuantidade(), manager)) {
                    throw new SQLException("Estoque insuficiente para o produto ID: " + item.getProdutoId());
                }

                produtoQuantidadeDAO.cadastrar(item, manager);
            }

            return vendaSalva;
        } catch (SQLException e) {
            throw new SQLException("Erro ao cadastrar venda: " + e.getMessage());
        }
    }

    public Optional<Venda> buscarVendaCompleta(Long id, ConnectionManager manager) throws SQLException {
        Optional<Venda> vendaOpt = buscarPorId(id, manager);
        if (vendaOpt.isPresent()) {
            Venda venda = vendaOpt.get();
            List<ProdutoQuantidade> items = produtoQuantidadeDAO.buscarPorVenda(id, manager);
            venda.setItems(items);
            return Optional.of(venda);
        }
        return Optional.empty();
    }
}
