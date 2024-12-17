package br.com.gpavao.domain;

import br.com.gpavao.annotations.Column;
import br.com.gpavao.annotations.Table;

@Table(name = "produtos_quantidade")
public class ProdutoQuantidade {

    @Column(name = "id")
    private Long id;

    @Column(name = "venda_id")
    private Long vendaId;

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "valor_unitario")
    private Double valorUnitario;

    @Column(name = "valor_total")
    private Double valorTotal;

    private Produto produto;
    private Venda venda;

    public ProdutoQuantidade() {}

    public ProdutoQuantidade(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.produtoId = produto.getId();
        this.quantidade = quantidade;
        this.valorUnitario = produto.getPreco();
        this.calcularValorTotal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularValorTotal();
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
        calcularValorTotal();
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        if (produto != null) {
            this.produtoId = produto.getId();
            this.valorUnitario = produto.getPreco();
            calcularValorTotal();
        }
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
        if (venda != null) {
            this.vendaId = venda.getId();
        }
    }

    // Métodos de negócio
    private void calcularValorTotal() {
        if (this.quantidade != null && this.valorUnitario != null) {
            this.valorTotal = this.quantidade * this.valorUnitario;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoQuantidade that = (ProdutoQuantidade) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProdutoQuantidade{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
