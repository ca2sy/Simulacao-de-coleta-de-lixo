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
    public int coletasRealizadas = 0;
    public int totalCaminhoesGrandesAdicionados = 0;
    public int totalLixoColetado = 0;
    public int totalViagensPequenos = 0;
    public int totalViagensGrandes = 0;
    public int somaTemposEsperaPequenos = 0;
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
                    processarCaminhoesPequenos();
                    adicionarCaminhaoGrande();
                    caminhaoPequenoVoltarColeta();
                    processarCaminhoesGrandes(); // PROBLEMA AQU (TIMER NAO ANDA)
                    CaminhoesChegamAoAterro();
                    atualizarMovimentoCaminhoesPequenos();
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
                    2, // lixo_minimo 120 //pra teste por 82 toneladas ou 600?
                    2, // lixo_maximo
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
            if (caminhao.zonas_de_atuacao.head == null && caminhao.carga_atual == 0 && caminhao.zona_a_ir == null) {
                // caso o caminhao nao tenha mais zonas pra ir e nao tenha mais carga (pq se
                // tivesse era pra ir pra estção), ele para

                System.out.println("\n[CAMINHÃO PEQUENO PARANDO] Caminhão " + caminhao.id_caminhao_pequeno
                        + " completou todas as coletas e descargas. Saindo da simulação.");
                System.out.println("");
                teresina.caminhoes_pequenos.remover(caminhao);
            }
            atual = atual.prox;
        }
    } // Nao funciona

    // aqui eu vejo que caminhoes acabaram de começar

    public void verificarCaminhoesPequenosComecando() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
            while (atual != null) {

                // caso o numero de viagens realizadas for 0, ele começa em uma zona. como
                // teoricamente ele foi pra aquela zona de algum lugar, eu ja aumento aqui o
                // numero de viagens.
                CaminhaoPequeno caminhao = atual.dado;
                if (caminhao.num_viagens_realizadas == 0) {
                    System.out.println("[CAMINHÃO PEQUENO COMEÇANDO] Caminhão " + atual.dado.id_caminhao_pequeno
                            + "Está começando agora na zona " + atual.dado.zonas_de_atuacao.head.dado.getNome() + ".");
                    System.out.println(" ");
                    caminhao.num_viagens_realizadas++; // aumenta o numero de viagens
                    caminhao.esta_coletando = true; // ele começa automaticamente coletando
                    caminhao.zona_atual = atual.dado.zonas_de_atuacao.head.dado; // a zona que ele ta atendendo
                                                                                 // atualmente
                                                                                 // (ate ela ficar sem lixo)
                    caminhao.esta_na_zona = true; // como ele começou agora, ele esta na zona agora. é como se ele
                                                  // tivesse
                                                  // acabado de chegar
                }
                atual = atual.prox;
            }
        }
    } // Funciona

    // Aqui é onde o caminhão de fato coleta o lixo da zona atual
    // Primeiro eu vejo o quanto de carga ele ainda pode receber e quanto lixo tem
    // na zona
    // Recolhe o que puder, sem ultrapassar o limite do caminhão
    // Atualizo os dados da zona e do caminhão e registro a coleta
    // Por fim, desativo a coleta porque ele vai precisar descarregar antes de
    // voltar

    public void caminhaoPequenoColeta() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
            while (atual != null) {
                CaminhaoPequeno caminhao = atual.dado;

                // se o caminhao estiver em uma zona, ele pode coletar.
                if (caminhao.esta_na_zona && caminhao.zona_atual != null && caminhao.coletou == false) {

                    caminhao.esta_coletando = true;
                    int espaco_disponivel = caminhao.capacidade - caminhao.carga_atual;
                    int quantidadeColetada = caminhao.zona_atual.LixoColetado(espaco_disponivel);
                    caminhao.coletarLixo(quantidadeColetada);
                    System.out.println("[COLETA DE LIXO] Caminhão " + caminhao.id_caminhao_pequeno + " coletou "
                            + quantidadeColetada + " toneladas na  " + caminhao.zona_atual.getNome());
                    System.out.println("[STATUS ZONA] Lixo restante na zona " + caminhao.zona_atual.getNome() + ": "
                            + caminhao.zona_atual.lixo_atual + " toneladas");
                    System.out.println(" ");
                    caminhao.esta_coletando = false;
                    caminhao.vai_coletar = false;
                    caminhao.coletou = true; // caminhao coletou, esta pronto para ir para a estação!
                    totalLixoColetado += quantidadeColetada;
                    coletasRealizadas += 1;

                }
                atual = atual.prox;
            }
        }
    } // funciona

    public void caminhaoPequenoIrPraEstacao() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

            while (atual != null) {
                CaminhaoPequeno caminhao = atual.dado;
                if (caminhao.coletou && caminhao.esta_indo_pra_estacao == false && caminhao.esta_na_estacao == false) { // se
                                                                                                                        // o
                                                                                                                        // caminhao
                                                                                                                        // coletou
                                                                                                                        // e
                                                                                                                        // estiver
                                                                                                                        // com
                                                                                                                        // a
                                                                                                                        // carga
                                                                                                                        // positiva

                    caminhao.estacao_a_ir = caminhao.zona_atual.estacao_descarga; // a estação q ele vai é a que a zona
                                                                                  // descarga
                    caminhao.esta_na_zona = false; // como ele ta indo pra zona, ele deixa de "estar nela"(mesmo q uma
                                                   // estação seja dentro de uma zona, vcs entenderam)
                    caminhao.esta_indo_pra_estacao = true; // ele começa a ir pra estação

                    boolean horarioPico = teresina.estaEmHorarioPico(); // verificar se teresina ta em horario de pico,
                                                                        // pq
                                                                        // ai o tempo muda
                    int tempo_viagem;
                    if (horarioPico) {
                        tempo_viagem = caminhao.zona_atual.tempo_viagem_estacao_pico;
                    } else {
                        tempo_viagem = caminhao.zona_atual.tempo_viagem_estacao_normal;
                    }

                    caminhao.tempo_viagem_ida = tempo_viagem;
                    caminhao.tempo_restante_viagem = tempo_viagem;
                    System.out.println("[IDA A ESTAÇÃO] Caminhão " + caminhao.id_caminhao_pequeno +
                            " indo para estação " + caminhao.estacao_a_ir.getNome() +
                            " | Tempo: " + tempo_viagem + " min");
                    System.out.println(" ");

                }
                atual = atual.prox;
            }
        }
    } // funciona

    public void processarCaminhoesPequenos() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

            while (atual != null) {
                CaminhaoPequeno caminhaoPequeno = atual.dado;
                            if (caminhaoPequeno.esta_na_estacao && !caminhaoPequeno.esta_indo_pra_estacao && 
                caminhaoPequeno.estacao_atual != null &&
                caminhaoPequeno.estacao_atual.fila_caminhao_pequeno.head != null &&
                !caminhaoPequeno.estacao_atual.fila_caminhao_pequeno.estaVazia() &&
                caminhaoPequeno == caminhaoPequeno.estacao_atual.fila_caminhao_pequeno.espiar()) {

                if (caminhaoPequeno.estacao_atual.fila_caminhao_grande.estaVazia()) {
                    if (caminhaoPequeno.tempo_inicio_espera == 0) {
                        caminhaoPequeno.tempo_inicio_espera = minutosSimulados;
                    }
                    caminhaoPequeno.tempo_espera_acumulado = minutosSimulados - caminhaoPequeno.tempo_inicio_espera;
                } else {
                        somaTemposEsperaPequenos = somaTemposEsperaPequenos + caminhaoPequeno.tempo_espera_acumulado;
                        caminhaoPequeno.tempo_espera_acumulado = 0; // Reset se há caminhão grande
                        // aqui a coleta vai acontecer
                        CaminhaoGrande caminhaoGrande = caminhaoPequeno.estacao_atual.fila_caminhao_grande.espiar();
                        caminhaoGrande.tempo_espera_acumulado = 0;
                        int quantidadeLiberada = Math.min(caminhaoPequeno.carga_atual, caminhaoGrande.espaco);
                        totalLixoTransferido += quantidadeLiberada;
                        caminhaoPequeno.liberarCarga(caminhaoGrande);
                        System.out.printf("[TRANSFERÊNCIA] Caminhão P %s -> Caminhão G %s: %d toneladas\n",
                                caminhaoPequeno.id_caminhao_pequeno,
                                caminhaoGrande.id_caminhao_grande,
                                quantidadeLiberada);
                        System.out.println(" ");

                        // Verifica se o caminhão pequeno esvaziou totalmente
                        if (caminhaoPequeno.carga_atual == 0) {
                            caminhaoPequeno.coletou = false; // Resetar flag
                            caminhaoPequeno.vai_coletar = true;
                            caminhaoPequeno.estacao_atual.fila_caminhao_pequeno.desenfileirar();
                            System.out.println("[FINALIZADO] Caminhão P " + caminhaoPequeno.id_caminhao_pequeno
                                    + " descarregou toda sua carga.");
                            System.out.println(" ");

                        }
                    }
                }
                atual = atual.prox;
            }
        }
    }

    public void caminhaoPequenoVoltarColeta() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

            while (atual != null) {
                CaminhaoPequeno caminhaoPequeno = atual.dado;
                if (caminhaoPequeno.coletou == false && caminhaoPequeno.vai_coletar
                        && caminhaoPequeno.esta_na_zona == false && !caminhaoPequeno.esta_indo_pra_zona
                        && caminhaoPequeno.zonas_de_atuacao.head != null) {
                    if (caminhaoPequeno.zona_atual.lixo_atual != 0) {
                        caminhaoPequeno.zona_a_ir = caminhaoPequeno.zona_atual;
                        System.out.println(
                                " [RETORNO A COLETA]  - Voltando para zona atual: "
                                        + caminhaoPequeno.zona_atual.getNome() +
                                        " (Lixo disponível: " + caminhaoPequeno.zona_atual.lixo_atual + " ton)");
                        System.out.println(" ");
                        if (teresina.estaEmHorarioPico()) {
                            caminhaoPequeno.tempo_viagem_volta = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_pico;
                            caminhaoPequeno.tempo_restante_viagem = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_pico;
                        } else {
                            caminhaoPequeno.tempo_viagem_volta = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_normal;
                            caminhaoPequeno.tempo_restante_viagem = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_normal;
                        }

                        caminhaoPequeno.esta_indo_pra_zona = true;
                        caminhaoPequeno.esta_na_estacao = false;
                        totalViagensPequenos++;
                        caminhaoPequeno.tempo_espera_acumulado = 0;
                        caminhaoPequeno.tempo_inicio_espera = 0;
                        

                    } else {
                        caminhaoPequeno.zonas_de_atuacao.desenfileirar();
                        if (caminhaoPequeno.zonas_de_atuacao.head != null) {
                            caminhaoPequeno.zona_a_ir = caminhaoPequeno.zonas_de_atuacao.head.dado;
                            System.out.println(" [RETORNO A COLETA - NOVA ZONA] - Próxima zona na fila: "
                                    + caminhaoPequeno.zona_a_ir.getNome() + " (Lixo: "
                                    + caminhaoPequeno.zona_a_ir.lixo_atual
                                    + " ton)");
                            System.out.println(" ");
                            if (teresina.estaEmHorarioPico()) {
                                caminhaoPequeno.tempo_viagem_volta = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_pico;
                                caminhaoPequeno.tempo_restante_viagem = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_pico;
                            } else {
                                caminhaoPequeno.tempo_viagem_volta = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_normal;
                                caminhaoPequeno.tempo_restante_viagem = caminhaoPequeno.zona_a_ir.tempo_viagem_estacao_normal;
                            }

                            caminhaoPequeno.esta_indo_pra_zona = true;
                            caminhaoPequeno.esta_na_estacao = false;
                            totalViagensPequenos++;
                            caminhaoPequeno.tempo_espera_acumulado = 0;
                            caminhaoPequeno.tempo_inicio_espera = 0;
                        }
                    }
                }

                atual = atual.prox;
            }
        }
    } // acredito q ok

    public void processarCaminhoesGrandes() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;
            while (atual != null) {
                CaminhaoGrande caminhao = atual.dado;
                if (caminhao.esta_na_estacao && caminhao.estacao.fila_caminhao_grande.espiar() == caminhao) {

                    if (caminhao.estacao.fila_caminhao_pequeno.estaVazia()) {

                        caminhao.tempo_espera_acumulado = minutosSimulados - caminhao.tempo_inicio_espera;
                    }

                    if ((caminhao.tempo_espera_acumulado >= caminhao.tolerancia_minutos && caminhao.carga_atual > 0)
                            || caminhao.carga_atual >= caminhao.capacidade_maxima) {
                        caminhao.iniciarViagemAoAterro(teresina.estaEmHorarioPico());
                        System.out.printf(
                                "\n[ATERRO] Caminhão G %s partindo para aterro. (Carga: %d/%d ton | Espera: %d min)\n",
                                caminhao.id_caminhao_grande, caminhao.carga_atual,
                                caminhao.capacidade_maxima, caminhao.tempo_espera_acumulado);
                        caminhao.estacao.fila_caminhao_grande.desenfileirar();
                        System.out.println(" ");
                        caminhao.esta_na_estacao = false;
                        caminhao.em_viagem_ao_aterro = true;
                        if (teresina.estaEmHorarioPico()) {
                            caminhao.tempo_viagem_ida = caminhao.estacao.tempo_viagem_aterro_pico;
                            caminhao.tempo_restante_viagem = caminhao.estacao.tempo_viagem_aterro_pico;

                        } else {

                            caminhao.tempo_viagem_ida = caminhao.estacao.tempo_viagem_aterro_normal;
                            caminhao.tempo_restante_viagem = caminhao.estacao.tempo_viagem_aterro_normal;
                        }

                    }

                }
                atual = atual.prox;
            }
        }
    } // acho q ok

    public void adicionarCaminhaoGrande() {
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

        while (atual != null) {
            CaminhaoPequeno caminhaoPequeno = atual.dado;
            if (caminhaoPequeno.estacao_atual != null &&
                    caminhaoPequeno.estacao_atual.fila_caminhao_pequeno != null &&
                    !caminhaoPequeno.estacao_atual.fila_caminhao_pequeno.estaVazia()) {

                if (caminhaoPequeno == caminhaoPequeno.estacao_atual.fila_caminhao_pequeno.espiar() &&
                        caminhaoPequeno.tempo_espera_acumulado >= caminhaoPequeno.tempo_max_espera && caminhaoPequeno.estacao_atual.fila_caminhao_grande.estaVazia() ) {

                    somaTemposEsperaPequenos = somaTemposEsperaPequenos + caminhaoPequeno.tempo_espera_acumulado;

                    EstacaoTransferencia estacao = caminhaoPequeno.estacao_atual;

                    String novoId = "CG-Extra-" + System.currentTimeMillis() % 10000;
                    CaminhaoGrande novoCaminhao = new CaminhaoGrande(novoId, estacao);
                    estacao.fila_caminhao_grande.enfileirar(novoCaminhao);
                    teresina.caminhoes_grandes.adicionar(novoCaminhao);
                    totalCaminhoesGrandesAdicionados++;

                    System.out.println("[ALERTA] Adicionado caminhão grande extra " + novoId +
                            " na " + estacao.getNome() +
                            " após " + caminhaoPequeno.tempo_espera_acumulado + " min de espera");
                    System.out.println(" ");

                    caminhaoPequeno.tempo_inicio_espera = 0;
                    caminhaoPequeno.tempo_espera_acumulado = 0;

                }

            }

            atual = atual.prox;
        }
    }

    public void atualizarMovimentoCaminhoesPequenos() {
        if (teresina.caminhoes_pequenos != null) {

            ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
            while (atual != null) {
                CaminhaoPequeno caminhao = atual.dado;
                if (caminhao.esta_na_estacao == false && caminhao.esta_na_zona == false) {

                    if (caminhao.esta_indo_pra_estacao) {
                        if (caminhao.tempo_restante_viagem > 0) {
                            caminhao.tempo_restante_viagem -= 1;
                            System.out.println("[VIAGEM PRA ESTAÇÃO] Caminhão " + caminhao.id_caminhao_pequeno +
                                    " a caminho: " + caminhao.tempo_restante_viagem +
                                    " minutos restantes");
                                    caminhao.tempo_espera_acumulado = 0;
                                    caminhao.tempo_inicio_espera = 0;

                        } else if (caminhao.tempo_restante_viagem <= 0) {
                            caminhao.estacao_atual = caminhao.estacao_a_ir;
                            caminhao.esta_na_estacao = true;
                            caminhao.estacao_a_ir = null;
                            caminhao.esta_indo_pra_estacao = false;
                            System.out.println("[CHEGOU-ESTAÇÃO] Caminhão " + caminhao.id_caminhao_pequeno +
                                    " completou viagem de ida em " + caminhao.tempo_viagem_ida + " minutos");
                            caminhao.estacao_atual.fila_caminhao_pequeno.enfileirar(caminhao);
                            totalViagensPequenos++;
                            caminhao.tempo_inicio_espera = minutosSimulados;
                        }

                    } else if (caminhao.esta_indo_pra_zona) {
                        if (caminhao.tempo_restante_viagem > 0) {
                            caminhao.tempo_restante_viagem -= 1;
                            System.out.println("[VIAGEM PRA ZONA] Caminhão " + caminhao.id_caminhao_pequeno +
                                    " a caminho: " + caminhao.tempo_restante_viagem +
                                    " minutos restantes");
                                    caminhao.tempo_espera_acumulado = 0;
                                    caminhao.tempo_inicio_espera = 0;

                        } else if (caminhao.tempo_restante_viagem <= 0) {
                            caminhao.esta_na_zona = true;
                            caminhao.esta_indo_pra_zona = false;
                            caminhao.zona_a_ir = null;
                            caminhao.esta_coletando = true;
                            System.out.println("[CHEGOU-ZONA] Caminhão " + caminhao.id_caminhao_pequeno +
                                    " chegou na zona " + caminhao.zona_atual.getNome());
                            totalViagensPequenos++;
                            caminhao.tempo_espera_acumulado = 0;
                                    caminhao.tempo_inicio_espera = 0;

                        }

                    }

                }

                atual = atual.prox;
            }
        }
    }

    public void CaminhoesChegamAoAterro() {
    ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;

    while (atual != null) {
        CaminhaoGrande caminhao = atual.dado;
        if (caminhao.esta_no_aterro && !caminhao.em_viagem_ao_aterro) {  
            System.out.println("\n[ATERRO] Caminhão G " + caminhao.id_caminhao_grande +
                    " descarregou " + caminhao.carga_atual + " toneladas");
            totalLixoDescarregado += caminhao.carga_atual;
            caminhao.descargaNoAterro();

            caminhao.esta_no_aterro = false;
            caminhao.em_retorno_do_aterro = true;
            
            System.out.println("[VOLTA PRA ESTACAO] Caminhão G " + caminhao.id_caminhao_grande
                    + " esta voltando para sua estação de transferencia após descarga no aterro");
            
            if (teresina.estaEmHorarioPico()) {
                caminhao.tempo_viagem_retorno = caminhao.estacao.tempo_viagem_aterro_pico;
                caminhao.tempo_restante_viagem = caminhao.estacao.tempo_viagem_aterro_pico;
            } else {
                caminhao.tempo_viagem_retorno = caminhao.estacao.tempo_viagem_aterro_normal;
                caminhao.tempo_restante_viagem = caminhao.estacao.tempo_viagem_aterro_normal;
            }
        }
        atual = atual.prox;
    }
}

    public void atualizarViagensAoAterro() {
    ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;

    while (atual != null) {
        CaminhaoGrande caminhao = atual.dado;
        
        // Viagem de ida ao aterro
        if (caminhao.em_viagem_ao_aterro && !caminhao.em_retorno_do_aterro) {
            if (caminhao.tempo_restante_viagem > 0) {
                caminhao.tempo_restante_viagem -= 1;
                System.out.println("[VIAGEM PRO ATERRO] Caminhão " + caminhao.id_caminhao_grande +
                        " a caminho: " + caminhao.tempo_restante_viagem +
                        " minutos restantes");
            } else {
                // Quando chega no aterro
                caminhao.esta_no_aterro = true;
                caminhao.em_viagem_ao_aterro = false;  // Importante: desativa a viagem de ida
                System.out.println("[CHEGOU-ATERRO] Caminhão " + caminhao.id_caminhao_grande +
                        " completou viagem de ida em " + caminhao.tempo_viagem_ida + " minutos");
                caminhao.tempo_viagem_ida = 0;
                totalViagensGrandes++;
            }
        }
        
        // Viagem de volta do aterro
        else if (caminhao.em_retorno_do_aterro && !caminhao.em_viagem_ao_aterro) {
            if (caminhao.tempo_restante_viagem > 0) {
                caminhao.tempo_restante_viagem -= 1;
                System.out.println("[VIAGEM DE VOLTA PRA ESTAÇÃO] Caminhão " + caminhao.id_caminhao_grande +
                        " a caminho: " + caminhao.tempo_restante_viagem +
                        " minutos restantes");
            } else {
                // Quando chega na estação
                caminhao.esta_na_estacao = true;
                caminhao.em_retorno_do_aterro = false;
                System.out.println("[CHEGOU-ESTAÇÃO] Caminhão " + caminhao.id_caminhao_grande +
                        " completou viagem de volta em " + caminhao.tempo_viagem_retorno + " minutos");
                caminhao.tempo_viagem_retorno = 0;
                caminhao.estacao.fila_caminhao_grande.enfileirar(caminhao);

            }
        }

        atual = atual.prox;
    }
}
    public void exibirEstatisticas() {
        // Exibe resumo da simulação
        System.out.println("\n======== ESTATÍSTICAS DA SIMULAÇÃO ========");

        double mediaEsperaPequenos = this.teresina.caminhoes_pequenos.tamanho > 0
                ? (double) somaTemposEsperaPequenos / this.teresina.caminhoes_pequenos.tamanho
                : 0;

        System.out.println("ESTATÍSTICAS GERAIS:");

        System.out
                .println("- Tempo médio de espera (pequenos): " + String.format("%.1f", mediaEsperaPequenos) + " min");
        System.out.println("- Caminhões grandes adicionados: " + totalCaminhoesGrandesAdicionados);
        System.out.println("- Lixo coletado total: " + totalLixoColetado + " ton");
        System.out.println("- Lixo transferido entre caminhões: " + totalLixoTransferido + " ton");
        System.out.println("- Lixo efetivamente descarregado: " + totalLixoDescarregado + " ton");
        System.out.println("- Total viagens pequenos (ida e volta): " + totalViagensPequenos);
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