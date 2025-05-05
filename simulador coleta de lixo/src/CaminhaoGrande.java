public class CaminhaoGrande { //O caminhao grande deve coletar lixo de caminhoes pequenos e levar pro aterro

    public static final int capacidade_maxima = 20; //ele tem capacidade maxima padrão de 20 toneladas
    public static final long tolerancia_em_ms = 40 * 60 * 1000; // 40 minutos em milissegundos. tolerancia de espera de caminhoes pequenos na estação

    public int carga_atual; //carga que ele atualmente esta carregando (não pode ser maior que capacidade máxima)

    public String id_caminhao_grande; //diferencia todos os caminhoes grandes. cada um vai ter id proprio

    private long tempoEsperadoEmMs; // tempo em milissegundos. tempo que ele esperou na estação. se for maior do que o tempo de tolerancia, incluir lógica de ir embora

    private long tempoUltimaAtualizacao; // Tempo da última atualização (ajuda a calcular quanto passou de tempo)

    public CaminhaoGrande(String id_caminhao_grande) { //construtor
        this.carga_atual = 0;
        this.tempoEsperadoEmMs = 0;
        this.tempoUltimaAtualizacao = 0;
        this.id_caminhao_grande = id_caminhao_grande;
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

    // Atualiza o tempo do caminhão (a cada avanço do tempo)
    public void atualizarTempo(long tempoPassadoEmMs) {
        tempoEsperadoEmMs += tempoPassadoEmMs;
        
        // verifica se já atingiu o limite de tolerância e deve sair
        if (tempoEsperadoEmMs >= tolerancia_em_ms) {
            if (carga_atual > 0) {
                System.out.println("Tempo de espera excedido. Caminhão " + id_caminhao_grande + " com carga, indo para aterro.");
                descargaNoAterro();
            } else {
                System.out.println("Tempo de espera excedido, mas caminhão " + id_caminhao_grande + " vazio."); //se ele não tiver carga, fica esperando ate receber e dai vai embora
            }
        }
    }

    // Método para carregar o caminhão
    public int receberLixo(int quantidade) {
        int espaco_disponivel = capacidade_maxima - carga_atual;
        int lixo_carregado = Math.min(quantidade, espaco_disponivel); //ele recebe oq da de receber no momento
        carga_atual += lixo_carregado;

        System.out.println("Caminhão " + id_caminhao_grande + " carregou " + lixo_carregado + " toneladas. Carga atual: " + carga_atual + " toneladas.");

        return lixo_carregado; //quantidade que o caminhao grande pegou, ou seja, quantidade que o caminhao pequeno nao vai ter mais
    }

    //caminhao grande vai pro aterro e volta zero bala
    public void descargaNoAterro() {
        int lixo_descarregado = carga_atual;
        tempoEsperadoEmMs = 0;
        carga_atual = 0;
        System.out.println("Caminhão " + id_caminhao_grande + " descarregou " + lixo_descarregado + " toneladas no aterro.");
 
       
    }

    public void irParaEstacao(EstacaoTransferencia estacao){
        estacao.adicionarCaminhaoGrande(this); // Adiciona o caminhão à fila da estação
        this.tempoEsperadoEmMs = 0; // Resetando o tempo de espera ao chegar
        System.out.println("Caminhão " + id_caminhao_grande + " foi para " + estacao.getNome() + ".");
    }
}

