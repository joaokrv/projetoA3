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

  // @SuppressWarnings("resource")
  // public boolean atualizarProduto(Produtos produto) throws IOException {
  //   Scanner in = new Scanner(System.in);
    
   //   try {
            // Pede a entrada de dados do usuário.
   //       System.out.println("Digite o ID do produto que você está buscando: ");
   //       if (!in.hasNextInt()) {
   //           throw new InputMismatchException("ID do produto inválido. Digite um número.");
   //       }
   //       int id = in.nextInt();
        
   //       in.nextLine(); // Consome a quebra de linha
        
   //       System.out.println("Digite o nome agora: ");
   //       if (!in.hasNextLine()) {
   //           throw new InputMismatchException("Nome do produto inválido. Digite algo.");
   //       }
   //       String nome = in.nextLine();

            // Buscar o produto na lista
   //       buscarProduto(nome, id);
            
            // Pede nome novo do produto
   //       System.out.println("\nAtualizar Produto");
   //       System.out.print("Nome: ");
   //       String novoNome = in.nextLine();

   //       System.out.print("ID do produto: ");
   //       int novoId = in.nextInt();
   //       in.nextLine(); // Consume newline character
   //   
   //       System.out.print("Descrição: ");
   //       String novaDesc = in.nextLine();
                    
   //       System.out.print("Quantidade: ");
   //       int novaQuantidade = in.nextInt();
   //       in.nextLine(); // Consume newline character
        
   //       System.out.print("Preço de custo: R$ ");
   //       double novoPrecoCusto = in.nextDouble();
   //       in.nextLine(); // Consume newline character
        
   //       System.out.print("Preço de venda: R$ ");
   //       double novoPrecoVenda = in.nextDouble();
   //       in.nextLine(); // Consume newline character

   //       double valorLucro = (novoPrecoVenda - novoPrecoCusto) * novaQuantidade;
   //       double valorTotalAtualizado = novaQuantidade * novoPrecoCusto;
            
            //Fechamento do scanner
   //       in.close();

            // Validação do novo nome
   //       if (novoNome == null) {
   //           throw new IllegalArgumentException("\nNovo nome não pode ser nulo.");
   //       }
    
            // Validação do novo ID
   //       if (novoId < 0) {
   //           throw new IllegalArgumentException("Novo ID precisa ser maior que 0.");
   //       }
    
            // Verifica se o novo ID já existe na lista
   //       for (Produtos outroProduto : listProducts) {
   //           if (!outroProduto.equals(produto) && outroProduto.getId() == novoId) {
   //               throw new IllegalArgumentException("ID já existe na lista para o produto: " + outroProduto.getName());
   //           }
   //       }
    
            // Validação da nova descrição
   //       if (novaDesc == null) {
   //           throw new IllegalArgumentException("Nova descrição não pode ser nula.");
   //       }
    
            // Validação para ver se não possui descrição igual
   //       for (Produtos outroProduto : listProducts) {
   //           if (!outroProduto.equals(produto) && outroProduto.getName().equalsIgnoreCase(novaDesc)) {
   //               throw new IllegalArgumentException("Descrição já existe na lista.");
   //           }
   //       }
    
   //       // Validação da nova quantidade
   //       if (novaQuantidade < 0) {
   //           throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
   //       }

            // Validação do novo Preço de venda
   //       if (novoPrecoVenda < produto.getCostPrice()) {
   //           throw new IllegalArgumentException("Preço de venda deve ser maior ou igual ao preço de custo.");
   //       }

            // Remove o produto antigo da lista
   //       Produtos produtoAntigo = listProducts.stream()
   //           .filter(p -> p.getId() == id)
   //           .findFirst()
   //           .orElse(null);

   //           if (produtoAntigo == null) {
   //           System.out.println("Produto não encontrado.");
   //           return false;
   //           }
                
                // Formatar a entrada de dados com formatação de números e alinhamento
   //           LocalDateTime dataHoraAtual = LocalDateTime.now();
   //           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
   //           String dataHoraFormatada = dataHoraAtual.format(formatter);
   //           String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
   //           DecimalFormat df = new DecimalFormat("#,##0.00");
   //           String produtoRemovido = String.format("Produto %s removido com sucesso | DATA: %s| HORA: %s|\n----------------------------------\nDados do produto removido\n----------------------------------\nProduto: %s | ID: %d | Quantidade: %d |Descricao: %s | Preco de Custo: R$%s | Preco de Venda: R$%s |\n----------------------------------",
   //               produtoAntigo.getName(), dataHoraFormatada, horaFormatada, produtoAntigo.getName(), produtoAntigo.getId(), produtoAntigo.getQuantity(), produtoAntigo.getDesc(),
   //               df.format(produtoAntigo.getCostPrice()), df.format(produtoAntigo.getSellPrice()));
    
   //           try (FileWriter writer = new FileWriter(fileName, true)) {
   //               writer.write(produtoRemovido);
   //           }

   //           listProducts.remove(produtoAntigo);

                // Cria um novo produto com as informações atualizadas
   //           Produtos produtoAtualizado = new Produtos(novoNome, novoId, novaQuantidade, novaDesc, novoPrecoCusto, novoPrecoVenda, valorTotalAtualizado, valorLucro);

                // Adiciona a lista e ao arquivo posteriormente
   //           listProducts.add(produtoAtualizado);

                // Formatar a entrada de dados com formatação de números e alinhamento
   //           String newProduct = String.format("\nProduto %s atualizado com sucesso | DATA: %s| HORA: %s|\n----------------------------------\nProduto: %s | ID: %d |  Quantidade: %d |Descricao: %s | Preco de Custo: R$%s | Preco de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n----------------------------------",
   //               produtoAntigo.getName() ,dataHoraFormatada, horaFormatada, produtoAtualizado.getName(), produtoAtualizado.getId(), produtoAtualizado.getQuantity(), produtoAtualizado.getDesc(),
   //               df.format(produtoAtualizado.getCostPrice()), df.format(produtoAtualizado.getSellPrice()),
   //               df.format(produtoAtualizado.getCostPrice() * produtoAtualizado.getQuantity()),
   //               df.format((produtoAtualizado.getSellPrice() - produtoAtualizado.getCostPrice()) * produtoAtualizado.getQuantity()));

                //Escreve os dados no arquivo
    //          try (FileWriter writer = new FileWriter(fileName, true)) {
    //              writer.write(newProduct);
    //          }
                // Retorna true em caso de sucesso
    //          System.out.println("Produto atualizado com sucesso.");
    //          return true;

        // Retorna falso em caso de cancelamento ou erro        
    //  } catch (IllegalArgumentException e) {
    //     System.err.println("Erro: " + e.getMessage());
    //      return false;
    //  } catch (InputMismatchException e) {
    //      System.err.println("Erro: Entrada inválida.");
    //      in.next(); // Consumir entrada inválida
    //      return false;
    //  }
    //}
    
    

    public void listarProdutos() {
        for (Produtos produto : listProducts) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            printProdutoInfo(produto, df);
        }
    }
}
