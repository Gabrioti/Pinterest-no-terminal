import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * View textual que interage com o usuário e coordena a RedeSocial.
 * Implementa interface de tela dividida (Split-Screen).
 */
public class Menu {
    private RedeSocial redeSocial;
    private Scanner scanner;
    private int indiceFeedAtual; // Controla qual postagem está sendo vista no momento

    public Menu() {
        this.redeSocial = new RedeSocial();
        this.scanner = new Scanner(System.in);
        this.indiceFeedAtual = 0;
    }

    public static void limparTela() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
      }

    public void iniciar() {
        limparTela();
        boolean executando = true;

        System.out.println("                                                                                                    \n" + //
                        "                                                                                                    \n" + //
                        "                                                                                                    \n" + //
                        "                      ░▒░                                                                           \n" + //
                        "                    ███████░                                                                        \n" + //
                        "                  ░█████████▒                                                                       \n" + //
                        "              ▒█████████████████▓   ░█▒     ██    ██  ███    █▒    ███    █████▒  ░██  ▒████░       \n" + //
                        "             █████████████████████  ░█▓     ██    ██  ████   █▒   ████▒   ██  ██▓ ░██ ░█▓           \n" + //
                        "            ░█████████████████████  ░█▓     ██    ██  ██ ██  █▒  ▒█▒ ██   ██████  ░██  ▓███         \n" + //
                        "             ▓████████████████████  ░█▓     ██   ░██  ██  ██▒█▒  ███████  ██▒██░  ░██     ██▓       \n" + //
                        "               █████████████████░   ░████▓░ ▒██████░  ██   ███▒ ██░   ██▒ ██  ▓██ ░██ ░██▓██▒       \n" + //
                        "               █████████████████                                                                    \n" + //
                        "               █████████████████                                                                    \n" + //
                        "                ███████████████░                                                                    \n" + //
                        "                  ▒▓▓░   ░▒▓▒░                                                                      \n" + //
                        "                                                                                                    \n" + //
                        "");


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
                    limparTela();
                } else {
                    limparTela();
                    System.out.println("Erro: Login já existe.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    limparTela();
                }
                break;
            case 2:
                limparTela();
                System.out.print("Login: ");
                String log = scanner.nextLine();
                System.out.print("Senha: ");
                String sen = scanner.nextLine();
                
                if (redeSocial.fazerLogin(log, sen)) {
                    indiceFeedAtual = 0; // Reseta o feed ao logar
                } else {
                    limparTela();
                    System.out.println("Erro: Credenciais inválidas.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    limparTela();
                }
                break;
            case 0:
                return false;
            default:
                limparTela();
                System.out.println("Opção inválida.");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                limparTela();
        }
        return true;
    }

    private void menuLogado() {
        Usuario eu = redeSocial.getUsuarioLogado();
        boolean logado = true;

        while (logado) {
            limparTela();
            // Desenha a interface lado a lado
            renderizarTelaDividida(eu);

            // Lê o comando do usuário (pode ser número do menu ou letra P/A para o feed)
            String input = scanner.nextLine().trim().toUpperCase();

            // Navegação do Feed
            if (input.equals("P")) {
                List<Postagem> feed = redeSocial.obterFeed();
                if (!feed.isEmpty() && indiceFeedAtual < feed.size() - 1) {
                    indiceFeedAtual++;
                }
            } 
            else if (input.equals("A")) {
                if (indiceFeedAtual > 0) {
                    indiceFeedAtual--;
                }
            } 
            // Execução de Comandos do Menu
            else {
                try {
                    int opcao = Integer.parseInt(input);
                    logado = processarAcaoMenu(opcao, eu);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida!");
                    pausar();
                }
            }
        }
    }

    /**
     * O "Motor" gráfico do nosso console. Monta o lado esquerdo e direito e funde os dois.
     */
    private void renderizarTelaDividida(Usuario eu) {

        limparTela();

        String menuEsquerda = construirMenuEsquerda(eu);
        String feedDireita = construirFeedDireita();

        // O Regex \r?\n garante compatibilidade de quebra de linha entre Windows/Linux
        String[] linhasEsq = menuEsquerda.split("\r?\n");
        String[] linhasDir = feedDireita.split("\r?\n");

        int maxLinhas = Math.max(linhasEsq.length, linhasDir.length);

        System.out.println("=========================================================================================");
        // %-35s significa: Reserva 35 espaços e alinha o texto à esquerda. O | divide a tela.
        System.out.printf("%-35s | %s%n", "MENU DE NAVEGAÇÃO", "FEED PÚBLICO (Visualização)");
        System.out.println("=========================================================================================");

        for (int i = 0; i < maxLinhas; i++) {
            String linhaEsq = (i < linhasEsq.length) ? linhasEsq[i] : "";
            String linhaDir = (i < linhasDir.length) ? linhasDir[i] : "";
            System.out.printf("%-35s | %s%n", linhaEsq, linhaDir);
        }

        System.out.println("=========================================================================================");
        System.out.print("Escolha uma opção [0-8] ou navegue no Feed [P]róxima / [A]nterior: ");
    }

    private String construirMenuEsquerda(Usuario eu) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- ").append(eu.getNome().toUpperCase()).append(" ---\n");
        sb.append("[1] Atualizar/Recarregar Feed\n");
        sb.append("[2] Criar Postagem\n");
        sb.append("[3] Curtir / Descurtir Postagem\n");
        sb.append("[4] Comentar em Postagem\n");
        sb.append("[5] Adicionar Amigo\n");
        sb.append("[6] Remover Amigo\n");
        sb.append("[7] Editar Perfil\n");
        sb.append("[8] Excluir Minha Postagem\n");
        sb.append("[0] Fazer Logout\n\n");
        sb.append("--- CONTROLES DO FEED ---\n");
        sb.append("[P] Proxima Postagem\n");
        sb.append("[A] Postagem Anterior\n");
        return sb.toString();
    }

    private String construirFeedDireita() {
        List<Postagem> feed = redeSocial.obterFeed();
        if (feed.isEmpty()) {
            return "\n   O feed está vazio no momento.\n   Seja o primeiro a postar!";
        }

        // Garante que o índice não saia dos limites (ex: se excluiu o último post)
        if (indiceFeedAtual >= feed.size()) {
            indiceFeedAtual = Math.max(0, feed.size() - 1);
        }

        Postagem atual = feed.get(indiceFeedAtual);
        String cabecalho = "--- POSTAGEM " + (indiceFeedAtual + 1) + " DE " + feed.size() + " ---\n";
        return cabecalho + atual.formatarPostagem();
    }

    /**
     * Gerencia as regras de negócio das opções numéricas do menu.
     */
    private boolean processarAcaoMenu(int opcao, Usuario eu) {
        System.out.println("\n----------------------------------");
        switch (opcao) {
            case 1:
                indiceFeedAtual = 0;
                return true; // Volta instantaneamente pro loop sem pausar
            case 2:
                System.out.print("Digite o texto da sua publicação:\n> ");
                String texto = scanner.nextLine();
                
                System.out.print("Deseja adicionar uma arte ASCII? (S/N): ");
                String desejaArte = scanner.nextLine().trim();
                String arteAscii = "";
                
                if (desejaArte.equalsIgnoreCase("S")) {
                    System.out.println("\n[1] Colar diretamente no terminal");
                    System.out.println("[2] Carregar arquivo txt da Área de Trabalho (Pasta 'Art')");
                    System.out.print("Escolha: ");
                    int opcaoArte = lerInteiro();

                    if (opcaoArte == 1) {
                        System.out.println("Cole a arte. Digite '//FIM' para finalizar.");
                        StringBuilder builderArte = new StringBuilder();
                        while (true) {
                            String linha = scanner.nextLine();
                            if (linha.equals("//FIM")) break;
                            builderArte.append(linha).append("\n");
                        }
                        arteAscii = builderArte.toString();
                    } else if (opcaoArte == 2) {
                        System.out.print("Nome do arquivo (ex: nave.txt): ");
                        String nomeArquivo = scanner.nextLine().trim();
                        arteAscii = lerArteDeArquivo(nomeArquivo);
                    }
                }
                eu.adicionarPostagem(new Postagem(texto, eu, arteAscii));
                System.out.println("Postagem criada!");
                indiceFeedAtual = 0; // Volta o feed pro início pra ver o post novo
                break;
            case 3:
                System.out.print("Digite o ID da postagem que deseja curtir/descurtir: ");
                Postagem postCurtir = redeSocial.buscarPostagemNoFeed(lerInteiro());
                if (postCurtir != null) {
                    boolean curtiu = postCurtir.alternarCurtida(eu);
                    System.out.println(curtiu ? "Postagem curtida!" : "Curtida removida!");
                } else System.out.println("Postagem não encontrada.");
                break;
            case 4:
                System.out.print("Digite o ID da postagem que deseja comentar: ");
                Postagem postComentar = redeSocial.buscarPostagemNoFeed(lerInteiro());
                if (postComentar != null) {
                    System.out.print("Digite seu comentário: ");
                    postComentar.adicionarComentario(new Comentario(scanner.nextLine(), eu));
                    System.out.println("Comentário adicionado!");
                } else System.out.println("Postagem não encontrada.");
                break;
            case 5:
                System.out.print("Digite o login do usuário que deseja adicionar: ");
                Usuario userAdd = redeSocial.buscarUsuarioPorLogin(scanner.nextLine());
                if (userAdd != null && !userAdd.equals(eu)) {
                    eu.adicionarAmigo(userAdd);
                    System.out.println(userAdd.getNome() + " adicionado aos amigos!");
                } else System.out.println("Usuário inválido.");
                break;
            case 6:
                System.out.print("Digite o login do amigo que deseja remover: ");
                Usuario userRem = redeSocial.buscarUsuarioPorLogin(scanner.nextLine());
                if (userRem != null && eu.getAmigos().contains(userRem)) {
                    eu.removerAmigo(userRem);
                    System.out.println(userRem.getNome() + " removido.");
                } else System.out.println("Amigo não encontrado.");
                break;
            case 7:
                System.out.println("Nome atual: " + eu.getNome());
                System.out.print("Novo nome (ENTER p/ manter): ");
                String nNome = scanner.nextLine();
                if (!nNome.trim().isEmpty()) eu.setNome(nNome);

                System.out.println("Bio atual: " + eu.getBiografia());
                System.out.print("Nova bio (ENTER p/ manter): ");
                String nBio = scanner.nextLine();
                if (!nBio.trim().isEmpty()) eu.setBiografia(nBio);
                System.out.println("Perfil atualizado!");
                break;
            case 8:
                System.out.print("Digite o ID da sua postagem a ser excluída: ");
                if (eu.removerPostagem(lerInteiro())) {
                    System.out.println("Postagem excluída.");
                    indiceFeedAtual = 0;
                } else System.out.println("Não encontrada ou sem permissão.");
                break;
            case 0:
                redeSocial.fazerLogout();
                return false;
            default:
                System.out.println("Opção inválida.");
        }
        pausar(); // Pausa para o usuário conseguir ler a mensagem de sucesso antes de limpar a tela
        return true;
    }

    private void pausar() {
        System.out.println("\nAperte ENTER para voltar...");
        scanner.nextLine();
    }

    private int lerInteiro() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine()); } 
            catch (NumberFormatException e) { System.out.print("Digite um número válido: "); }
        }
    }

    private String lerArteDeArquivo(String nomeArquivo) {
        String dir = System.getProperty("user.home");
        Path caminho = Paths.get(dir, "Desktop", "Art", nomeArquivo);
        try {
            return new String(Files.readAllBytes(caminho));
        } catch (IOException e) {
            System.out.println("[ERRO] Arquivo não encontrado em: " + caminho);
            return "";
        }
    }
}