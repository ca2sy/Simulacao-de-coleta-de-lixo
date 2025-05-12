public class ListaEncadeada<T> {
    public static class No<T> {
        T dado;
        No<T> prox;

        No(T dado) {
            this.dado = dado;
            this.prox = null;
        }
    }

    public No<T> head;
    public int tamanho;

    public ListaEncadeada() {
        this.head = null;
        this.tamanho = 0;
    }

    public void adicionar(T elemento) {
        No<T> novoNo = new No<>(elemento);
        if (head == null) {
            head = novoNo;
        } else {
            No<T> atual = head;
            while (atual.prox != null) {
                atual = atual.prox;
            }
            atual.prox = novoNo;
        }
        tamanho++;
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

    public boolean contem(T elemento) {
        No<T> atual = head;
        while (atual != null) {
            if (atual.dado.equals(elemento))
                return true;
            atual = atual.prox;
        }
        return false;
    }

    public boolean estaVazia() {
        return tamanho == 0;
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