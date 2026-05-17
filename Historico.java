public class Historico {
    String nome;
    NoHistorico primeiroNo; // Referência para o primeiro nó do histórico

    public Historico(String nome) {
        this.nome = nome;
        this.primeiroNo = null; // Inicialmente, o histórico está vazio
    }
}

void adicionarHistorico(String nome) {
    NoHistorico novaAcao = new NoHistorico(nome); // Cria um novo nó com o nome da ação

        novaAcao.proximo = primeiroNo; // O novo nó aponta para o primeiro nó atual
        primeiroNo = novaAcao; // O novo nó se torna o primeiro nó da pasta

}

void voltarHistorico() {
    if (primeiroNo != null) {
        primeiroNo = primeiroNo.proximo; // Move o primeiro nó para o próximo, removendo o último histórico
    }
}

void exibirHistorico() {
    No atual = primeiroNo;
    while (atual != null) {
        System.out.println(atual.nome);
        atual = atual.proximo;
    }
}