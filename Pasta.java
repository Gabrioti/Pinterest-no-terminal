public class Pasta {
    String nome;
    No primeiroNo; // Referência para o primeiro nó da pasta

    public Pasta(String nome) {
        this.nome = nome;
        this.primeiroNo = null; // Inicialmente, a pasta está vazia
    }
}

void adicionarPin(Pin novoPin){
    No novoNo = new No(novoPin); // Cria um novo nó com o pin fornecido

    if (primeiroNo == null) {
        primeiroNo = novoNo; // Se a pasta estiver vazia, o novo nó se torna o primeiro
    } else {
        novoNo.proximo = primeiroNo; // O novo nó aponta para o primeiro nó atual
        primeiroNo = novoNo; // O novo nó se torna o primeiro nó da pasta

    }
}

void exibirPins() {
    No noAtual = primeiroNo; // Começa a partir do primeiro nó

    while (noAtual != null) {

        Pin pin = noAtual.pin; // Obtém o pin do nó atual, que está na classe Pin, que está na classe No.
                               //  O pin tem as informações do título, descrição, tag, identidade e arte ASCII.       
        System.out.println("Título: " + pin.titulo);
        System.out.println("Descrição: " + pin.descricao);
        System.out.println("Tag: " + pin.tag);
        System.out.println("Identidade: " + pin.identidade);
        System.out.println("Arte ASCII:\n" + pin.arteASCII);
        System.out.println("-----------------------------");

        noAtual = noAtual.proximo; // Move para o próximo nó
    }
}