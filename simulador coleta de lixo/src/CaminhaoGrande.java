public class CaminhaoGrande {

    public static final int capacidade_maxima = 20; 
    public static final long tolerancia_em_ms = 40 * 60 * 1000; 

    public int carga_atual; 
    public String id_caminhao_grande; 

    private long tempoEsperadoEmMs; 
  

    private EstacaoTransferencia estacao_atual; 

    public CaminhaoGrande(String id_caminhao_grande) {
        this.carga_atual = 0;
        this.tempoEsperadoEmMs = 0;
        
        this.id_caminhao_grande = id_caminhao_grande;
        this.estacao_atual = null;
    }

    public String getIdCaminhaoGrande() {
        return id_caminhao_grande;
    }

    public static int getCapacidademaxima() {
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

    public int receberLixo(int quantidade) {
        int espaco_disponivel = capacidade_maxima - carga_atual;
        int lixo_carregado = Math.min(quantidade, espaco_disponivel);
        carga_atual += lixo_carregado;

        System.out.println("Caminhão " + id_caminhao_grande + " carregou " + lixo_carregado + " toneladas. Carga atual: " + carga_atual + " toneladas.");

        return lixo_carregado;
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
}
