import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma publicação feita por um usuário na rede social.
 */
public class Postagem {
    private static int contadorId = 1; // Para facilitar a seleção no console
    private int id;
    private String texto;
    private String arteAscii; // Novo atributo para armazenar a arte
    private Usuario autor;
    private List<Comentario> comentarios;
    private List<Usuario> curtidas;

    /**
     * Construtor da classe Postagem.
     * @param texto Conteúdo textual da postagem.
     * @param autor Usuário que criou a postagem.
     * @param arteAscii Arte ASCII anexada (pode ser vazia).
     */
    public Postagem(String texto, Usuario autor, String arteAscii) {
        this.id = contadorId++;
        this.texto = texto;
        this.arteAscii = arteAscii;
        this.autor = autor;
        this.comentarios = new ArrayList<>();
        this.curtidas = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getTexto() { return texto; }
    public String getArteAscii() { return arteAscii; }
    public Usuario getAutor() { return autor; }
    public List<Comentario> getComentarios() { return comentarios; }
    public int getQuantidadeCurtidas() { return curtidas.size(); }

    public void adicionarComentario(Comentario comentario) {
        this.comentarios.add(comentario);
    }

    public boolean alternarCurtida(Usuario usuario) {
        if (curtidas.contains(usuario)) {
            curtidas.remove(usuario);
            return false; // Descurtiu
        } else {
            curtidas.add(usuario);
            return true; // Curtiu
        }
    }

    /**
     * Retorna os detalhes da postagem formatados em uma String.
     * Necessário para a interface de tela dividida.
     */
    public String formatarPostagem() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------\n");
        sb.append("[ID: ").append(id).append("] ").append(autor.getNome()).append(" (@").append(autor.getLogin()).append(") postou:\n");
        sb.append(texto).append("\n");
        
        // Renderiza a arte ASCII apenas se houver conteúdo
        if (arteAscii != null && !arteAscii.trim().isEmpty()) {
            sb.append("\n").append(arteAscii);
            // Garante que a arte termine com uma quebra de linha para não bugar o layout
            if (!arteAscii.endsWith("\n")) {
                sb.append("\n");
            }
        }
        
        sb.append("-> Curtidas: ").append(getQuantidadeCurtidas()).append("\n");
        if (!comentarios.isEmpty()) {
            sb.append("-> Comentários:\n");
            for (Comentario c : comentarios) {
                sb.append("   - ").append(c.toString()).append("\n");
            }
        }
        sb.append("--------------------------------------------------");
        return sb.toString();
    }
}