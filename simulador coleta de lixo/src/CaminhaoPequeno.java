public class CaminhaoPequeno {
    public int capacidade; // 2, 4, 8 ou 10
    public int carga_atual;
    public Fila<Zona> zonas_de_atuacao;
    public String id_caminhao_pequeno;
    public int num_viagens_realizadas;
    public int num_viagens_a_realizar;
    public Zona zona_atual;

    public long tempoEsperadoEmMs; 
    public static final long tempo_max_espera_ms = 20 * 60 * 1000; // 20 minutos em milissegundos

    public CaminhaoPequeno(int capacidade, Fila<Zona> zonas_de_atuacao, String id_caminhao_pequeno, int numeros_viagem_a_realizar) {
        this.capacidade = capacidade;
        this.zonas_de_atuacao = zonas_de_atuacao;
        this.carga_atual = 0;
        this.id_caminhao_pequeno = id_caminhao_pequeno;
        this.num_viagens_realizadas = 0;
        this.tempoEsperadoEmMs = 0;
        this.num_viagens_a_realizar = numeros_viagem_a_realizar;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getCargaAtual() {
        return carga_atual;
    }

    public int getNumViagensRealizadas() {
        return num_viagens_realizadas;
    }

    public String getIdCaminhaoPequeno() {
        return id_caminhao_pequeno;
    }


    public void liberarCarga(int quantidade) { //o caminhao pequeno vai liberar carga em um caminhao grande
        if (quantidade > carga_atual) {
            quantidade = carga_atual;
        }
        carga_atual -= quantidade;
        System.out.println("Caminhão " + id_caminhao_pequeno + " reduziu sua carga em " + quantidade + 
                           " toneladas. Carga restante: " + carga_atual + " toneladas.");
    }

    public void atualizarTempo(long tempoPassadoEmMs) {
        tempoEsperadoEmMs += tempoPassadoEmMs;
    }
    
    public void verificarTempoLimite() {
        if (tempoMaximoEsperado()) {
            System.out.println("Caminhão " + id_caminhao_pequeno + " excedeu o tempo máximo de espera.");
        } else {
            System.out.println("Caminhão " + id_caminhao_pequeno + " ainda tem tempo disponível.");
        }
    }
    
    public boolean tempoMaximoEsperado() {
        return tempoEsperadoEmMs >= tempo_max_espera_ms;
    }

    public void irParaEstacao(EstacaoTransferencia estacao){
        estacao.adicionarCaminhaoPequeno(this); // Adiciona o caminhão à fila da estação
        this.tempoEsperadoEmMs = 0; // Resetando o tempo de espera ao chegar
        System.out.println("Caminhão " + id_caminhao_pequeno + " foi para " + estacao.getNome() + ".");
    }
    
}