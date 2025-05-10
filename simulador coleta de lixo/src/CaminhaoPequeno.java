public class CaminhaoPequeno {
    public int capacidade; // 2, 4, 8 ou 10
    public int carga_atual;
    public Fila<Zona> zonas_de_atuacao;
    public String id_caminhao_pequeno;
    public int num_viagens_realizadas;
    public int num_viagens_a_realizar;
    public Zona zona_atual;
    public EstacaoTransferencia estacao_a_ir;
    public long tempo_restante_viagem;
    public EstacaoTransferencia estacao_atual;
    public boolean estaColetando;
   public long tempo_inicio_espera; // timestamp quando começou a esperar
    public long tempo_espera_acumulado; // tempo total de espera em ms
    public long tempo_max_espera = 20 * 60 * 1000; // 20 minutos em milissegundos

    public CaminhaoPequeno(int capacidade, Fila<Zona> zonas_de_atuacao, String id_caminhao_pequeno, int numeros_viagem_a_realizar) {
        this.capacidade = capacidade;
        this.zonas_de_atuacao = zonas_de_atuacao;
        this.carga_atual = 0;
        this.id_caminhao_pequeno = id_caminhao_pequeno;
        this.num_viagens_realizadas = 0;
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

    public long getTempoMaxEsperaMs() {
        return tempo_max_espera;
    }

public void liberarCarga(CaminhaoGrande caminhaoGrande) { 
   
    int espacoDisponivelCaminhaoGrande = caminhaoGrande.capacidade_maxima - caminhaoGrande.getCargaAtual();
    
    // Se o caminhão pequeno tiver mais carga do que o caminhão grande pode receber,
    // libera apenas a quantidade que o caminhão grande consegue carregar
    int quantidadeLiberada = Math.min(this.carga_atual, espacoDisponivelCaminhaoGrande);
    
    if (quantidadeLiberada > carga_atual) {
        quantidadeLiberada = carga_atual; 
    
   
    carga_atual -= quantidadeLiberada;
    
   
    caminhaoGrande.receberLixo(quantidadeLiberada); 
    
  
    System.out.println("Caminhão " + id_caminhao_pequeno + " reduziu sua carga em " + quantidadeLiberada + 
                       " toneladas. Carga restante: " + carga_atual + " toneladas.");
}

}
    // public void atualizarTempo(long tempoPassadoEmMs) {
    //     tempoEsperadoEmMs += tempoPassadoEmMs;
    // }
    
    // public void verificarTempoLimite() {
    //     if (tempoMaximoEsperado()) {
    //         System.out.println("Caminhão " + id_caminhao_pequeno + " excedeu o tempo máximo de espera.");
    //     } else {
    //         System.out.println("Caminhão " + id_caminhao_pequeno + " ainda tem tempo disponível.");
    //     }
    // }

    public void coletarLixo(int quantidade){
        int espaco_disponivel = capacidade - carga_atual;
        carga_atual += Math.min(quantidade, espaco_disponivel);
    }
    
    public boolean tempoMaximoEsperado() {
        return tempo_espera_acumulado >= tempo_max_espera;
    }

  
    

}

