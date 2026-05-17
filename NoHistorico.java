public class NoHistorico {
    String nome;
    NoHistorico proximo; // Referência para o próximo nó do histórico

    public NoHistorico(String nome) {
        this.nome = nome;
        this.proximo = null; // Inicialmente, o nó não aponta para nenhum outro
    }
}