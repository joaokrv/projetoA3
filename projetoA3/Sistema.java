import java.io.IOException;
import java.util.Scanner;

public class Sistema {
    private Estoque estoque;
    private Scanner in;
    private Produtos produto;

    public void sistema() throws IOException {
        estoque = new Estoque();
        in = new Scanner(System.in);
    }

    public void iniciar() throws IOException {
        exibirMenu();
        int opcao = in.nextInt();
        in.nextLine(); // Consumir quebra de linha

        do {
            switch (opcao) {
                case 1:
                    estoque.addProduto(produto);
                    break;
                case 2:
                    estoque.removeProduto(produto);
                    break;
                case 3:
                    estoque.buscarProduto(produto.getName(), produto.getId());
                    break;
                case 4:
                    estoque.listarProdutos();
                    break;
                case 5:
                    menuAtualizar();
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.err.println("Opção inválida!");
            }
        } while (opcao != 6);
    }

    private void menuAtualizar() throws IOException {
        int opcao = in.nextInt();
        in.nextLine(); // Consumir quebra de linha

        do {
            switch (opcao) {
                case 1:
                    System.out.println("Atualização geral");
                    estoque.atualizarProduto(produto);
                case 2:
                    System.out.println("Atualizar ID");
                    estoque.atualizarID(produto);
                case 3:
                    System.out.println("Atualizar Nome do produto");
                    estoque.atualizarNome(produto);
                case 4:
                    System.out.println("Atualizar quantidade");
                    estoque.atualizarQuantidade(produto);
                case 5:
                    System.out.println("Atualizar descrição");
                    estoque.atualizarDesc(produto);
                case 6:
                    System.out.println("Atualizar Preço de Custo");
                    estoque.atualizarPrecoCusto(produto);
                case 7:
                    System.out.println("Atualizar Preço de Venda");
                    estoque.atualizarPrecoVenda(produto);
                case 8:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.err.println("Opção inválida!");
            }
        } while (opcao != 8);
    }

    private void exibirMenu() {
        System.out.println("\nMenu Principal:");
        System.out.println("1. Adicionar Produto");
        System.out.println("2. Remover Produto");
        System.out.println("3. Buscar Produto");
        System.out.println("4. Listar Produtos");
        System.out.println("5. Atualizar Produto");
        System.out.println("6. Sair");
        System.out.print("Digite sua opção: ");
    }
}
