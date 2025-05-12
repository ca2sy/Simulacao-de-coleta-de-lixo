public class CaminhaoGrande {
    // Variáveis de carga
    public int capacidade_maxima = 20;
    public int carga_atual;
    public int espaco;

    // Variável de identificação
    public String id_caminhao_grande;

    // Variáveis de tempo
    public int tolerancia_minutos = 40;
    public int tempo_inicio_espera = 0; // Novo: tempo que começou a esperar
    public int tempo_espera_acumulado = 0; // Novo: tempo total de espera
    public int tempo_restante_viagem = 0;
    public int tempo_viagem_total_retorno = 0;
    public int tempo_no_aterro;
    public int tempo_transferencia_restante = 0;

    // Variáveis de localização
    public EstacaoTransferencia estacao_atual;
    public EstacaoTransferencia estacaoOrigem;

    // Atributos de estado
    public boolean em_viagem_ao_aterro = false;
    public boolean coletando;
    public boolean esta_no_aterro;
    public boolean em_retorno_do_aterro = false;
    public boolean em_transferencia = false;

    public CaminhaoGrande(String id_caminhao_grande, EstacaoTransferencia estacao_origem) {
        this.carga_atual = 0;
        this.espaco = this.capacidade_maxima;
        this.estacaoOrigem = estacao_origem;
        this.id_caminhao_grande = id_caminhao_grande;
        this.estacao_atual = null;
        this.coletando = false;
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
        return estacao_atual;
    }

    public boolean estaNaEstacao() {
        return estacao_atual != null;
    }

    // Métodos de controle de tempo
    public void atualizarTempoEspera(int minutosSimulados) {
        if (tempo_inicio_espera == 0 && coletando) {
            tempo_inicio_espera = minutosSimulados;
        }
        tempo_espera_acumulado = minutosSimulados - tempo_inicio_espera;
    }

    public boolean deveIrParaAterro() {
        return (carga_atual == capacidade_maxima) ||
                (tempo_espera_acumulado >= tolerancia_minutos && carga_atual > 0);
    }

    // Métodos de carga/descarga
    public void receberLixo(int quantidade) {
        if (!coletando) {
            coletando = true;
            tempo_inicio_espera = 0; // Reinicia o tempo de espera ao começar a coletar
        }

        int capacidadeDisponivel = capacidade_maxima - carga_atual;
        int quantidadeRecebida = Math.min(quantidade, capacidadeDisponivel);

        carga_atual += quantidadeRecebida;
        espaco = capacidade_maxima - carga_atual;

        System.out.println("Caminhão " + id_caminhao_grande + " recebeu " + quantidadeRecebida +
                " toneladas. Carga atual: " + carga_atual + "/" + capacidade_maxima);
    }

    public void descargaNoAterro() {
        int lixo_descarregado = carga_atual;
        carga_atual = 0;
        espaco = capacidade_maxima;
        tempo_inicio_espera = 0;
        tempo_espera_acumulado = 0;
        esta_no_aterro = true;
        coletando = false;

        System.out.println("Caminhão " + id_caminhao_grande + " descarregou " +
                lixo_descarregado + " toneladas no aterro.");
    }

    // Métodos de movimentação
    public void iniciarViagemAoAterro(EstacaoTransferencia estacao, boolean horarioPico) {
        this.em_viagem_ao_aterro = true;
        this.esta_no_aterro = false;
        this.estacaoOrigem = estacao;
        this.estacao_atual = null;
        this.tempo_restante_viagem = horarioPico ? estacao.tempo_viagem_aterro_pico
                : estacao.tempo_viagem_aterro_normal;

        System.out.println("Caminhão " + id_caminhao_grande + " iniciou viagem ao aterro (" +
                tempo_restante_viagem + " minutos)");
    }

    public void finalizarViagem() {
        this.em_viagem_ao_aterro = false;
        this.esta_no_aterro = false;
        this.estacao_atual = estacaoOrigem;
        this.tempo_inicio_espera = 0;
        this.tempo_espera_acumulado = 0;

        System.out.println("Caminhão " + id_caminhao_grande + " retornou para " +
                estacaoOrigem.getNome());
        estacaoOrigem.adicionarCaminhaoGrande(this);
    }
}