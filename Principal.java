import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException {
    
        Scanner in = new Scanner(System.in);
        Estoque estoque = new Estoque();
        Produtos produto = new Produtos("Teste", 999, 1, "Teste", 01, 01, 0, 0);
        int opcao;
        
        do {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Remover Produto");
            System.out.println("3. Buscar Produto");
            System.out.println("4. Listar Produtos");
            System.out.println("5. Atualizar Produto");
            System.out.println("6. Sair");
            System.out.print("Digite sua opção: ");

            opcao = in.nextInt();

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
                    estoque.atualizarProduto(produto);
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 6);
            
        in.close();
    }
}
