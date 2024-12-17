package br.com.gpavao.dao;

import br.com.gpavao.domain.ProdutoQuantidade;
import br.com.gpavao.jdbc.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoQuantidadeDAO extends GenericDAO<ProdutoQuantidade, Long> {

    public ProdutoQuantidadeDAO() {
        super(ProdutoQuantidade.class);
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO produtos_quantidade (venda_id, produto_id, quantidade, valor_unitario, valor_total) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE produtos_quantidade SET venda_id = ?, produto_id = ?, quantidade = ?, valor_unitario = ?, valor_total = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParams(PreparedStatement stmt, ProdutoQuantidade pq) throws SQLException {
        stmt.setLong(1, pq.getVendaId());
        stmt.setLong(2, pq.getProdutoId());
        stmt.setInt(3, pq.getQuantidade());
        stmt.setDouble(4, pq.getValorUnitario());
        stmt.setDouble(5, pq.getValorTotal());
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, ProdutoQuantidade pq) throws SQLException {
        setInsertParams(stmt, pq);
        stmt.setLong(6, pq.getId());
    }

    @Override
    protected ProdutoQuantidade mapToEntity(ResultSet rs) throws SQLException {
        ProdutoQuantidade pq = new ProdutoQuantidade();
        pq.setId(rs.getLong("id"));
        pq.setVendaId(rs.getLong("venda_id"));
        pq.setProdutoId(rs.getLong("produto_id"));
        pq.setQuantidade(rs.getInt("quantidade"));
        pq.setValorUnitario(rs.getDouble("valor_unitario"));
        pq.setValorTotal(rs.getDouble("valor_total"));
        return pq;
    }

    public List<ProdutoQuantidade> buscarPorVenda(Long vendaId, ConnectionManager manager) throws SQLException {
        String sql = "SELECT * FROM produtos_quantidade WHERE venda_id = ?";
        List<ProdutoQuantidade> items = new ArrayList<>();

        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapToEntity(rs));
                }
            }
        }
        return items;
    }
}
