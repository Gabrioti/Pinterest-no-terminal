import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller principal que gerencia o estado da rede social.
 */
public class RedeSocial {
    private List<Usuario> usuariosGlobais;
    private Usuario usuarioLogado;
    String nomeArquivo = "arte_ascii.txt"; // Nome do arquivo de arte ASCII

    /**
     * Construtor inicializa as coleções globais.
     */
    public RedeSocial() {
        this.usuariosGlobais = new ArrayList<>();
        this.usuarioLogado = null;

        // --- CARGA DE DADOS INICIAIS (MOCK) ---
        inicializarDadosDeTeste();
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Cadastra um novo usuário no sistema.
     * @param nome Nome do usuário.
     * @param login Login desejado.
     * @param senha Senha da conta.
     * @return true se sucesso, false se login já existe.
     */
    public boolean cadastrarUsuario(String nome, String login, String senha) {
        for (Usuario u : usuariosGlobais) {
            if (u.getLogin().equals(login)) {
                return false; // Login já existe
            }
        }
        usuariosGlobais.add(new Usuario(nome, login, senha));
        return true;
    }

    /**
     * Realiza a autenticação do usuário.
     * @param login Nome de usuário.
     * @param senha Senha de acesso.
     * @return true se logado com sucesso, false caso contrário.
     */
    public boolean fazerLogin(String login, String senha) {
        for (Usuario u : usuariosGlobais) {
            if (u.getLogin().equals(login) && u.getSenha().equals(senha)) {
                usuarioLogado = u;
                return true;
            }
        }
        return false;
    }

    /**
     * Encerra a sessão atual.
     */
    public void fazerLogout() {
        usuarioLogado = null;
    }

    /**
     * Busca um usuário pelo login.
     * @param login Login a ser buscado.
     * @return O objeto Usuario se encontrado, null caso contrário.
     */
    public Usuario buscarUsuarioPorLogin(String login) {
        for (Usuario u : usuariosGlobais) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Retorna o feed personalizado (posts do usuário + posts dos amigos).
     * @return Lista de postagens do feed.
     */
    public List<Postagem> obterFeed() {
        List<Postagem> feed = new ArrayList<>();
        if (usuarioLogado != null) {
            // Adiciona posts próprios
            feed.addAll(usuarioLogado.getPostagens());
            // Adiciona posts dos amigos
            for (Usuario amigo : usuarioLogado.getAmigos()) {
                feed.addAll(amigo.getPostagens());
            }
            // Ordenação decrescente pelo ID (mais recentes primeiro)
            feed.sort((p1, p2) -> Integer.compare(p2.getId(), p1.getId()));
        }
        return feed;
    }

    /**
     * Busca uma postagem específica no feed do usuário atual pelo ID.
     * @param id ID da postagem.
     * @return Objeto Postagem se encontrado no feed, null caso contrário.
     */
    public Postagem buscarPostagemNoFeed(int id) {
        for (Postagem p : obterFeed()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private void inicializarDadosDeTeste() {
        // 1. Cadastrando os usuários base
        cadastrarUsuario("Felipe", "felipe", "123");
        cadastrarUsuario("Lucas", "lucas", "123");
        cadastrarUsuario("Maria", "maria", "123");
        cadastrarUsuario("João", "joao", "123");
        cadastrarUsuario("Ana", "ana", "123");

        // 2. Recuperando as instâncias para gerar interações
        Usuario felipe = buscarUsuarioPorLogin("felipe");
        Usuario lucas = buscarUsuarioPorLogin("lucas");
        Usuario maria = buscarUsuarioPorLogin("maria");

        // 3. Criando amizades
        if (felipe != null && lucas != null && maria != null) {
            felipe.adicionarAmigo(lucas);
            lucas.adicionarAmigo(felipe);

            felipe.adicionarAmigo(maria);
            maria.adicionarAmigo(felipe);

            // 4. Lendo os arquivos txt e criando as postagens iniciais
            String arteFelipe = carregarArteParaTeste("Gato.txt");
            Postagem postFelipe = new Postagem("Olhem esse gato que eu desenhei no bloco de notas!", felipe, arteFelipe);
            felipe.adicionarPostagem(postFelipe);

            String arteLucas = carregarArteParaTeste("Recital.txt");
            Postagem postLucas = new Postagem("Meu recital! ", lucas, arteLucas);
            lucas.adicionarPostagem(postLucas);

            String arteMaria = carregarArteParaTeste("Violino.txt");
            Postagem postMaria = new Postagem("Estou aprendendo a tocar violino!", maria, arteMaria);
            maria.adicionarPostagem(postMaria);

            // 5. Adicionando interações (Curtidas e Comentários)
            postFelipe.alternarCurtida(maria);
            postFelipe.alternarCurtida(lucas);
            postFelipe.adicionarComentario(new Comentario("Ficou irado, Felipe!", maria));

            postLucas.adicionarComentario(new Comentario("Foi muito bom!", felipe));
            postLucas.alternarCurtida(felipe);

            postMaria.alternarCurtida(felipe);
            postMaria.adicionarComentario(new Comentario("Parabéns, Maria!", lucas));

        }
    }

    private String carregarArteParaTeste(String nomeArquivo) {
        String diretorioUsuario = System.getProperty("user.home");
        Path caminhoArquivo = Paths.get(diretorioUsuario, "Desktop", "Art", nomeArquivo);

        try {
            return new String(Files.readAllBytes(caminhoArquivo));
        } catch (IOException e) {
            System.out.println("[Aviso de Inicialização] O arquivo '" + nomeArquivo + "' não foi encontrado na pasta Art. O post de teste será criado apenas com texto.");
            try { Thread.sleep(1000); } catch (InterruptedException c) {}
            return "";
        }
    }
}