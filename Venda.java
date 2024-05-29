import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private LocalDate data;
    private Produto produto;
    private int quantidadeVendida;
    private final double valorTotal;

    private static final List<Venda> listaVendas = new ArrayList<>();

    public Venda(LocalDate data, Produto produto, int quantidadeVendida) {
        this.data = data;
        this.produto = produto;
        this.quantidadeVendida = quantidadeVendida;
        this.valorTotal = quantidadeVendida * produto.getPrecoVenda();
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public double getValorTotal() {
        return valorTotal;
    }
    
    public void escreverRegistroVendas(Venda venda) {
        
        // Formatar data
        DateTimeFormatter dc = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = venda.getData().format(dc);
    
        // Formatar registro de venda
        String registroVenda = "Data da venda: " + dataFormatada +
                "\nProduto: " + venda.getProduto().getNome() +
                "\nPreço de Venda: R$" + venda.getProduto().getPrecoVendaFormatado() +
                "\nQuantidade Vendida: " + venda.getQuantidadeVendida() +
                "\nValor Total: R$" + String.format("%.2f", venda.getValorTotal()) +
                "\n-------------------";
    
        // Nome do arquivo onde será armazenado
        String nomeArquivo = "Registro de Vendas.txt";
    
        // Escrever o registro no arquivo
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) { // 'true' para apêndice
            writer.write(registroVenda + "\n");
            System.out.println("Registro de venda adicionado com sucesso ao arquivo.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }

        listaVendas.add(venda);

    }
    

    public static String calcularTotal() {
        double total = 0.0;
        for (Venda venda : listaVendas) {
            total += venda.getValorTotal();
        }
        return "Total vendido: R$" + total;
    }

    public static String calcularLucro() {
        double lucroTotal = 0.0;
        for (Venda venda : listaVendas) {
            lucroTotal += (venda.getProduto().getPrecoVenda() - venda.getProduto().getPrecoCusto()) * venda.getQuantidadeVendida();
        }
        return "Lucro total: R$" + lucroTotal;
    }
}