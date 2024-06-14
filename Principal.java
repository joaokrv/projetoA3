import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Estoque estoque = new Estoque();
        Produtos produto = new Produtos("Teste", 999, 1, "Teste", 01, 01, 0, 0);
        int opcao;
        boolean continuar = true;

        do {
            System.out.println("\nMenu Principal:\n-------------------------");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Remover Produto");
            System.out.println("3. Listar Produtos");
            System.out.println("4. Atualizar Produto");
            System.out.println("5. Sair\n-------------------------");
            System.out.print("Digite sua opção > ");
            String opcaoStr = in.next();

            try {
                opcao = Integer.parseInt(opcaoStr);
            } catch (NumberFormatException  e) {
                System.out.println("Opção inválida. Digite um número inteiro.");
                continue;
            }

            switch (opcao) {
                case 1:
                    try {
                        estoque.addProduto();
                    } catch (InputMismatchException  e) {
                        System.err.println("Erro ao adicionar produto: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        estoque.removeProduto(produto);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao remover produto: " + e.getMessage());
                    }
                    break;
                case 3:
                    estoque.listarProdutos();
                    break;
                case 4:
                    try {
                        estoque.atualizarProduto(produto);
                    } catch (IOException e) {
                        System.err.println("Erro ao atualizar produto: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            if (opcao == 5) {
                continuar = false;
              }

        } while (continuar);

        in.close();
    }
}
