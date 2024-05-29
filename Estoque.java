import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private List<Produto> listaProdutos;

    public Estoque() {
        listaProdutos = new ArrayList<>();
    }

    public void registrarProdutos(Produto produto){

        // Formatar registro de venda
        String registroProdutos =
                "\nProduto: " + produto.getNome() +
                "\nPreço de Venda: R$" + produto.getPrecoVendaFormatado() +
                "\nPreço de Custo: R$" + produto.getPrecoCustoFormatado() +
                "\nQuantidade: " + produto.getQuantidade() +
                "\nDescrição: " + produto.getDescricao() +
                "\n-------------------";

        // Nome do arquivo onde será armazenado
        String nomeArquivo = "Registro Estoque.txt";

        // Escrever o registro no arquivo
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) { // 'true' para apêndice
            writer.write(registroProdutos + "\n");
            System.out.println("Registro adicionado com sucesso ao arquivo.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void atualizarArquivoRegistro() {

        String nomeArquivo = "Registro Estoque.txt";

        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (Produto produto : listaProdutos) {
                String registroProduto =
                        "\nProduto: " + produto.getNome() +
                        "\nPreço de Venda: R$" + produto.getPrecoVendaFormatado() +
                        "\nPreço de Custo: R$" + produto.getPrecoCustoFormatado() +
                        "\nDescrição: " + produto.getDescricao() +
                        "\n-------------------";
                writer.write(registroProduto + "\n");
            }
            System.out.println("Arquivo de registro atualizado com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void adicionarProduto(Produto produto) {
        listaProdutos.add(produto);
    }

    public void removerProdutoPorNome(String nome) {
        Produto produtoRemovido = null;
        for (Produto produto : listaProdutos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                produtoRemovido = produto;
                break; // Quando encontrar o produto, sai do loop
            }
        }
    
        if (produtoRemovido != null) {
            listaProdutos.remove(produtoRemovido);
            atualizarArquivoRegistro();
            System.out.println("Produto removido.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
    

    public Produto buscarProduto(String nome) {
        for (Produto produto : listaProdutos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Produto encontrado:");
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Preço de Venda: R$" + produto.getPrecoVendaFormatado());
                System.out.println("Quantidade em Estoque: " + produto.getQuantidade());
                return produto;
            }
        }
        System.out.println("Produto não encontrado.");
        return null; // Caso produto não seja encontrado
    }

    //  Método para listar os produtos
    //  public String listarProdutos() {
    //    StringBuilder produtosString = new StringBuilder();        
    //    for (Produto produto : listaProdutos) {
    //        produtosString.append(produto.toString()).append("\n");
    //    }
    //    return produtosString.toString();        
    // }

    public List<Produto> listarProdutos() {
        return listaProdutos;
    }
}