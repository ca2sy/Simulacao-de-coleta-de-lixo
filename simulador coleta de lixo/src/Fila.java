public class Fila<T> {
    private static class No<T> {
        T dado;
        No<T> prox;

        No(T dado) {
            this.dado = dado;
            this.prox = null;
        }
    }

    public No<T> head;
    public No<T> tail;
    public int tamanho;

    public Fila() {
        this.head = null;
        this.tail = null;
        this.tamanho = 0;
    }

    public void enfileirar(T elemento) {
        No<T> novoNo = new No<>(elemento);
        if (estaVazia()) {
            head = novoNo;
        } else {
            tail.prox = novoNo;
        }
        tail = novoNo;
        tamanho++;
    }

    public T desenfileirar() {
        if (estaVazia()) {
            throw new IllegalStateException("Fila vazia");
        }
        T elemento = head.dado;
        head = head.prox;
        if (head == null) {
            tail = null;
        }
        tamanho--;
        return elemento;
    }

    public T espiar() {
        if (estaVazia()) {
            throw new IllegalStateException("Fila vazia");
        }
        return head.dado;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public String listaString() {
        String r = "";
        No<T> atual = this.head;
        while (atual != null){
            r = r + " >> " + atual.dado.toString();
            atual = atual.prox;
        }

        return r;

    }

    public int tamanho() {
        return tamanho;
    }
}
