import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Simulacao {

    private InterfaceSimulacao interfaceSimulacao;

    // cidades e estações
    public Cidade teresina;
    public EstacaoTransferencia estacao_transferencia_a;
    public EstacaoTransferencia estacao_transferencia_b;

    // variaveis pra tempo
    public int hora_atual;
    private Timer timer;
    public int minutosSimulados;
    final long INTERVALO_REAL_MS = 1000; // 1 segundo = 1000ms
    final long MINUTOS_POR_DIA = 24 * 60;

    // variaveis pra guardar estatisticas

    public int totalCaminhoesGrandesAdicionados = 0;
    public int totalLixoColetado = 0;
    public int somaTemposViagemPequenos = 0;
    public int somaTemposViagemGrandes = 0;
    public int totalViagensPequenos = 0;
    public int totalViagensGrandes = 0;
    public int maxTempoEsperaPequenos = 0;
    public int somaTemposEsperaPequenos = 0;
    public int totalAtendimentosEstacoes = 0;
    public int totalLixoTransferido = 0;
    public int totalLixoDescarregado = 0;

    // Construtor: define quantidade de cada tipo de caminhão, cria as estações, as
    // zonas, inicializa a cidade e os caminhões

    public Simulacao(int qtd_pequenos_2, int qtd_pequenos_4, int qtd_pequenos_8, int qtd_pequenos_10, int qtd_grandes,
            int qtd_viagens) {
        this.inicializaCidade();
        this.criarEstacoes();
        this.criarZonas(5, 20, 30);
        this.zonaEstacoes();
        this.inicializarCaminhoes(qtd_pequenos_2, qtd_pequenos_4, qtd_pequenos_8, qtd_pequenos_10, qtd_grandes,
                qtd_viagens);
        hora_atual = 0;

    }

    // inicia o timer da simulação
    private void iniciarSimulacao() {
        timer = new Timer();

        final long INTERVALO_MINUTO_SIMULADO = 1000; // 1 segundo real = 1 minuto simulado (isso demora muito, qual
                                                     // seria o tempo ideal?)

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                minutosSimulados++; // incrementa 1 minuto a cada execução

                // converte minutos para horas (pra mostrar as 24 horas, fica mais facil de
                // visualizar acredito eu)

                hora_atual = minutosSimulados / 60;

                // já passou de 24 horas (1440 em minutos) para a simulação
                if (minutosSimulados > 1440) {
                    pararSimulacao();
                } else {

                    System.out.println("Hora atual simulada: " + hora_atual + "h (" +
                            minutosSimulados + " minutos)");

                    teresina.avancarTempoEmMinutos(1); // 1 minuto

                    // chamando meus metodos
                    verificarCaminhoesPequenosEncerraram();
                    verificarCaminhoesPequenosComecando();
                    caminhaoPequenoColeta();
                    caminhaoPequenoIrPraEstacao();
                    atualizarMovimentoCaminhoes();
                    processarCaminhoesPequenos();
                    adicionarCaminhaoGrande();
                    caminhaoPequenoVoltarColeta();
                    processarCaminhoesGrandes();
                    CaminhoesChegamAoAterro();
                    atualizarViagensAoAterro();

                    // 1. verificar quais caminhões pequenos encerraram suas atividades (FEITO)
                    // 2. verificar quais caminhões pequenos iniciam suas atividades (FEITO)
                    // 3. caminhões pequenos que chegaram às suas zonas, precisam ser carregados
                    // (FEITO)
                    // 4. caminhões pequenos que precisam descarregar vão para as estações de
                    // transferencia (FEITO)
                    // 5. caminhões pequenos nas estações de transferência precisam descarregar
                    // se for possível, descarrega, caso contrário, aguarda (processar a fila de
                    // descarga de caminhões pequenos) (FEITO?)
                    // 6. verificar os tempos de espera nas estações de transferência e decidir se é
                    // o momento de adicionar um caminhão grande ou não (FEITO?)
                    // 7. processar fila de caminhõe grandes nas estações de transferência (FEITO?)
                    // 8. verificar quais camionhões grandes chegaram no aterro para descarregar
                    // (feito?)
                    // 9. atualiza o movimento de cada caminhão que está no mapa. (feito?)

                }
            }
        }, 0, INTERVALO_MINUTO_SIMULADO);
    }

    public void pararSimulacao() { // para simulação
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        System.out.println("Simulação parada pelo usuário.");
    }

    private void inicializaCidade() { // cria a cidade
        this.teresina = new Cidade();
    }

    private void criarEstacoes() { // cria estações

        // tempos de cada estação ate o aterro
        int tempoViagemNormalA = 30;
        int tempoViagemPicoA = 35;

        int tempoViagemNormalB = 35;
        int tempoViagemPicoB = 40;

        // criando as estações de transferência com os tempos de viagem até o aterro
        this.estacao_transferencia_a = new EstacaoTransferencia("Estação A", tempoViagemNormalA, tempoViagemPicoA);
        this.estacao_transferencia_b = new EstacaoTransferencia("Estação B", tempoViagemNormalB, tempoViagemPicoB);

        // adicionando as estações à cidade
        teresina.estacoes_transferencia.adicionar(estacao_transferencia_a);
        teresina.estacoes_transferencia.adicionar(estacao_transferencia_b);
    }

    // criar as 5 zonas
    private void criarZonas(int qtd_zonas, int tempo_maximo, int tempo_minimo) {
        for (int i = 0; i < qtd_zonas; i++) {

            EstacaoTransferencia estacao_descarga;
            int tempo_estacao;
            int tempo_estacao_pico;
            int tempo_min_viagem_normal = 10;
            int tempo_max_viagem_normal = 18;

            // define pra qual estaçao de transferencia a zona manda lixo
            if (i <= 2) {
                estacao_descarga = estacao_transferencia_a;
                tempo_estacao = (tempo_minimo + (int) (Math.random() * (tempo_maximo - tempo_minimo + 1)));
                tempo_estacao_pico = tempo_estacao + 20;

            } else {
                estacao_descarga = estacao_transferencia_b;
                tempo_estacao = (tempo_minimo + (int) (Math.random() * (tempo_maximo - tempo_minimo + 1)));
                tempo_estacao_pico = tempo_estacao + 20;

            }

            Zona z = new Zona(
                    "Zona " + i,
                    50, // lixo_minimo
                    90, // lixo_maximo
                    tempo_min_viagem_normal, // tempo_min_viagem_pico
                    tempo_max_viagem_normal, // tempo_max_viagem_pico
                    tempo_min_viagem_normal, // tempo_min_viagem_normal
                    tempo_max_viagem_normal, // tempo_max_viagem_normal
                    estacao_descarga,
                    tempo_estacao, // tempo_viagem_estacao_normal
                    tempo_estacao_pico // tempo_viagem_estacao_pico
            );
            teresina.zonas.adicionar(z);
        }
    }

    // define em qual zona a estação esta
    private void zonaEstacoes() {
        int total_zonas = teresina.zonas.tamanho;
        Zona[] arrayZonas = new Zona[total_zonas];
        ListaEncadeada.No<Zona> atual = teresina.zonas.head;

        for (int i = 0; i < total_zonas; i++) {
            arrayZonas[i] = atual.dado;
            atual = atual.prox;
        }

        estacao_transferencia_a.localizacao = arrayZonas[2];
        estacao_transferencia_b.localizacao = arrayZonas[4];
    }

    // cria caminhões de diferentes cargas
    private void inicializarCaminhoes(int qtd_pequenos_2, int qtd_pequenos_4, int qtd_pequenos_8, int qtd_pequenos_10,
            int qtd_grandes, int qtd_viagens) {
        Random random = new Random();
        int total_zonas = teresina.zonas.tamanho;

        Zona[] arrayZonas = new Zona[total_zonas];
        ListaEncadeada.No<Zona> atual = teresina.zonas.head;

        for (int i = 0; i < total_zonas; i++) {
            arrayZonas[i] = atual.dado;
            atual = atual.prox;
        }

        // inicio caminhoes de capacidade 2
        for (int i = 0; i < qtd_pequenos_2; i++) {
            int qtd_viagens_sorteada = random.nextInt(qtd_viagens) + 1;
            Fila<Zona> fila_zonas_caminhao_pequeno = new Fila<>();
            for (int b = 0; b < qtd_viagens_sorteada; b++) {
                int indiceAleatorio = random.nextInt(total_zonas);
                fila_zonas_caminhao_pequeno.enfileirar(arrayZonas[indiceAleatorio]);
            }

            CaminhaoPequeno cp = new CaminhaoPequeno(2, fila_zonas_caminhao_pequeno, "CaminhaoP2_" + i,
                    qtd_viagens_sorteada);
            teresina.caminhoes_pequenos.adicionar(cp);
        }

        // Inicializando os caminhões pequenos com capacidades 4
        for (int i = 0; i < qtd_pequenos_4; i++) {
            int qtd_viagens_sorteada = random.nextInt(qtd_viagens) + 1;
            Fila<Zona> fila_zonas_caminhao_pequeno = new Fila<>();
            for (int b = 0; b < qtd_viagens_sorteada; b++) {
                int indiceAleatorio = random.nextInt(total_zonas);
                fila_zonas_caminhao_pequeno.enfileirar(arrayZonas[indiceAleatorio]);
            }
            CaminhaoPequeno cp = new CaminhaoPequeno(4, fila_zonas_caminhao_pequeno, "CaminhaoP4_" + i,
                    qtd_viagens_sorteada);
            teresina.caminhoes_pequenos.adicionar(cp);

        }

        // Inicializando os caminhões pequenos com capacidades 8
        for (int i = 0; i < qtd_pequenos_8; i++) {
            int qtd_viagens_sorteada = random.nextInt(qtd_viagens) + 1;
            Fila<Zona> fila_zonas_caminhao_pequeno = new Fila<>();
            for (int b = 0; b < qtd_viagens_sorteada; b++) {
                int indiceAleatorio = random.nextInt(total_zonas);
                fila_zonas_caminhao_pequeno.enfileirar(arrayZonas[indiceAleatorio]);
            }
            CaminhaoPequeno cp = new CaminhaoPequeno(8, fila_zonas_caminhao_pequeno, "CaminhaoP8_" + i,
                    qtd_viagens_sorteada);
            teresina.caminhoes_pequenos.adicionar(cp);
        }

        // Inicializando os caminhões pequenos com capacidades 10
        for (int i = 0; i < qtd_pequenos_10; i++) {
            int qtd_viagens_sorteada = random.nextInt(qtd_viagens) + 1;
            Fila<Zona> fila_zonas_caminhao_pequeno = new Fila<>();
            for (int b = 0; b < qtd_viagens_sorteada; b++) {
                int indiceAleatorio = random.nextInt(total_zonas);
                fila_zonas_caminhao_pequeno.enfileirar(arrayZonas[indiceAleatorio]);
            }
            CaminhaoPequeno cp = new CaminhaoPequeno(10, fila_zonas_caminhao_pequeno, "CaminhaoP10_" + i,
                    qtd_viagens_sorteada);
            teresina.caminhoes_pequenos.adicionar(cp);
        }

        // inicializando caminhões grandes
        for (int i = 0; i < qtd_grandes; i++) {
            if (i % 2 == 0) {
                CaminhaoGrande cg = new CaminhaoGrande("Caminhao Grande " + i, estacao_transferencia_a);
                teresina.caminhoes_grandes.adicionar(cg);
                estacao_transferencia_a.fila_caminhao_grande.enfileirar(cg);
            } else {
                CaminhaoGrande cg = new CaminhaoGrande("Caminhao Grande " + i, estacao_transferencia_b);
                teresina.caminhoes_grandes.adicionar(cg);
                estacao_transferencia_b.fila_caminhao_grande.enfileirar(cg);
            }
        }
    }

    public void setInterface(InterfaceSimulacao interfaceSimulacao) {
        this.interfaceSimulacao = interfaceSimulacao;
    }

    // REFAZER TUDO APARTIR DAQUI!!!!!!!!!!!

    // verificar quais caminhões pequenos encerraram suas atividades
    public void verificarCaminhoesPequenosEncerraram() {
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while (atual != null) {
            CaminhaoPequeno caminhao = atual.dado;
            ListaEncadeada.No<CaminhaoPequeno> proximo = atual.prox;

            // se o caminhao nao tiver zonas pra coletar, se ele nao tiver indo pra nenhuma
            // estação e a carga tiver zerada, o caminhao nao tem mais trabalho
            boolean terminouTrabalho = (caminhao.zonas_de_atuacao == null || caminhao.zonas_de_atuacao.estaVazia()) &&
                    (caminhao.estacao_atual == null) &&
                    (caminhao.carga_atual == 0);

            if (terminouTrabalho) {
                System.out.println("\n[CAMINHÃO PEQUENO PARANDO] Caminhão " + caminhao.id_caminhao_pequeno +
                        " completou todas as coletas e descargas. Saindo da simulação.");
                teresina.caminhoes_pequenos.remover(caminhao);
            }

            atual = proximo;
        }
    }

    public void verificarCaminhoesPequenosComecando() {
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while (atual != null) {

            // caso o numero de viagens realizadas for 0, ele começa em uma zona. como
            // teoricamente ele foi pra aquela zona de algum lugar, eu ja aumento aqui o
            // numero de viagens.
            if (atual.dado.num_viagens_realizadas == 0 && atual.dado.zonas_de_atuacao != null
                    && atual.dado.zonas_de_atuacao.head != null) {
                System.out.println("[CAMINHÃO PEQUENO COMEÇANDO] Caminhão " + atual.dado.id_caminhao_pequeno
                        + "Está começando agora na zona " + atual.dado.zonas_de_atuacao.head.dado.getNome() + ".");
                atual.dado.num_viagens_realizadas++;
                atual.dado.esta_coletando = true;
                atual.dado.zona_atual = atual.dado.zonas_de_atuacao.head.dado;
            }
            atual = atual.prox;
        }

    }

    // Aqui é onde o caminhão de fato coleta o lixo da zona atual
    // Primeiro eu vejo o quanto de carga ele ainda pode receber e quanto lixo tem
    // na zona
    // Recolhe o que puder, sem ultrapassar o limite do caminhão
    // Atualizo os dados da zona e do caminhão e registro a coleta
    // Por fim, desativo a coleta porque ele vai precisar descarregar antes de
    // voltar

    public void caminhaoPequenoColeta() {
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while (atual != null) {
            CaminhaoPequeno caminhao = atual.dado;

            // se ele tiver uma zona atual e estiver coletando, ele vai coletar. ele so deve coletar se o tempo de viagem dele restante for zero (chegou a zona)
            if (caminhao.zona_atual != null && caminhao.esta_coletando  && caminhao.tempo_restante_viagem == 0) {
                int capacidadeRestante = caminhao.capacidade - caminhao.carga_atual;
                int lixoDisponivel = caminhao.zona_atual.lixo_atual;

                // pega o minimo dos dois. se uma zona tiver menos lixo doq o caminhao pode
                // carregar, o caminhao vai recolher o lixo e ir por na estação. caso a zona
                // tenha mais lixo, o caminhao so vai pegar oq pode carregar
                int quantidadeColetada = Math.min(capacidadeRestante, lixoDisponivel);

                if (quantidadeColetada > 0) {
                    caminhao.coletarLixo(quantidadeColetada);
                    caminhao.zona_atual.LixoColetado(quantidadeColetada);
                    totalLixoColetado += quantidadeColetada;

                    System.out.println("[COLETA DE LIXO] Caminhão " + caminhao.id_caminhao_pequeno +
                            " coletou " + quantidadeColetada + " toneladas na zona " +
                            caminhao.zona_atual.getNome());
                    System.out.println("[LIXO COLETADO] Lixo restante na zona " + caminhao.zona_atual.getNome() +
                            ": " + caminhao.zona_atual.lixo_atual + " toneladas");
                    caminhao.esta_coletando = false; // ele ja coletou, entao deve ir pra estação
                }
            }
            atual = atual.prox;
        }
    }

    public void caminhaoPequenoIrPraEstacao() {
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

        while (atual != null) {
            CaminhaoPequeno caminhao = atual.dado;

            // caminhão deve ir pr estação, ele nao pode ir vazio e nem pode estar coletando
            // mais.
            if (caminhao.carga_atual > 0 && !caminhao.esta_coletando && caminhao.estacao_a_ir == null && !caminhao.esta_na_estacao && caminhao.zona_a_ir == null) {
                boolean horarioPico = teresina.estaEmHorarioPico();
                int tempo_viagem;
                if (horarioPico) {
                    tempo_viagem = caminhao.zona_atual.tempo_viagem_estacao_pico;
                } else {
                    tempo_viagem = caminhao.zona_atual.tempo_viagem_estacao_normal;
                }

                caminhao.tempo_viagem_ida = tempo_viagem;
                caminhao.tempo_restante_viagem = tempo_viagem;
                caminhao.estacao_a_ir = caminhao.zona_atual.estacao_descarga; // Define estação como destino
                caminhao.esta_coletando = false;

                System.out.println("[IDA] Caminhão " + caminhao.id_caminhao_pequeno +
                        " indo para estação " + caminhao.estacao_a_ir.getNome() +
                        " | Tempo: " + tempo_viagem + " min");
            }

            atual = atual.prox;
        }
    }

   

    // Esse método atualiza o deslocamento dos caminhões, minuto a minuto
    // Se o caminhão estiver a caminho de algum lugar (zona ou estação), ele diminui
    // o tempo restante de viagem
    // Quando o tempo chega a 0, ele chega no destino
    // Se chegou numa estação, ele entra na fila para transferir carga
    // Se chegou na zona, volta a coletar
    // Também atualizo o tempo total de viagem e o número de viagens feitas
    // Se a zona estiver sem lixo, retiro ela da fila e passo para a próxima

    public void atualizarMovimentoCaminhoes() {
    ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
    while (atual != null) {
        CaminhaoPequeno caminhao = atual.dado;

        if ((caminhao.estacao_a_ir != null || caminhao.zona_a_ir != null) && !caminhao.esta_coletando && caminhao.tempo_espera_acumulado == 0) {
            if (caminhao.tempo_restante_viagem > 0) {
                caminhao.tempo_restante_viagem -= 1;
                System.out.println("Caminhão " + caminhao.id_caminhao_pequeno +
                        " a caminho: " + caminhao.tempo_restante_viagem +
                        " minutos restantes");
            }

            if (caminhao.tempo_restante_viagem <= 0) {
                if (caminhao.estacao_a_ir != null) {
                    // Chegou na estação 
                    somaTemposViagemPequenos += caminhao.tempo_viagem_ida;
                    caminhao.estacao_atual = caminhao.estacao_a_ir;
                    caminhao.esta_na_estacao = true;
                    caminhao.estacao_a_ir = null;
                    System.out.println("[CHEGOU-ESTAÇÃO] Caminhão " + caminhao.id_caminhao_pequeno +
                            " completou viagem de ida em " + caminhao.tempo_viagem_ida + " minutos");

                    if (caminhao.estacao_atual instanceof EstacaoTransferencia) {
                        caminhao.estacao_atual.fila_caminhao_pequeno.enfileirar(caminhao);
                    }
                } else if (caminhao.zona_a_ir != null) {
                    // Chegou na zona
                    caminhao.zona_atual = caminhao.zona_a_ir;
                    caminhao.zona_a_ir = null;
                    caminhao.esta_coletando = true;
                    System.out.println("[CHEGOU-ZONA] Caminhão " + caminhao.id_caminhao_pequeno +
                            " chegou na zona " + caminhao.zona_atual.getNome());
                }
                totalViagensPequenos++;
            }
        }
        atual = atual.prox;
    }
}
    

    public void processarCaminhoesPequenos() {
        ListaEncadeada.No<EstacaoTransferencia> estacaoAtual = teresina.estacoes_transferencia.head;

        while (estacaoAtual != null) {
            EstacaoTransferencia estacao = estacaoAtual.dado;

            if (!estacao.fila_caminhao_pequeno.estaVazia()) {
                CaminhaoPequeno caminhaoPequeno = estacao.fila_caminhao_pequeno.espiar();

                if (caminhaoPequeno.tempo_inicio_espera == 0) {
                    caminhaoPequeno.tempo_inicio_espera = minutosSimulados;
                }
                caminhaoPequeno.tempo_espera_acumulado = minutosSimulados - caminhaoPequeno.tempo_inicio_espera;

                boolean podeAtender = false;
                if (!estacao.fila_caminhao_grande.estaVazia()) {
                    CaminhaoGrande caminhaoGrande = estacao.fila_caminhao_grande.espiar();
                    podeAtender = (!caminhaoGrande.coletando && !caminhaoGrande.em_viagem_ao_aterro);
                }

                if (podeAtender) {
                    CaminhaoGrande caminhaoGrande = estacao.fila_caminhao_grande.espiar();
                    int quantidade = Math.min(caminhaoPequeno.carga_atual, caminhaoGrande.espaco);
                    caminhaoPequeno.liberarCarga(caminhaoGrande);
                    totalLixoTransferido += quantidade;

                    caminhaoGrande.coletando = true;
                    caminhaoPequeno.tempo_inicio_espera = 0;
                    caminhaoPequeno.tempo_espera_acumulado = 0;
                    caminhaoPequeno.ultimaEstacaoVisitada = estacao;

                    if (caminhaoPequeno.carga_atual == 0) {
                        // Remove da fila da estação
                        estacao.fila_caminhao_pequeno.desenfileirar();
                        caminhaoPequeno.tempo_espera_acumulado = 0;
                        
                        


                       
                            }
                        } else {
                    // Atualiza estatísticas de espera
                    if (caminhaoPequeno.tempo_espera_acumulado > maxTempoEsperaPequenos) {
                        maxTempoEsperaPequenos = caminhaoPequeno.tempo_espera_acumulado;
                    }
                    somaTemposEsperaPequenos += caminhaoPequeno.tempo_espera_acumulado;
                    totalAtendimentosEstacoes++;

                    if (caminhaoPequeno.tempo_espera_acumulado >= 20) {
                        System.out.println("[ALERTA] Tempo de espera excedido para " +
                                caminhaoPequeno.id_caminhao_pequeno);
                    }
                }
            }
            estacaoAtual = estacaoAtual.prox;
        }
    }

     public void caminhaoPequenoVoltarColeta() {
    ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

    while (atual != null) {
        CaminhaoPequeno caminhaoPequeno = atual.dado;
        
        // Caminhão está em estação, sem carga e não está coletando
        if (caminhaoPequeno.estacao_atual != null && caminhaoPequeno.carga_atual == 0 && !caminhaoPequeno.esta_coletando) {
            // Verifica se a zona atual ainda tem lixo
            if (caminhaoPequeno.zona_atual != null && caminhaoPequeno.zona_atual.lixo_atual > 0) {
                // Volta para a mesma zona
                boolean horarioPico = teresina.estaEmHorarioPico();
                caminhaoPequeno.tempo_viagem_volta = horarioPico ? 
                    caminhaoPequeno.zona_atual.tempo_viagem_estacao_pico : 
                    caminhaoPequeno.zona_atual.tempo_viagem_estacao_normal;
                
                caminhaoPequeno.tempo_restante_viagem = caminhaoPequeno.tempo_viagem_volta;
                caminhaoPequeno.zona_a_ir = caminhaoPequeno.zona_atual;
                caminhaoPequeno.estacao_atual = null;
                
                System.out.println("Caminhão " + caminhaoPequeno.id_caminhao_pequeno +
                        " retornando para " + caminhaoPequeno.zona_atual.getNome());
                caminhaoPequeno.esta_na_estacao = false;
            } else {
                // Zona está vazia, vai para próxima zona ou encerra
                if (caminhaoPequeno.zonas_de_atuacao != null && !caminhaoPequeno.zonas_de_atuacao.estaVazia()) {
                    caminhaoPequeno.zonas_de_atuacao.desenfileirar();
                    
                    if (!caminhaoPequeno.zonas_de_atuacao.estaVazia()) {
                        caminhaoPequeno.zona_atual = caminhaoPequeno.zonas_de_atuacao.espiar();
                        boolean horarioPico = teresina.estaEmHorarioPico();
                        caminhaoPequeno.tempo_viagem_volta = horarioPico ? 
                            caminhaoPequeno.zona_atual.tempo_viagem_estacao_pico : 
                            caminhaoPequeno.zona_atual.tempo_viagem_estacao_normal;
                        
                        caminhaoPequeno.tempo_restante_viagem = caminhaoPequeno.tempo_viagem_volta;
                        caminhaoPequeno.zona_a_ir = caminhaoPequeno.zona_atual;
                        caminhaoPequeno.estacao_atual = null;
                        
                        System.out.println("Caminhão " + caminhaoPequeno.id_caminhao_pequeno +
                                " indo para nova zona " + caminhaoPequeno.zona_atual.getNome());
                        caminhaoPequeno.esta_na_estacao = false;
                    }
                }
            }
        }
        atual = atual.prox;
    }
}

    public void processarCaminhoesGrandes() {
        // Percorre todas as estações de transferência
        ListaEncadeada.No<EstacaoTransferencia> estacaoAtual = teresina.estacoes_transferencia.head;
        boolean horarioPico = teresina.estaEmHorarioPico();

        while (estacaoAtual != null) {
            EstacaoTransferencia estacao = estacaoAtual.dado;

            // Verifica se há caminhões grandes esperando na estação
            if (!estacao.fila_caminhao_grande.estaVazia()) {
                CaminhaoGrande caminhaoGrande = estacao.fila_caminhao_grande.espiar();

                // Registra o início da espera se ainda não foi feito
                if (caminhaoGrande.tempo_inicio_espera == 0) {
                    caminhaoGrande.tempo_inicio_espera = minutosSimulados;
                }

                // Atualiza o tempo de espera acumulado
                caminhaoGrande.tempo_espera_acumulado = minutosSimulados - caminhaoGrande.tempo_inicio_espera;

                // Verifica condições para o caminhão grande ser enviado ao aterro
                int cargaAtual = caminhaoGrande.capacidade_maxima - caminhaoGrande.espaco;
                boolean deveIrParaAterro = false;

                // Vai ao aterro se estiver cheio ou se a espera exceder o tempo máximo
                if (caminhaoGrande.coletando) {
                    deveIrParaAterro = (cargaAtual == caminhaoGrande.capacidade_maxima) ||
                            (caminhaoGrande.tempo_espera_acumulado >= caminhaoGrande.tolerancia_minutos
                                    && cargaAtual > 0);
                }

                if (deveIrParaAterro && cargaAtual > 0) {
                    // Remove da fila, inicia a viagem ao aterro e atualiza estatísticas
                    estacao.fila_caminhao_grande.desenfileirar();
                    caminhaoGrande.iniciarViagemAoAterro(estacao, horarioPico);
                    totalViagensGrandes++;
                    somaTemposViagemGrandes += (horarioPico ? caminhaoGrande.estacaoOrigem.tempo_viagem_aterro_pico
                            : caminhaoGrande.estacaoOrigem.tempo_viagem_aterro_normal);

                    caminhaoGrande.coletando = false;
                    caminhaoGrande.tempo_inicio_espera = 0;
                    caminhaoGrande.tempo_espera_acumulado = 0;

                    System.out.println("Caminhão " + caminhaoGrande.id_caminhao_grande +
                            " indo para o aterro com " + cargaAtual + " toneladas" +
                            (caminhaoGrande.tempo_espera_acumulado >= caminhaoGrande.tolerancia_minutos
                                    ? " [TEMPO DE ESPERA EXCEDIDO]"
                                    : ""));
                }
            }
            estacaoAtual = estacaoAtual.prox;
        }
    }

    public void atualizarViagensAoAterro() {
    // Atualiza os caminhões que estão em viagem ao aterro
    ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;

    while (atual != null) {
        CaminhaoGrande caminhao = atual.dado;

        if (caminhao.em_viagem_ao_aterro) {
            caminhao.tempo_restante_viagem -= 1; // Avança o tempo da viagem

            // Se chegou ao aterro e ainda não descarregou
            if (caminhao.tempo_restante_viagem <= 0 && !caminhao.esta_no_aterro) {
                // Descarrega no aterro
                int cargaDescarregada = caminhao.capacidade_maxima - caminhao.espaco;
                if (cargaDescarregada > 0) {
                    caminhao.descargaNoAterro();
                    totalLixoDescarregado += cargaDescarregada;
                    System.out.println("Caminhão " + caminhao.id_caminhao_grande +
                            " descarregou " + cargaDescarregada + " toneladas no aterro");
                }
                
                // Configura a viagem de volta
                boolean horarioPico = teresina.estaEmHorarioPico();
                caminhao.tempo_restante_viagem = horarioPico ? 
                    caminhao.estacaoOrigem.tempo_viagem_aterro_pico : 
                    caminhao.estacaoOrigem.tempo_viagem_aterro_normal;
                caminhao.esta_no_aterro = true;
                
                System.out.println("Caminhão " + caminhao.id_caminhao_grande +
                        " iniciando viagem de volta para " + caminhao.estacaoOrigem.getNome() +
                        " | Tempo: " + caminhao.tempo_restante_viagem + " min");
            } 
            // Se está voltando para a estação e chegou
            else if (caminhao.tempo_restante_viagem <= 0 && caminhao.esta_no_aterro) {
                caminhao.em_viagem_ao_aterro = false;
                caminhao.esta_no_aterro = false;
                caminhao.estacaoOrigem.fila_caminhao_grande.enfileirar(caminhao);
                
                System.out.println("Caminhão " + caminhao.id_caminhao_grande +
                        " retornou à estação " + caminhao.estacaoOrigem.getNome());
            }
            // Se ainda está viajando
            else if (caminhao.tempo_restante_viagem > 0) {
                System.out.println("Caminhão " + caminhao.id_caminhao_grande +
                        " em viagem: " + caminhao.tempo_restante_viagem +
                        " minutos restantes");
            }
        }
        atual = atual.prox;
    }
}
    public void adicionarCaminhaoGrande() {
        // Adiciona caminhão grande extra se houver caminhão pequeno esperando demais
        ListaEncadeada.No<EstacaoTransferencia> estacaoAtual = teresina.estacoes_transferencia.head;
        while (estacaoAtual != null) {
            EstacaoTransferencia estacao = estacaoAtual.dado;

            if (!estacao.fila_caminhao_pequeno.estaVazia()) {
                CaminhaoPequeno caminhaoPequeno = estacao.fila_caminhao_pequeno.espiar();

                // Se o tempo de espera do pequeno for muito alto, cria caminhão grande extra
                if (caminhaoPequeno.tempo_espera_acumulado >= caminhaoPequeno.tempo_max_espera) {
                    String novoId = "CG-Extra-" + System.currentTimeMillis() % 10000;
                    CaminhaoGrande novoCaminhao = new CaminhaoGrande(novoId, estacao);

                    estacao.fila_caminhao_grande.enfileirar(novoCaminhao);
                    teresina.caminhoes_grandes.adicionar(novoCaminhao);
                    totalCaminhoesGrandesAdicionados++;

                    System.out.println("ALERTA: Adicionado caminhão grande extra " + novoId +
                            " na " + estacao.getNome());

                    // Reinicia o tempo de espera do pequeno
                    caminhaoPequeno.tempo_inicio_espera = 0;
                    caminhaoPequeno.tempo_espera_acumulado = 0;
                }
            }
            estacaoAtual = estacaoAtual.prox;
        }
    }

    public void CaminhoesChegamAoAterro() {
        // Verifica se caminhões chegaram ao aterro e descarrega
        ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;

        while (atual != null) {
            CaminhaoGrande caminhao = atual.dado;

            if (caminhao.em_viagem_ao_aterro && caminhao.tempo_restante_viagem <= 0 && !caminhao.esta_no_aterro) {
                int cargaDescarregada = caminhao.capacidade_maxima - caminhao.espaco;

                // Se houver carga a ser descarregada, realiza a descarga
                if (cargaDescarregada > 0) {
                    caminhao.descargaNoAterro();
                    totalLixoDescarregado += cargaDescarregada;
                    System.out.println("Caminhão " + caminhao.id_caminhao_grande +
                            " descarregou " + cargaDescarregada + " toneladas no aterro");
                }

                // Prepara retorno da viagem
                boolean horarioPico = teresina.estaEmHorarioPico();
                caminhao.tempo_restante_viagem = horarioPico ? caminhao.estacaoOrigem.tempo_viagem_aterro_pico
                        : caminhao.estacaoOrigem.tempo_viagem_aterro_normal;
                caminhao.esta_no_aterro = true;
                caminhao.em_viagem_ao_aterro = false;
            }
            atual = atual.prox;
        }
    }

    public void exibirEstatisticas() {
        // Exibe resumo da simulação
        System.out.println("\n======== ESTATÍSTICAS DA SIMULAÇÃO ========");

        double mediaViagemPequenos = totalViagensPequenos > 0 ? (double) somaTemposViagemPequenos / totalViagensPequenos
                : 0;

        double mediaViagemGrandes = totalViagensGrandes > 0 ? (double) somaTemposViagemGrandes / totalViagensGrandes
                : 0;

        double mediaEsperaPequenos = totalAtendimentosEstacoes > 0
                ? (double) somaTemposEsperaPequenos / totalAtendimentosEstacoes
                : 0;

        System.out.println("ESTATÍSTICAS GERAIS:");
        System.out
                .println("- Tempo médio de viagem (pequenos): " + String.format("%.1f", mediaViagemPequenos) + " min");
        System.out.println("- Tempo médio ao aterro (grandes): " + String.format("%.1f", mediaViagemGrandes) + " min");
        System.out
                .println("- Tempo médio de espera (pequenos): " + String.format("%.1f", mediaEsperaPequenos) + " min");
        System.out.println("- Máximo tempo de espera: " + maxTempoEsperaPequenos + " min");
        System.out.println("- Caminhões grandes adicionados: " + totalCaminhoesGrandesAdicionados);
        System.out.println("- Lixo coletado total: " + totalLixoColetado + " ton");
        System.out.println("- Lixo transferido entre caminhões: " + totalLixoTransferido + " ton");
        System.out.println("- Lixo efetivamente descarregado: " + totalLixoDescarregado + " ton");
        System.out.println("- Total viagens pequenos: " + totalViagensPequenos);
        System.out.println("- Total viagens aterro: " + totalViagensGrandes);
    }

    public void executar() {
        // Início da simulação, imprime os dados iniciais
        System.out.println("Iniciando simulação do dia...\n");
        teresina.imprimeListaZonas();
        teresina.imprimeListaEstacoes();
        teresina.imprimeListaCaminhaoGrande();
        teresina.imprimeListaCaminhaoPequeno();
        exibirEstatisticas(); // Mostra estatísticas antes de iniciar
        iniciarSimulacao(); // Executa o ciclo de simulação
    }

}