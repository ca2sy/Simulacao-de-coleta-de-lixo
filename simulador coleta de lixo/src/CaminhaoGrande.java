public class CaminhaoGrande {
    // Variáveis de carga
    public int capacidade_maxima = 20;
    public int carga_atual;
    public int espaco;

    // Variável de identificação
    public String id_caminhao_grande;

    // Variáveis de tempo
    public int tolerancia_minutos = 30;
    public int tempo_inicio_espera = 0; 
    public int tempo_espera_acumulado = 0; 
    public int tempo_restante_viagem = 0;
    public int tempo_viagem_retorno = 0;
    public int tempo_viagem_ida = 0;

    // Variáveis de localização
    public EstacaoTransferencia estacao;

 

    // Atributos de estado
    public boolean em_viagem_ao_aterro = false;
    public boolean esta_no_aterro = false;
    public boolean em_retorno_do_aterro = false;
    public boolean esta_na_estacao = true;
    public boolean descarregou = false;
  

    public CaminhaoGrande(String id_caminhao_grande, EstacaoTransferencia estacao) {
        this.carga_atual = 0;
        this.espaco = this.capacidade_maxima;
        this.id_caminhao_grande = id_caminhao_grande;
        this.estacao = estacao;
       
    }

    // Getters
    public String getIdCaminhaoGrande() {
        return id_caminhao_grande;
    }

    public int getCapacidademaxima() {
        return capacidade_maxima;
    }

    public int getCargaAtual() {
        return carga_atual;
    }

    public int getTempoEsperaAcumulado() {
        return tempo_espera_acumulado;
    }

    public int getToleranciaEmMinutos() {
        return tolerancia_minutos;
    }

    public EstacaoTransferencia getEstacaoAtual() {
        return estacao;
    }

    public boolean estaNaEstacao() {
        return estacao != null;
    }

    // Métodos de carga/descarga
   

     public void receberLixo(int quantidade) {
        int capacidadeDisponivel = capacidade_maxima - carga_atual;
        int quantidadeRecebida = Math.min(quantidade, capacidadeDisponivel);

        carga_atual += quantidadeRecebida;
        espaco = capacidade_maxima - carga_atual;

    }

    public void descargaNoAterro() {
       
        carga_atual = 0;
        espaco = capacidade_maxima;
        descarregou = true;
    }

    // Métodos de movimentação
    public void iniciarViagemAoAterro(boolean horarioPico) {
        this.em_viagem_ao_aterro = true;

        if(horarioPico){
          this.tempo_restante_viagem =  this.estacao.tempo_viagem_aterro_pico;

        } else {
            this.tempo_restante_viagem =  this.estacao.tempo_viagem_aterro_normal;
        }
       
        this.esta_na_estacao = false;
        
    }

   
}