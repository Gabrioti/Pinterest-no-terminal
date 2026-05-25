import java.util.ArrayList;
import java.util.List;

/**
 * Controller principal que gerencia o estado da rede social.
 */
public class RedeSocial {
    private List<Usuario> usuariosGlobais;
    private Usuario usuarioLogado;

    /**
     * Construtor inicializa as coleções globais.
     */
    public RedeSocial() {
        this.usuariosGlobais = new ArrayList<>();
        this.usuarioLogado = null;
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
}