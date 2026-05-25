import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário da rede social.
 */
public class Usuario {
    private String nome;
    private String login;
    private String senha;
    private String biografia;
    private List<Usuario> amigos;
    private List<Postagem> postagens;

    /**
     * Construtor da classe Usuario.
     * @param nome Nome de exibição.
     * @param login Nome de usuário (único).
     * @param senha Senha de acesso.
     */
    public Usuario(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.biografia = "Olá! Estou usando a Rede Social no Console.";
        this.amigos = new ArrayList<>();
        this.postagens = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getLogin() { return login; }
    
    public String getSenha() { return senha; }
    
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public List<Usuario> getAmigos() { return amigos; }
    public List<Postagem> getPostagens() { return postagens; }

    /**
     * Adiciona um amigo à lista.
     * @param amigo Objeto usuário a ser adicionado.
     */
    public void adicionarAmigo(Usuario amigo) {
        if (!amigos.contains(amigo) && !this.equals(amigo)) {
            amigos.add(amigo);
        }
    }

    /**
     * Remove um amigo da lista.
     * @param amigo Objeto usuário a ser removido.
     */
    public void removerAmigo(Usuario amigo) {
        amigos.remove(amigo);
    }

    /**
     * Adiciona uma nova postagem à lista do usuário.
     * @param postagem Objeto postagem a ser adicionado.
     */
    public void adicionarPostagem(Postagem postagem) {
        postagens.add(postagem);
    }

    /**
     * Remove uma postagem própria pelo ID.
     * @param id ID da postagem.
     * @return true se removida com sucesso, false caso contrário.
     */
    public boolean removerPostagem(int id) {
        return postagens.removeIf(p -> p.getId() == id);
    }
}