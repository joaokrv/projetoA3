import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Sistema {

    private Scanner in = new Scanner(System.in);
    private Estoque estoque;

    public Sistema() {
        estoque = new Estoque();
    }

    public void menuEstoque() {
        int opcao;
        do {
            System.out.println("Escolha uma opção:\n1 - Adicionar Produto\n2 - Remover Produto\n3 - Listar produtos\n0 - Voltar");
            opcao = in.nextInt();

            switch (opcao) {
                case 1:
                    
                    System.out.println("Digite o nome do produto: ");
                    String nomeProduto = in.next();
                    System.out.println("Digite o preço de custo: ");
                    double precoCusto = in.nextDouble();
                    System.out.println("Digite o preço de venda: ");
                    double precoVenda = in.nextDouble();     
                    System.out.println("Digite a quantidade em estoque: ");
                    int quantidadeEstoque = in.nextInt();                    
                    System.out.println("Digite uma descrição (tamanho, cor, etc): ");
                    String descricao = in.next();
                    
                    // Crie a instância do Produto com os dados inseridos
                    Produto novoProduto = new Produto(nomeProduto, precoCusto, precoVenda, quantidadeEstoque, descricao);

                    // Adicione o produto à lista do estoque
                    estoque.adicionarProduto(novoProduto);

                    // Registre o produto no estoque
                    estoque.registrarProdutos(novoProduto);

                    break;
                case 2:
                    System.out.println("Digite o nome do produto a ser removido: ");
                    String nomeProdutoRemover = in.next();
                    estoque.removerProdutoPorNome(nomeProdutoRemover);
                    break;
                case 3:
                    System.out.println("Produtos em estoque:");
                    System.out.println(estoque.listarProdutos());                
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    public void menuVenda() {
        int opcao;
        do {
            System.out.println("Escolha uma opção:\n1 - Registrar Venda\n2 - Calcular Total de Vendas\n3 - Calcular Lucro\n0 - Voltar");
            opcao = in.nextInt();
    
            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome do produto vendido: ");
                    String nomeProduto = in.next();
                    Produto produto = estoque.buscarProduto(nomeProduto);
                        
                    if (produto == null) {
                        System.out.println("Produto não encontrado no estoque.");
                        return;
                    }
                
                    System.out.println("Digite a quantidade vendida: ");
                    int quantidadeVendida = in.nextInt();
                
                    // Solicitar a data da venda
                    System.out.println("Digite a data da venda (no formato dd/MM/yyyy): ");
                    String dataVendaStr = in.next();
                    LocalDate data;
                    try {
                        DateTimeFormatter dc = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        data = LocalDate.parse(dataVendaStr, dc);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
                        break;
                    }

                    Venda vendaNova = new Venda(data, produto, quantidadeVendida);
                    vendaNova.escreverRegistroVendas(vendaNova);

                    break;
                case 2:
                    System.out.println(Venda.calcularTotal());
                    break;
                case 3:
                    System.out.println(Venda.calcularLucro());
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
    
}