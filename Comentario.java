/**
 * Representa um comentário feito em uma postagem.
 */
public class Comentario {
    private String texto;
    private Usuario autor;

    /**
     * Construtor da classe Comentario.
     * @param texto Conteúdo do comentário.
     * @param autor Usuário que fez o comentário.
     */
    public Comentario(String texto, Usuario autor) {
        this.texto = texto;
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public Usuario getAutor() {
        return autor;
    }

    @Override
    public String toString() {
        return autor.getNome() + " (@" + autor.getLogin() + ") comentou: " + texto;
    }
}