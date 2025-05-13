public class CaminhaoPequeno {
    // Atributos de capacidade
    public int capacidade;
    public int carga_atual;

    // Atributos de operação
    public Fila<Zona> zonas_de_atuacao;
    public Zona zona_atual;
   

    // Atributos de identificação
    public String id_caminhao_pequeno;

    // Atributos de viagem
    public int num_viagens_realizadas;
    public int num_viagens_a_realizar;
    public EstacaoTransferencia ultimaEstacaoVisitada = null;

    // zona a ir

    public Zona zona_a_ir;
    

    // Atributos de estação
    public EstacaoTransferencia estacao_a_ir; //estação q ta indo
    public EstacaoTransferencia estacao_atual;

    
    //controle

    public boolean esta_na_estacao = false;
    public boolean esta_na_zona = false;
    public boolean coletou = false;
    public boolean vai_coletar = true;
    public boolean esta_indo_pra_estacao = false;
    public boolean esta_indo_pra_zona;
    

    public boolean esta_coletando;

    // Atributos de tempo (em minutos)
    public int tempo_restante_viagem;
    public int tempo_inicio_espera;
    public int tempo_espera_acumulado;
    public int tempo_max_espera = 20;
    public int tempo_viagem_ida;
    public int tempo_viagem_volta;

    public CaminhaoPequeno(int capacidade, Fila<Zona> zonas_de_atuacao, String id_caminhao_pequeno,
            int numeros_viagem_a_realizar) {
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

    public int getTempoMaxEspera() {
        return tempo_max_espera;
    }

    public void liberarCarga(CaminhaoGrande caminhaoGrande) {
        int espacoDisponivelCaminhaoGrande = caminhaoGrande.capacidade_maxima - caminhaoGrande.getCargaAtual();
        int quantidadeLiberada = Math.min(this.carga_atual, espacoDisponivelCaminhaoGrande);

        if (quantidadeLiberada > carga_atual) {
            quantidadeLiberada = carga_atual;
        }
        carga_atual -= quantidadeLiberada;
        caminhaoGrande.receberLixo(quantidadeLiberada);
        
    }

    public void coletarLixo(int quantidade) {
        int espaco_disponivel = capacidade - carga_atual;
         carga_atual += Math.min(quantidade, espaco_disponivel);
       
    }

    public boolean tempoMaximoEsperado() {
        return tempo_espera_acumulado >= tempo_max_espera;
    }
}