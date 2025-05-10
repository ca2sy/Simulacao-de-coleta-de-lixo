public class CaminhaoGrande {

    public  int capacidade_maxima = 20; 
    public  long tolerancia_em_ms = 40 * 60 * 1000; 
    public int carga_atual; 
    public String id_caminhao_grande; 
    public long tempoEsperadoEmMs; 
    public EstacaoTransferencia estacao_atual; 
    public int espaco;
    public boolean emViagemAoAterro = false;
    public long tempoRestanteViagem = 0;
    public EstacaoTransferencia estacaoOrigem;
    public boolean coletando;
    public boolean estaNoAterro;


    public CaminhaoGrande(String id_caminhao_grande, EstacaoTransferencia estacaoOrigem) {
        this.carga_atual = 0;
        this.tempoEsperadoEmMs = 0;
        this.estacaoOrigem = estacaoOrigem;
        
        this.id_caminhao_grande = id_caminhao_grande;
        this.estacao_atual = null;
    }

    public String getIdCaminhaoGrande() {
        return id_caminhao_grande;
    }

    public int getCapacidademaxima() {
        return capacidade_maxima;
    }

    public int getCargaAtual() {
        return carga_atual;
    }

    public long getTempoEsperadoEmMs() {
        return tempoEsperadoEmMs;
    }

    public long getToleranciaEmMs() {
        return tolerancia_em_ms;
    }

    public EstacaoTransferencia getEstacaoAtual() {
        return estacao_atual;
    }

    public boolean estaNaEstacao() {
        return estacao_atual != null;
    }

    public void atualizarTempo(long tempoPassadoEmMs) {
        tempoEsperadoEmMs += tempoPassadoEmMs;

        if (tempoEsperadoEmMs >= tolerancia_em_ms) {
            if (carga_atual > 0) {
                System.out.println("Tempo de espera excedido. Caminhão " + id_caminhao_grande + " com carga, indo para aterro.");
                descargaNoAterro();
            } else {
                System.out.println("Tempo de espera excedido, mas caminhão " + id_caminhao_grande + " vazio.");
            }
        }
    }

 public void receberLixo(int quantidade) {
    carga_atual += quantidade;

    System.out.println("Caminhão " + id_caminhao_grande + " carregou " + quantidade + " toneladas. Carga atual: " + carga_atual + " toneladas.");

    
    if (carga_atual >= 20) {
        System.out.println("Caminhão " + id_caminhao_grande + " atingiu a carga máxima de 20 toneladas e irá para o aterro.");
        descargaNoAterro(); 
    }
}


    public void descargaNoAterro() {
        int lixo_descarregado = carga_atual;
        tempoEsperadoEmMs = 0;
        carga_atual = 0;
        estacao_atual = null; 
        System.out.println("Caminhão " + id_caminhao_grande + " descarregou " + lixo_descarregado + " toneladas no aterro.");
    }

    public void irParaEstacao(EstacaoTransferencia estacao) {
        estacao.adicionarCaminhaoGrande(this);
        this.estacao_atual = estacao; 
        this.tempoEsperadoEmMs = 0;
        System.out.println("Caminhão " + id_caminhao_grande + " foi para " + estacao.getNome() + ".");
    }

    public void iniciarViagemAoAterro(EstacaoTransferencia estacao, boolean horarioPico) {
        this.emViagemAoAterro = true;
        this.estaNoAterro = false;
        this.estacaoOrigem = estacao;
        this.tempoRestanteViagem = horarioPico ? 
            estacao.tempo_viagem_aterro_pico : 
            estacao.tempo_viagem_aterro_normal;
    }
    
    
    public void finalizarViagem() {
        this.emViagemAoAterro = false;
        this.espaco = capacidade_maxima; // Reset da capacidade ao voltar
        System.out.println("Caminhão " + id_caminhao_grande + " voltou do aterro para " + estacaoOrigem.getNome());
        estacaoOrigem.adicionarCaminhaoGrande(this); // Volta para a fila da estação
    }

 

}

