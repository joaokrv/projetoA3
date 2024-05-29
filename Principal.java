import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Sistema s = new Sistema();

        int opcao;
        do {
            System.out.println("\nEscolha uma opção do menu -->\n-----------\n1 - Estoque\n2 - Vendas\n0 - Sair");
            opcao = in.nextInt();

            switch (opcao) {
                case 1:
                    s.menuEstoque();
                    break;
                case 2:
                    s.menuVenda();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        in.close();
    
    }
}
