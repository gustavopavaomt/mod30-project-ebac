package br.com.gpavao.dao;
import br.com.gpavao.domain.Cliente;
import br.com.gpavao.jdbc.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class ClienteDAO extends GenericDAO<Cliente, Long> {

    public ClienteDAO() {
        super(Cliente.class);
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO clientes (nome, email, cpf) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE clientes SET nome = ?, email = ?, cpf = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParams(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getEmail());
        stmt.setString(3, cliente.getCpf());
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getEmail());
        stmt.setString(3, cliente.getCpf());
        stmt.setLong(4, cliente.getId());
    }

    @Override
    protected Cliente mapToEntity(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCpf(rs.getString("cpf"));
        return cliente;
    }

    public Optional<Cliente> buscarPorCpf(String cpf, ConnectionManager manager) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
                return Optional.empty();
            }
        }
    }
}