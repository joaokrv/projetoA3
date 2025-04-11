import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Estoque {

    private List<Produtos> listProducts;
    private Scanner in = new Scanner(System.in);
    
    // Criando o nome do arquivo de registro de estoque
    private static final String fileName = "Registro.txt";

    public Estoque() throws IOException {
        listProducts = new ArrayList<>();
    }

    //Metodo para adicionar o produto na lista e registra-lo no arquivo Registro
    public void addProduto() {
        System.out.print("----------------------------------\nDigite o nome do produto: ");
        String name = in.nextLine();
    
        System.out.print("\nDigite o ID do produto: ");
        int id = in.nextInt();
        in.nextLine(); // Consumir a nova linha pendente
    
        System.out.print("\nDigite a quantidade do produto: ");
        int quantidade = in.nextInt();
        in.nextLine(); // Consumir a nova linha pendente
    
        System.out.print("\nDigite a descrição do produto: ");
        String desc = in.nextLine();
    
        System.out.print("\nDigite o preço de custo do produto: R$");
        double precoCusto = in.nextDouble();
    
        System.out.print("\nDigite o preço de venda: R$");
        double precoVenda = in.nextDouble();
        in.nextLine(); // Consumir a nova linha pendente
        
        double valorEstoque = quantidade * precoCusto;
        double valorLucro = quantidade * (precoVenda - precoCusto);
        
        Produtos novoProduto = new Produtos(name, id, quantidade, desc, precoCusto, precoVenda, valorEstoque, valorLucro);
    
        listProducts.add(novoProduto);
        registrarProduto(novoProduto);
    }

    //Metodo para remover o produto da lista e colocar no arquivo Registro que foi removido
    public void removeProduto(Produtos produto) throws IOException {
        // Pede a entrada de dados do usuário.
        System.out.println("Digite o ID do produto que você está buscando: ");
        if (!in.hasNextInt()) {
            throw new InputMismatchException("ID do produto inválido. Digite um número.");
        }
        int id = in.nextInt();
    
        in.nextLine(); // Consome a quebra de linha
    
        System.out.println("Digite o nome agora: ");
        if (!in.hasNextLine()) {
            throw new InputMismatchException("Nome do produto inválido. Digite algo.");
        }
        String nome = in.nextLine();
    
        // Buscar o produto na lista
        Produtos produtoEncontrado = buscarProduto(nome, id);
    
        if (produtoEncontrado != null) {
            listProducts.remove(produtoEncontrado);
    
            // Formatar a entrada de dados com formatação de números e alinhamento
            LocalDateTime dataHoraAtual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dataHoraFormatada = dataHoraAtual.format(formatter);
            String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            DecimalFormat df = new DecimalFormat("#,##0.00");
            String produtoRemovido = String.format("\n----------------------------------\nDATA: %s| HORA: %s\nDados do produto removido\n----------------------------------\nProduto: %s | ID: %d | Quantidade: %d |Descricao: %s | Preco de Custo: R$%s | Preco de Venda: R$%s |\n----------------------------------",
                    dataHoraFormatada, horaFormatada, produtoEncontrado.getName(), produtoEncontrado.getId(), produtoEncontrado.getQuantity(), produtoEncontrado.getDesc(),
                    df.format(produtoEncontrado.getCostPrice()), df.format(produtoEncontrado.getSellPrice()));
    
            try (FileWriter writer = new FileWriter(fileName, true)) {
                writer.write(produtoRemovido);
            }
            System.out.println("Produto removido com sucesso.\n----------------------------------");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    //Metodo para registrar o produto no arquivo Registro
    public void registrarProduto(Produtos produto) {

        // Formatar a entrada de dados com formatação de números e alinhamento
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String registerProduct = String.format("----------------------------------\nProduto Adicionado: %s\n Produto: %s | ID: %d | Quantidade: %d | Descricao: %s | Preco de Custo: R$%s | Preco de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n----------------------------------",
                produto.getName(), produto.getName(),produto.getId(), produto.getQuantity(), produto.getDesc(),
                df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                df.format(produto.getCostPrice() * produto.getQuantity()),
                df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));

        // Escrever os dados no arquivo
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(registerProduct + "\n");
            System.out.println("\n----------------------------------Produto registrado com sucesso.\n----------------------------------");
        } catch (Exception e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    //Busca o produto por ID ou Nome 
    public Produtos buscarProduto(String nome, int id) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        try {
            final int buscaId = id; // id final para uso no lambda
            Produtos produtoEncontrado;

            //BUSCA POR ID
            if (id > 0) {
                produtoEncontrado = listProducts.stream()
                .filter(p -> p.getId() == buscaId)
                 .findFirst()
                 .orElse(null);
                
                if (produtoEncontrado != null) {
                    
                    printProdutoInfo(produtoEncontrado, df);
                    return produtoEncontrado;

                } else {
                    System.out.println("Produto não encontrado por ID.");
                    return null;
                }
            }

            //Busca pelo nome
            for (Produtos produto : listProducts) {
                if (produto.getName().equalsIgnoreCase(nome)) {
                    printProdutoInfo(produto, df);
                    return produto;
                }
            }

            System.out.println("Produto não encontrado.");
            return null;
        } catch (NullPointerException e) {
            System.err.println("Erro: Lista de produtos vazia ou nome do produto não fornecido.");
            return null;
        }
    }

    //Metodo para dar print no dados do produto
    private void printProdutoInfo(Produtos produto, DecimalFormat df) {
        System.out.println("----------------------------------");
        System.out.println("Produto encontrado:");
        System.out.println("Nome: " + produto.getName());
        System.out.println("ID: " + produto.getId());
        System.out.println("Quantidade em Estoque: " + produto.getQuantity());
        System.out.println("Descricao: " + produto.getDesc());
        System.out.println("Preço de Custo: R$" + df.format(produto.getCostPrice()));
        System.out.println("Preço de Venda: R$" + df.format(produto.getSellPrice()));
        System.out.println("Valor em estoque: R$" + df.format(produto.getCostPrice() * produto.getQuantity()));
        System.out.println("Lucro estimado: R$" + df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));
        System.out.println("----------------------------------");
    }
    
    public void listarProdutos() {
        for (Produtos produto : listProducts) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            printProdutoInfo(produto, df);
        }
    }
}
