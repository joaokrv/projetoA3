import java.text.DecimalFormat;

public class Produto {
    private String nome;
    private double precoCusto;
    private double precoVenda;
    private int quantidade;
    private String descricao;

    public Produto(String nome, double precoCusto, double precoVenda, int quantidade, String descricao) {
        this.nome = nome;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    DecimalFormat df = new DecimalFormat("00.00");

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getPrecoVendaFormatado() {
        return df.format(precoVenda);
    }

    public String getPrecoCustoFormatado() {
        return df.format(precoCusto);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Produto: " + nome +
               "\nPreço de Venda: R$" + getPrecoVendaFormatado() +
               "\nPreço de Custo: R$" + getPrecoCustoFormatado() +
               "\nQuantidade: " + quantidade +
               "\nDescrição: " + descricao +
               "\n-------------------";
    }
}