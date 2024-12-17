package br.com.gpavao.dao;


import br.com.gpavao.annotations.Column;
import br.com.gpavao.annotations.Table;
import br.com.gpavao.jdbc.ConnectionManager;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class GenericDAO<T, K> {
    protected final Class<T> entityClass;
    protected final String tableName;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = entityClass.getAnnotation(Table.class).name();
    }

    protected abstract String getInsertSQL();
    protected abstract String getUpdateSQL();
    protected abstract void setInsertParams(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateParams(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract T mapToEntity(ResultSet rs) throws SQLException;

    public T cadastrar(T entity, ConnectionManager manager) throws SQLException {
        String sql = getInsertSQL();
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParams(stmt, entity);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    setEntityId(entity, rs.getLong(1));
                }
            }
            return entity;
        }
    }

    public void excluir(K id, ConnectionManager manager) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    public T atualizar(T entity, ConnectionManager manager) throws SQLException {
        String sql = getUpdateSQL();
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            setUpdateParams(stmt, entity);
            stmt.executeUpdate();
            return entity;
        }
    }

    public Optional<T> buscarPorId(K id, ConnectionManager manager) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
                return Optional.empty();
            }
        }
    }

    public List<T> buscarTodos(ConnectionManager manager) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        List<T> results = new ArrayList<>();

        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(mapToEntity(rs));
            }
        }
        return results;
    }

    protected void setEntityId(T entity, Long id) {
        try {
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class) &&
                        field.getAnnotation(Column.class).name().equals("id")) {
                    field.setAccessible(true);
                    field.set(entity, id);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro ao definir ID da entidade", e);
        }
    }
}
