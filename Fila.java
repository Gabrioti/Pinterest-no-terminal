public class Fila {
    String nome;
    No primeiroNo; // Referência para o primeiro nó da fila
    No ultimoNo; // Referência para o último nó da fila    

    public Fila(String nome) {
        this.nome = nome;
        this.primeiroNo = null; // Inicialmente, o primeiro nó da fila está vazio
        this.ultimoNo = null; // Inicialmente, o último nó da fila está vazio
    }
}

void adicionarFila(Pin novoPin){
    No novoNo = new No(novoPin); // Cria um novo nó com o pin fornecido

    if (primeiroNo == null) {
        primeiroNo = novoNo; // Se a fila estiver vazia, o novo nó se torna o primeiro
        ultimoNo = novoNo; // O novo nó também se torna o último
    } else {
        ultimoNo.proximo = novoNo; // O último nó atual aponta para o novo nó
        ultimoNo = novoNo; // O novo nó se torna o último nó da fila
    }
}