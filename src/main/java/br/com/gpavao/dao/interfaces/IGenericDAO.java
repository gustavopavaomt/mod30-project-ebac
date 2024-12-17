package br.com.gpavao.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface IGenericDAO<T, K> {
    T cadastrar(T entity);
    void excluir(K id);
    T atualizar(T entity);
    Optional<T> buscarPorId(K id);
    List<T> buscarTodos();
}
