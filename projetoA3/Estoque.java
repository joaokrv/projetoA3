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
    public void addProduto(Produtos produto) {
        System.out.print("Digite o nome do produto: ");
        produto.setName(in.nextLine());

        System.out.print("\nDigite o ID do produto: ");
        produto.setId(in.nextInt());

        System.out.print("\nDigite a quantidade do produto: ");
        produto.setQuantity(in.nextInt());

        System.out.print("\nDigite a descrição do produto: ");
        produto.setDesc(in.nextLine());

        System.out.print("\nDigite o preço de custo do produto: R$");
        produto.setCostPrice(in.nextDouble());

        System.out.print("\nDigite o preço de venda: R$");
        produto.setSellPrice(in.nextDouble());

        listProducts.add(produto);
        registrarProduto(produto);
    }

    //Metodo para remover o produto da lista e colocar no arquivo Registro que foi removido
    public void removeProduto(Produtos produto) throws IOException {
        System.out.println("Digite o Nome e ID do produto que você deseja remover: ");
        String nome = in.nextLine();
        int id = in.nextInt();
        
        // Buscar o produto na lista
        buscarProduto(nome, id);
        
        listProducts.remove(produto);

        // Formatar a entrada de dados com formatação de números e alinhamento
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataHoraAtual.format(formatter);
        String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String produtoRemovido = String.format("Nome do produto %s removidio | DATA: %s| HORA: %s|\n---Dados do produto removido---\nProduto: %s | ID: %d | Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s |\n--------------------",
            dataHoraFormatada, horaFormatada, produto.getName(), produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
            df.format(produto.getCostPrice()), df.format(produto.getSellPrice()));

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(produtoRemovido);
        }
    }

    //Metodo para registrar o produto no arquivo Registro
    public void registrarProduto(Produtos produto) {

        // Formatar a entrada de dados com formatação de números e alinhamento
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String registerProduct = String.format("Produto: %s | ID: %d | Quantidade: %d | Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n",
                produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                df.format(produto.getCostPrice() * produto.getQuantity()),
                df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));

        // Escrever os dados no arquivo
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(registerProduct + "\n");
            System.out.println("Produto registrado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    //Busca o produto por ID ou Nome 
    public Produtos buscarProduto(String nome, int id) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        try {
            //BUSCA POR ID
            if (id > 0) {
                Produtos produtoEncontrado = listProducts.stream()
                 .filter(p -> p.getId() == id)
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
        System.out.println("Produto encontrado:");
        System.out.println("Nome: " + produto.getName());
        System.out.println("ID: " + produto.getId());
        System.out.println("Quantidade em Estoque: " + produto.getQuantity());
        System.out.println("Descricao: " + produto.getDesc());
        System.out.println("Preço de Custo: R$" + df.format(produto.getCostPrice()));
        System.out.println("Preço de Venda: R$" + df.format(produto.getSellPrice()));
        System.out.println("Valor em estoque: R$" + df.format(produto.getCostPrice() * produto.getQuantity()));
        System.out.println("Lucro estimado: R$" + df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));
    }


    //Atualiza o ID tanto na lista quanto no arquivo Registro, printando uma mensagem e colocando todos seus
    public boolean atualizarID(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();
    
            // Buscar o produto na lista
            buscarProduto(nome, id);
    
            // Pede o novo ID
            System.out.println("Digite o novo ID: ");
            int novoId = in.nextInt();
    
            // Validação do novo ID
            if (novoId < 0) {
                throw new IllegalArgumentException("Novo ID precisa ser maior que 0.");
            }
    
            // Verifica se o novo ID já existe na lista
            for (Produtos outroProduto : listProducts) {
                if (!outroProduto.equals(produto) && outroProduto.getId() == novoId) {
                    throw new IllegalArgumentException("ID já existe na lista para o produto: " + outroProduto.getName());
                }
            }
    
            // Confirmação do usuário
            System.out.println("Deseja realmente atualizar o ID do produto para " + novoId + "? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();
    
            if (confirmacao.equals("S")) {
                // Atualiza o ID do produto
                produto.setId(novoId);
    
                // Atualiza o ID do produto na lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);
    
                // Imprime mensagem sinalizando o sucesso da ação.
                System.out.println("ID do produto atualizado com sucesso.");
    
                // Formata a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("ID do produto %s atualizado com sucesso | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d | Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));
    
                // Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }
    
                return true;
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida. Digite um número inteiro.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public boolean atualizarNome(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();
    
            // Buscar o produto na lista
            buscarProduto(nome, id);
    
            // Pede nome novo do produto
            System.out.println("Digite o novo nome: ");
            String novoNome = in.nextLine();
    
            // Validação do novo nome
            if (novoNome == null) {
                throw new IllegalArgumentException("Novo nome não pode ser nulo.");
            }
    
            // Validação para ver se não possui o nome igual
            for (Produtos outroProduto : listProducts) {
                if (!outroProduto.equals(produto) && outroProduto.getName().equalsIgnoreCase(novoNome)) {
                    throw new IllegalArgumentException("Nome já existe na lista.");
                }
            }
    
            // Confirmação do usuário
            System.out.println("Deseja realmente atualizar o nome do produto para " + novoNome + "? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();
    
            if (confirmacao.equals("S")) {
                // Atualiza o nome do produto
                produto.setName(novoNome);
    
                // Atualiza o nome do produto na lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);
    
                // Imprime mensagem sinalizando o sucesso da ação.
                System.out.println("Nome do produto atualizado com sucesso.");
    
                // Formatar a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("Nome do produto %s atualizado | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d | Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));
    
                // Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }
    
                // Retorna true em caso de sucesso
                return true;

            // Retorna falso em caso de cancelamento ou erro
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }
    
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida. Digite um nome válido.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public boolean atualizarDesc(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();
    
            // Buscar o produto na lista
            buscarProduto(nome, id);
    
            // Pede nova descrição do produto
            System.out.println("Digite a nova descrição: ");
            String novaDesc = in.nextLine();
    
            // Validação da nova descrição
            if (novaDesc == null) {
                throw new IllegalArgumentException("Nova descrição não pode ser nula.");
            }
    
            // Validação para ver se não possui descrição igual
            for (Produtos outroProduto : listProducts) {
                if (!outroProduto.equals(produto) && outroProduto.getName().equalsIgnoreCase(novaDesc)) {
                    throw new IllegalArgumentException("Descrição já existe na lista.");
                }
            }
    
            // Confirmação do usuário
            System.out.println("Deseja realmente atualizar a descrição do produto para " + novaDesc + "? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();
    
            if (confirmacao.equals("S")) {
                // Atualiza o nome do produto
                produto.setName(novaDesc);
    
                // Atualiza o nome do produto na lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);
    
                // Imprime mensagem sinalizando o sucesso da ação.
                System.out.println("Descrição do produto atualizado com sucesso.");
    
                // Formatar a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("Descrição do produto %s atualizado | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d | Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));
    
                // Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }
    
                // Retorna true em caso de sucesso
                return true;

            // Retorna falso em caso de cancelamento ou erro
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }
    
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida. Digite um nome válido.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public boolean atualizarQuantidade(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();

            // Buscar o produto na lista
            buscarProduto(nome, id);

            // Pede nova quantidade do produto
            System.out.println("Digite a nova quantidade: ");
            int novaQuantidade = in.nextInt();
    
            // Validação da nova quantidade
            if (novaQuantidade < 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
            }
            
            // Confirmação do usuario
            System.out.println("Deseja realmente atualizar a quantidade do produto para " + novaQuantidade + "? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();

            if (confirmacao.equals("S")) {
            
                // Atualiza a quantidade do produto
                produto.setQuantity(novaQuantidade);
                
                // Remove caso a quantidade for zero
                if (novaQuantidade == 0) {
                    removeProduto(produto);
                }
            
                // Atualiza a lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);

                // Formatar a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("Quantidade do produto %s atualizado com sucesso | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d |  Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));

                //Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }            

                // Retorna true em caso de sucesso
                return true;

            // Retorna falso em caso de cancelamento ou erro
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida. Digite uma quantidade válida.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public boolean atualizarPrecoCusto(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();

            // Buscar o produto na lista
            buscarProduto(nome, id);

            // Pede o preço novo do produto
            System.out.println("Digite o novo preço de custo: ");
            double novoPrecoCusto = in.nextDouble();
    
            // Validação do novo Preço de Custo
            if (novoPrecoCusto > produto.getSellPrice()) {
                throw new IllegalArgumentException("Preço de custo deve ser menor ou igual ao preço de venda.");
            }
            
            // Confirmação do usuario
            System.out.println("Deseja realmente atualizar a quantidade do produto para " + novoPrecoCusto + "? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();

            if (confirmacao.equals("S")) {

                // Atualiza a quantidade do produto
                produto.setCostPrice(novoPrecoCusto);

                // Atualiza a lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);

                // Formatar a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("Preco de Custo do produto %s atualizado com sucesso | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d |  Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));

                //Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }
            
                // Retorna true em caso de sucesso
                return true;
            
                // Retorna falso em caso de cancelamento ou erro
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }
    
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida. Digite um preço válido.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public boolean atualizarPrecoVenda(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();

            // Buscar o produto na lista
            buscarProduto(nome, id);

            // Pede o preço novo do produto
            System.out.println("Digite a novo preço de venda: ");
            double novoPrecoVenda = in.nextDouble();
    
            // Validação do novo Preço de venda
            if (novoPrecoVenda < produto.getCostPrice()) {
                throw new IllegalArgumentException("Preço de venda deve ser maior ou igual ao preço de venda.");
            }
    
            // Confirmação do usuario
            System.out.println("Deseja realmente atualizar a quantidade do produto para " + novoPrecoVenda + "? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();

            if (confirmacao.equals("S")) {

                // Atualiza a Preço de venda do produto
                produto.setSellPrice(novoPrecoVenda);

                // Atualiza a lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);

                // Formatar a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("Preco de Venda do produto %s atualizado com sucesso | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d |  Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));

                //Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }

                // Retorna true em caso de sucesso
                return true;
            
            // Retorna falso em caso de cancelamento ou erro
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }           
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida. Digite um preço válido.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public boolean atualizarProduto(Produtos produto) throws IOException {
        Scanner in = new Scanner(System.in);
    
        try {
            // Pede a entrada de dados do usuário.
            System.out.println("Digite o Nome e ID do produto que você está buscando: ");
            String nome = in.nextLine();
            int id = in.nextInt();

            // Buscar o produto na lista
            buscarProduto(nome, id);

            // Pede o novo ID
            System.out.println("Digite o novo ID: ");
            int novoId = in.nextInt();
    
            // Validação do novo ID
            if (novoId < 0) {
                throw new IllegalArgumentException("Novo ID precisa ser maior que 0.");
            }
    
            // Verifica se o novo ID já existe na lista
            for (Produtos outroProduto : listProducts) {
                if (!outroProduto.equals(produto) && outroProduto.getId() == novoId) {
                    throw new IllegalArgumentException("ID já existe na lista para o produto: " + outroProduto.getName());
                }
            }

            // Pede nome novo do produto
            System.out.println("Digite o novo nome: ");
            String novoNome = in.nextLine();
            
            // Validação do novo nome
            if (novoNome == null) {
                throw new IllegalArgumentException("Novo nome não pode ser nulo.");
            }

            // Pede nova descrição do produto
            System.out.println("Digite a nova descrição: ");
            String novaDesc = in.nextLine();
    
            // Validação da nova descrição
            if (novaDesc == null) {
                throw new IllegalArgumentException("Nova descrição não pode ser nula.");
            }
    
            // Validação para ver se não possui descrição igual
            for (Produtos outroProduto : listProducts) {
                if (!outroProduto.equals(produto) && outroProduto.getName().equalsIgnoreCase(novaDesc)) {
                    throw new IllegalArgumentException("Descrição já existe na lista.");
                }
            }

            // Pede nova quantidade do produto
            System.out.println("Digite a nova quantidade: ");
            int novaQuantidade = in.nextInt();
    
            // Validação da nova quantidade
            if (novaQuantidade < 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
            }

            // Pede o preço novo do produto
            System.out.println("Digite o novo preço de custo: ");
            double novoPrecoCusto = in.nextDouble();
    
            // Validação do novo Preço de Custo
            if (novoPrecoCusto > produto.getSellPrice()) {
                throw new IllegalArgumentException("Preço de custo deve ser menor ou igual ao preço de venda.");
            }               

            // Pede o preço novo do produto
            System.out.println("Digite a novo preço de venda: ");
            double novoPrecoVenda = in.nextDouble();
    
            // Validação do novo Preço de venda
            if (novoPrecoVenda < produto.getCostPrice()) {
                throw new IllegalArgumentException("Preço de venda deve ser maior ou igual ao preço de venda.");
            }
    
            // Confirmação do usuario
            System.out.println("Deseja realmente atualizar as informações do produto? (S/N): ");
            String confirmacao = in.nextLine().toUpperCase();

            if (confirmacao.equals("S")) {

                // Atualiza as informações do produto
                produto.setId(novoId);
                produto.setName(novoNome);
                produto.setDesc(novaDesc);
                produto.setCostPrice(novoPrecoCusto);
                produto.setSellPrice(novoPrecoVenda);

                // Atualiza a lista
                int index = listProducts.indexOf(produto);
                listProducts.set(index, produto);

                // Formatar a entrada de dados com formatação de números e alinhamento
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                String horaFormatada = dataHoraAtual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String newProduct = String.format("Produto %s atualizado com sucesso | DATA: %s| HORA: %s|\n--------------------\nProduto: %s | ID: %d |  Quantidade: %d |Descrição: %s | Preço de Custo: R$%s | Preço de Venda: R$%s | Valor total em estoque: R$%s | Lucro estimado: R$%s\n--------------------",
                    produto.getName() ,dataHoraFormatada, horaFormatada, produto.getName(), produto.getId(), produto.getQuantity(), produto.getDesc(),
                    df.format(produto.getCostPrice()), df.format(produto.getSellPrice()),
                    df.format(produto.getCostPrice() * produto.getQuantity()),
                    df.format((produto.getSellPrice() - produto.getCostPrice()) * produto.getQuantity()));

                //Escreve os dados no arquivo
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(newProduct);
                }

                // Retorna true em caso de sucesso
                return true;
            
            // Retorna falso em caso de cancelamento ou erro
            } else if (confirmacao.equals("N")) {
                System.out.println("Atualização cancelada.");
                return false;
            } else {
                System.err.println("Entrada inválida. Digite S (sim) ou N (não).");
                return false;
            }           
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida.");
            in.next(); // Consumir entrada inválida
            return false;
        } finally {
            in.close(); // Fechar o Scanner para liberar recursos
        }
    }

    public void listarProdutos() {
        for (Produtos produto : listProducts) {
            System.out.printf("ID: %d | Nome: %s | Quantidade: %d\n------------\n",
                    produto.getId(), produto.getName(), produto.getQuantity());
        }
    }
}