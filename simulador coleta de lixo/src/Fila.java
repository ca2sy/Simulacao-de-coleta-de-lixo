public class Fila<T> {
    public static class No<T> { //No 
        T dado;
        No<T> prox;

        No(T dado) {
            this.dado = dado;
            this.prox = null;
        }
    }

   
    public No<T> head;// head é o que vai ser atendido
    public No<T> tail; //tail é o ultimo da fila
    public int tamanho;

    public Fila() { //fila começa sem ninguem, head e tail null
        this.head = null;
        this.tail = null;
        this.tamanho = 0;
    }

    public void enfileirar(T elemento) { //adicionar alguem na fila, enqueue
        No<T> novoNo = new No<>(elemento);
        if (estaVazia()) { //se tiver vazia, o elemento que ta sendo adicioado vai ser o primeiro
            head = novoNo;
        } else { 
            tail.prox = novoNo; //se nao, ele vira o proximo do atual tail
        }
        tail = novoNo; //de qualquer forma, o elemento vai ser o novo ultimo, mesmo se for o unico da fila
        tamanho++; //o tamanho da fila aumenta
    }

    public T desenfileirar() { //remover, dequeue
        if (estaVazia()) {
            throw new IllegalStateException("Fila vazia"); //se a fila nao tiver ngm, ngm pode ser removido
        }
        T elemento = head.dado; // o elemento removido é o primeiro da vida, referenciamos ele para retorná-lo
        head = head.prox; //o primeiro da fila vira o proximo
        if (head == null) { //se a o head virar null, isso significa que era o unico da fila, então atualizamos o tail tbm
            tail = null;
        }
        tamanho--; //tamanho diminui
        return elemento; //retorno quem foi tirado
    }

    public T espiar() { //so pra ver o primeiro da fila e retornar ele, sem tirar da fila ainda
        if (estaVazia()) {
            throw new IllegalStateException("Fila vazia");
        }
        return head.dado;
    }

    public boolean estaVazia() { //serve pra verificar se ta vazia
        return tamanho == 0;
    }

    public String listaString() { //metodo de listar, eu atualizo ele la na cidade pra listar os componentes
        String elemento = "";
        No<T> atual = this.head;
        while (atual != null) {
            elemento = elemento + " >> " + atual.dado.toString();
            atual = atual.prox;
        }

        return elemento;

    }

}
