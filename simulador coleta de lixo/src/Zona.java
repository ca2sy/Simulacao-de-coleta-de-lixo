public class Zona {
    // Atributos de identificação
    public String nome;

    // Atributos de lixo (toneladas)
    public int lixo_minimo;
    public int lixo_maximo;
    public int lixo_zona;
    public int lixo_atual;

    // Atributos de tempo (em minutos)
    public int tempo_min_viagem_pico;
    public int tempo_max_viagem_pico;
    public int tempo_min_viagem_normal;
    public int tempo_max_viagem_normal;
    public int tempo_viagem_estacao_normal;
    public int tempo_viagem_estacao_pico;

    // Atributos de localização
    public EstacaoTransferencia estacao_descarga;

    public Zona(String nome, int lixo_minimo, int lixo_maximo,
            int tempo_min_viagem_pico, int tempo_max_viagem_pico,
            int tempo_min_viagem_normal, int tempo_max_viagem_normal,
            EstacaoTransferencia estacao_descarga,
            int tempo_viagem_estacao_normal, int tempo_viagem_estacao_pico) {

        this.nome = nome;
        this.lixo_minimo = lixo_minimo;
        this.lixo_maximo = lixo_maximo;
        this.tempo_min_viagem_pico = tempo_min_viagem_pico;
        this.tempo_max_viagem_pico = tempo_max_viagem_pico;
        this.tempo_min_viagem_normal = tempo_min_viagem_normal;
        this.tempo_max_viagem_normal = tempo_max_viagem_normal;
        this.estacao_descarga = estacao_descarga;
        this.tempo_viagem_estacao_normal = tempo_viagem_estacao_normal;
        this.tempo_viagem_estacao_pico = tempo_viagem_estacao_pico;
        this.lixo_zona = gerarLixoDiario();
        this.lixo_atual = this.lixo_zona;
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
        int coletado =  Math.min(quantidadeSolicitada, lixo_atual);
        this.lixo_atual = lixo_atual - quantidadeSolicitada;
        System.out.println(
                "Zona " + nome + ": " + coletado + " toneladas coletadas. Lixo restante: " + lixo_atual + " toneladas.");
        return coletado ;
    }

    public int calcularTempoViagemPico() {
        return tempo_min_viagem_pico + (int) (Math.random() * (tempo_max_viagem_pico - tempo_min_viagem_pico));
    }

    public int calcularTempoViagemNormal() {
        return tempo_min_viagem_normal + (int) (Math.random() * (tempo_max_viagem_normal - tempo_min_viagem_normal));
    }
}