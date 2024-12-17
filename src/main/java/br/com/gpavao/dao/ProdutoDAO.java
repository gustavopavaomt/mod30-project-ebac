package br.com.gpavao.dao;

import br.com.gpavao.domain.Produto;
import br.com.gpavao.jdbc.ConnectionManager;

import java.sql.*;

public class ProdutoDAO extends GenericDAO<Produto, Long> {

    public ProdutoDAO() {
        super(Produto.class);
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO produtos (nome, preco, quantidade_estoque) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE produtos SET nome = ?, descricao = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParams(PreparedStatement stmt, Produto produto) throws SQLException {
        stmt.setString(1, produto.getNome());
        stmt.setDouble(2, produto.getPreco());
        stmt.setInt(3, produto.getQuantidadeEstoque());
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Produto produto) throws SQLException {
        stmt.setString(1, produto.getNome());
        stmt.setDouble(2, produto.getPreco());
        stmt.setInt(3, produto.getQuantidadeEstoque());
        stmt.setLong(4, produto.getId());
    }

    @Override
    protected Produto mapToEntity(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        return produto;
    }

    public boolean atualizarEstoque(Long produtoId, int quantidade, ConnectionManager manager) throws SQLException {
        String sql = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ? AND quantidade_estoque >= ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setLong(2, produtoId);
            stmt.setInt(3, quantidade);
            return stmt.executeUpdate() > 0;
        }
    }
}