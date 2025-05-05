public class Zona {
    public String nome;
    public int lixo_minimo;
    public int lixo_maximo; // com esses atributos, você pode gerar uma quantidade aleatória de lixo para a zona
    public long tempo_min_viagem_pico, tempo_max_viagem_pico, tempo_min_viagem_normal, tempo_max_viagem_normal; // Tempo em milissegundos
    public int lixo_zona;
    public EstacaoTransferencia estacao_descarga;
    public long tempo_viagem_estacao_normal;
    public long tempo_viagem_estacao_pico;

    public Zona(String nome, int lixo_minimo, int lixo_maximo, long tempo_min_viagem_pico, long tempo_max_viagem_pico, long tempo_min_viagem_normal, long tempo_max_viagem_normal,EstacaoTransferencia estacao_descarga, long tempo_viagem_estacao_normal, long tempo_viagem_estacao_pico) {
        this.lixo_maximo = lixo_maximo;
        this.lixo_minimo = lixo_minimo;
        this.tempo_max_viagem_normal = tempo_max_viagem_normal;
        this.tempo_min_viagem_normal = tempo_min_viagem_normal;
        this.tempo_max_viagem_pico = tempo_max_viagem_pico;
        this.tempo_min_viagem_pico = tempo_min_viagem_pico;
        this.nome = nome;
        this.estacao_descarga = estacao_descarga;
        this.tempo_viagem_estacao_normal = tempo_viagem_estacao_normal;
        this.tempo_viagem_estacao_pico = tempo_viagem_estacao_pico;
        this.lixo_zona = gerarLixoDiario();
  
    }

    public int getLixo_maximo() {
        return lixo_maximo;
    }

    public int getLixo_minimo() {
        return lixo_minimo;
    }

    public int getLixo_zona() {
        return lixo_zona;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public int gerarLixoDiario() {
        lixo_zona = lixo_minimo + (int) (Math.random() * (lixo_maximo - lixo_minimo + 1));
        System.out.println("Zona " + nome + " gerou " + lixo_zona + " toneladas de lixo hoje.");
        return lixo_zona;
    }

    public int LixoColetado(int quantidadeSolicitada) {
        int coletado = Math.min(quantidadeSolicitada, lixo_zona); 
        lixo_zona -= coletado; // Diminui o lixo da zona
        System.out.println("Zona " + nome + ": " + coletado + " toneladas coletadas. Lixo restante: " + lixo_zona + " toneladas.");
        return coletado; // Retorna a quantidade coletada
    }

    // Métodos para calcular o tempo de viagem em milissegundos
    public long calcularTempoViagemPico() {
        return tempo_min_viagem_pico + (long) (Math.random() * (tempo_max_viagem_pico - tempo_min_viagem_pico));
    }

    public long calcularTempoViagemNormal() {
        return tempo_min_viagem_normal + (long) (Math.random() * (tempo_max_viagem_normal - tempo_min_viagem_normal));
    }
}
