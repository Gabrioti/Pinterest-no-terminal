import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * View textual que interage com o usuário e coordena a RedeSocial.
 */
public class Menu {
    private RedeSocial redeSocial;
    private Scanner scanner;

    public static void limparTela() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    public Menu() {
        this.redeSocial = new RedeSocial();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        limparTela();
        System.out.println("=== Bem-vindo à Rede Social Console ===");
        boolean executando = true;

        while (executando) {
            try {
                if (redeSocial.getUsuarioLogado() == null) {
                    executando = menuDeslogado();
                } else {
                    menuLogado();
                }
            } catch (Exception e) {
                System.out.println("Erro inesperado. Tente novamente.");
            }
        }
        System.out.println("Encerrando a aplicação. Até logo!");
        scanner.close();
    }

    private boolean menuDeslogado() {
        limparTela();
        System.out.println("[1] Cadastrar");
        System.out.println("[2] Fazer Login");
        System.out.println("[0] Sair");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();

        switch (opcao) {
            case 1:
                limparTela();
                System.out.print("Digite seu nome: ");
                String nome = scanner.nextLine();
                System.out.print("Digite seu login: ");
                String login = scanner.nextLine();
                System.out.print("Digite sua senha: ");
                String senha = scanner.nextLine();
                
                if (redeSocial.cadastrarUsuario(nome, login, senha)) {
                    limparTela();
                    System.out.println("Cadastro realizado com sucesso!");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    System.out.println("Erro: Login já existe.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 2:
                limparTela();
                System.out.print("Login: ");
                String log = scanner.nextLine();
                System.out.print("Senha: ");
                String sen = scanner.nextLine();
                
                if (redeSocial.fazerLogin(log, sen)) {
                    limparTela();
                    System.out.println("Login efetuado! Bem-vindo, " + redeSocial.getUsuarioLogado().getNome());
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    limparTela();
                    System.out.println("Erro: Credenciais inválidas.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 0:
                return false;
            default:
                limparTela();
                System.out.println("Opção inválida.");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
        return true;
    }

    private void menuLogado() {
        limparTela();
        Usuario eu = redeSocial.getUsuarioLogado();
        System.out.println("\n--- MENU DE " + eu.getNome().toUpperCase() + " ---");
        System.out.println("[1] Ver Feed");
        System.out.println("[2] Criar Postagem (Com ou sem Arte ASCII)");
        System.out.println("[3] Curtir / Descurtir Postagem");
        System.out.println("[4] Comentar em Postagem");
        System.out.println("[5] Adicionar Amigo");
        System.out.println("[6] Remover Amigo");
        System.out.println("[7] Editar Perfil");
        System.out.println("[8] Excluir Minha Postagem");
        System.out.println("[0] Fazer Logout");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();

        switch (opcao) {
            case 1:
                limparTela();
                exibirFeed();
                scanner.nextLine(); // Pausa para o usuário ler o feed
                break;
            case 2:
                limparTela();
                System.out.print("Digite o texto da sua publicação:\n> ");
                String texto = scanner.nextLine();

                limparTela();
                System.out.print("Deseja adicionar uma arte ASCII? (S/N): ");
                String desejaArte = scanner.nextLine().trim();
                String arteAscii = "";
                
                if (desejaArte.equalsIgnoreCase("S")) {
                    limparTela();
                    System.out.println("Como deseja inserir a arte?");
                    System.out.println("[1] Colar diretamente no terminal");
                    System.out.println("[2] Carregar de um arquivo na Área de Trabalho (pasta 'Art')");
                    System.out.print("Escolha: ");
                    int opcaoArte = lerInteiro();

                    if (opcaoArte == 1) {
                        limparTela();
                        System.out.println("Cole ou desenhe sua arte ASCII abaixo.");
                        System.out.println("IMPORTANTE: Para finalizar a arte, digite '//FIM' em uma linha vazia e aperte ENTER.");
                        
                        StringBuilder builderArte = new StringBuilder();
                        while (true) {
                            String linha = scanner.nextLine();
                            if (linha.equals("//FIM")) {
                                System.out.println("Arte ASCII capturada com sucesso!");
                                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                                break;
                            }
                            builderArte.append(linha).append("\n");
                        }
                        arteAscii = builderArte.toString();
                    } 
                    else if (opcaoArte == 2) {
                        limparTela();
                        System.out.print("Digite o nome do arquivo com a extensão (ex: nave.txt): ");
                        String nomeArquivo = scanner.nextLine().trim();
                        arteAscii = lerArteDeArquivo(nomeArquivo);
                    } 
                    else {
                        limparTela();
                        System.out.println("Opção de arte inválida. A postagem será criada sem arte.");
                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    }
                }
                
                eu.adicionarPostagem(new Postagem(texto, eu, arteAscii));
                limparTela();
                System.out.println("Postagem criada com sucesso!");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                break;
            case 3:
                limparTela();
                exibirFeed();
                System.out.print("Digite o ID da postagem que deseja curtir/descurtir: ");
                int idCurtir = lerInteiro();
                Postagem postCurtir = redeSocial.buscarPostagemNoFeed(idCurtir);
                if (postCurtir != null) {
                    boolean curtiu = postCurtir.alternarCurtida(eu);
                    System.out.println(curtiu ? "\nPostagem curtida!" : "Curtida removida!");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    System.out.println("\nPostagem não encontrada no seu feed.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 4:
                limparTela();
                exibirFeed();
                System.out.print("Digite o ID da postagem que deseja comentar: ");
                int idComentar = lerInteiro();
                Postagem postComentar = redeSocial.buscarPostagemNoFeed(idComentar);
                if (postComentar != null) {
                    System.out.print("\nDigite seu comentário: ");
                    String textoComentario = scanner.nextLine();
                    postComentar.adicionarComentario(new Comentario(textoComentario, eu));
                    limparTela();
                    System.out.println("Comentário adicionado!");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    limparTela();
                    System.out.println("Postagem não encontrada no seu feed.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 5:
                limparTela();
                System.out.print("Digite o login do usuário que deseja adicionar: ");
                String loginAdd = scanner.nextLine();
                Usuario userAdd = redeSocial.buscarUsuarioPorLogin(loginAdd);
                if (userAdd != null && !userAdd.equals(eu)) {
                    eu.adicionarAmigo(userAdd);
                    limparTela();
                    System.out.println(userAdd.getNome() + " adicionado aos amigos!");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    limparTela();
                    System.out.println("Usuário não encontrado ou é você mesmo.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 6:
                limparTela();
                System.out.print("Digite o login do amigo que deseja remover: ");
                String loginRem = scanner.nextLine();
                Usuario userRem = redeSocial.buscarUsuarioPorLogin(loginRem);
                if (userRem != null && eu.getAmigos().contains(userRem)) {
                    eu.removerAmigo(userRem);
                    limparTela();
                    System.out.println(userRem.getNome() + " removido dos amigos.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    limparTela();
                    System.out.println("Amigo não encontrado na sua lista.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 7:
                limparTela();
                System.out.println("Nome atual: " + eu.getNome());
                System.out.print("Novo nome (ou aperte ENTER para manter): ");
                String novoNome = scanner.nextLine();
                if (!novoNome.isBlank()) eu.setNome(novoNome);

                limparTela();
                System.out.println("Bio atual: " + eu.getBiografia());
                System.out.print("Nova bio (ou aperte ENTER para manter): ");
                String novaBio = scanner.nextLine();
                if (!novaBio.isBlank()) eu.setBiografia(novaBio);
                limparTela();
                System.out.println("Perfil atualizado!");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                break;
            case 8:
                limparTela();
                System.out.print("Digite o ID da sua postagem a ser excluída: ");
                int idRemover = lerInteiro();
                if (eu.removerPostagem(idRemover)) {
                    limparTela();
                    System.out.println("Postagem excluída com sucesso.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    limparTela();
                    System.out.println("Postagem não encontrada ou sem permissão.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                break;
            case 0:
                redeSocial.fazerLogout();
                System.out.println("Logout efetuado com sucesso.");
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void exibirFeed() {
        List<Postagem> feed = redeSocial.obterFeed();
        if (feed.isEmpty()) {
            System.out.println("O feed está vazio. Adicione amigos ou faça uma postagem!");
        } else {
            System.out.println("\n--- SEU FEED ---");
            for (Postagem p : feed) {
                p.exibirPostagem();
            }
        }
    }

    private int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Por favor, digite um número válido: ");
            }
        }
    }

    /**
     * Auxiliar para buscar a arte em um arquivo de texto na pasta 'Art' na Área de Trabalho.
     * @param nomeArquivo O nome do arquivo com a extensão (ex: dragao.txt).
     * @return O conteúdo do arquivo em String, ou String vazia se falhar.
     */
    private String lerArteDeArquivo(String nomeArquivo) {
        String diretorioUsuario = System.getProperty("user.home");
        
        // Monta o caminho: C:\Users\SeuUsuario\Desktop\Art\nomeArquivo.txt
        Path caminhoArquivo = Paths.get(diretorioUsuario, "Desktop", "Art", nomeArquivo);

        try {
            // Files.readString é um recurso muito útil do Java moderno (Java 11+)
            return Files.readString(caminhoArquivo);
        } catch (IOException e) {
            System.out.println("\n[ERRO DE ARQUIVO] Não foi possível ler a arte.");
            System.out.println("Tentou buscar em: " + caminhoArquivo.toString());
            System.out.println("Verifique se a pasta 'Art' existe na sua Área de Trabalho e se o nome do arquivo está correto (com o .txt).\n");
            return ""; // Retorna vazio para a postagem não quebrar caso o arquivo não seja achado
        }
    }
}