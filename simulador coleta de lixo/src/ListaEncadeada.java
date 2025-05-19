public class ListaEncadeada<T> {
    public static class No<T> { //no
        T dado;
        No<T> prox;

        No(T dado) {
            this.dado = dado;
            this.prox = null;
        }
    }

    public No<T> head; //decidi usar head doq "primeiro" por costume
    public int tamanho;

    public ListaEncadeada() { //começa vazia
        this.head = null;
        this.tamanho = 0;
    }

    public void adicionar(T elemento) { //adicionar na lista
        No<T> novoNo = new No<>(elemento); //novo no a ser adicionado
        if (head == null) { //se nao tiver ngm  na lista
            head = novoNo; //ele vai ser o primeiro da lista
        } else { //se nao, quer dizer que tem gente
            No<T> atual = head; //crio o no atual pra me "locomover" pela lista
            while (atual.prox != null) { //e enquanto o proximo nao for null, ou seja, enquanto eu nao estiver no fim da lista
                atual = atual.prox; //eu avanço
            }
            atual.prox = novoNo; //o ultimo vira meu novo no
        }
        tamanho++;// tamanho aumenta
    }


    //eu fiz esse metodo, mas acabei nem usando,pq eu nunca tinha que adicionar em uma posição especifica. mas vai ficar ai, pq eu fiz
    public void adicionarNaPosicao(int posicao, T elemento) { 
    if (posicao < 0 || posicao > tamanho) {
        throw new IndexOutOfBoundsException("Posição inválida"); //nao posso por numa posição negativa ou numa posição que deixe um "buraco" na lista
    }

    No<T> novoNo = new No<>(elemento);

    if (posicao == 0) { // inserir no início
        novoNo.prox = head;
        head = novoNo;
    } else { //se nao for no começo
        No<T> atual = head;
        for (int i = 0; i < posicao - 1; i++) { //faço o atual ir ate uma posicao antes da que eu quero por
            atual = atual.prox;
        }
        novoNo.prox = atual.prox; //o prox do meu elemento vira o proximo do meu atual, assim nao perco ele
        atual.prox = novoNo;// o prox do meu atual deixa de ser o que ja ta na lista e vira meu elemento
    }
    tamanho++; //tamanho da lista aumenta
}


    public boolean remover(T elemento) {
        if (head == null)
            return false;

        if (head.dado.equals(elemento)) {
            head = head.prox;
            tamanho--;
            return true;
        }

        No<T> atual = head;
        while (atual.prox != null && !atual.prox.dado.equals(elemento)) {
            atual = atual.prox;
        }

        if (atual.prox == null)
            return false;

        atual.prox = atual.prox.prox;
        tamanho--;
        return true;
    }

    //tambem nao usei esse

    public T removerDaPosicao(int posicao) {
    if (posicao < 0 || posicao >= tamanho) {
        throw new IndexOutOfBoundsException("Posição inválida"); //nao posso remover de um lugar inexistente, ou seja, negativo ou fora do tamanho da lista
    }

    T removido; //dou nome pra quem vai ser removido pq preciso retornar ele

    if (posicao == 0) { // remover o primeiro
        removido = head.dado; 
        head = head.prox; //meu novo head é o prox
    } else {
        No<T> atual = head;  //crio o atual
        for (int i = 0; i < posicao - 1; i++) { //fico atras de onde quero remover
            atual = atual.prox;
        }
        removido = atual.prox.dado; //o removido é o proximo
        atual.prox = atual.prox.prox; //faço o proximo ser agora o proximo do que quero remover
    }

    tamanho--; //diminuo o tamanho
    return removido; //retorno o elemento removido
}

    
    public void imprimir() {
        No<T> atual = head;
        while (atual != null) {
            System.out.print(atual.dado + " -> ");
            atual = atual.prox;
        }
        System.out.println("null");
    }
}