import java.util.Scanner;   

public class Main {
    
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int escolha = -1; // Variável para armazenar a escolha do usuário

        while(escolha != 0){
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Criar pasta");
            System.out.println("2 - Criar pin");
            System.out.println("3 - Adicionar pin à pasta");
            System.out.println("4 - Exibir histórico de ações");
            System.out.println("0 - Sair");

            escolha = Scanner(System.in).nextInt();

            switch (escolha) {
                case 1:
                    // Lógica para criar pasta
                    
                    break;
                case 2:
                    // Lógica para criar pin
                    break;
                case 3:
                    // Lógica para adicionar pin à pasta
                    break;
                case 4:
                    // Lógica para exibir histórico de ações
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
