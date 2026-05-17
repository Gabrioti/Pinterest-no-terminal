public class Pin {
    String titulo;
    String descricao;
    String tag;
    int identidade; // Identidade única para cada pin
    String arteASCII; // Representação visual do pin usando arte ASCII

    public Pin(String titulo, String descricao, String tag, int identidade, String arteASCII) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tag = tag;
        this.identidade = identidade;
        this.arteASCII = arteASCII;
    }

    Pin removerDaFila(){
        No noTemporario = primeiroNo; // Armazena o primeiro nó da fila em uma variável temporária
        if (primeiroNo != null) {
            primeiroNo = primeiroNo.proximo; // Move o primeiro nó para o próximo, removendo o primeiro nó da fila
            if (primeiroNo == null) {
                ultimoNo = null; // Se a fila ficar vazia, o último nó também deve ser definido como null
            }
            return noTemporario.pin; // Retorna o pin do nó removido
        } else {
            ultimoNo = null;
            return null; // Retorna null se a fila estiver vazia
        }
    }
}


