package br.com.gpavao.domain;

import br.com.gpavao.annotations.Column;
import br.com.gpavao.annotations.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "vendas")
public class Venda {
    @Column(name = "id")
    private Long id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @Column(name = "valor_total")
    private Double valorTotal;

    private Cliente cliente;
    private List<ProdutoQuantidade> items = new ArrayList<>();

    public Venda() {
        this.dataVenda = LocalDateTime.now();
        this.valorTotal = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            this.clienteId = cliente.getId();
        }
    }

    public List<ProdutoQuantidade> getItems() {
        return items;
    }

    public void setItems(List<ProdutoQuantidade> items) {
        this.items = items != null ? items : new ArrayList<>();
        this.items.forEach(item -> item.setVenda(this));
        calcularValorTotal();
    }

    // Métodos de negócio
    public void addItem(ProdutoQuantidade item) {
        if (item != null) {
            items.add(item);
            item.setVenda(this);
            calcularValorTotal();
        }
    }

    public void removeItem(ProdutoQuantidade item) {
        if (items.remove(item)) {
            item.setVenda(null);
            calcularValorTotal();
        }
    }

    private void calcularValorTotal() {
        this.valorTotal = items.stream()
                .mapToDouble(ProdutoQuantidade::getValorTotal)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return id != null && id.equals(venda.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", dataVenda=" + dataVenda +
                ", valorTotal=" + valorTotal +
                ", quantidadeItems=" + (items != null ? items.size() : 0) +
                '}';
    }
}