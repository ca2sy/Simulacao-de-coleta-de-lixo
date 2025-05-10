import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Simulacao {
    public Cidade teresina;
    public EstacaoTransferencia estacao_transferencia_a;
    public EstacaoTransferencia estacao_transferencia_b;
    public int hora_atual;
    private Timer timer;
    private static final long INTERVALO_HORA_SIMULADA = 1000; // 1 segundo é 1 hora, mas caso eu queira que 1 hora seja
                                                              // 1 minuto, so mudar de 1000 pra 60000

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

private void iniciarSimulacao() {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            hora_atual++;  // Incrementa a hora atual

            // Verifica se já passou de 24 horas para parar a simulação
            if (hora_atual > 24) {
                pararSimulacao();
            } else {
                // Imprime a hora simulada
                System.out.println("Hora atual simulada: " + hora_atual + "h");

                // Aqui você chama os métodos que devem ser executados em cada hora
                
                // verificarCaminhoesPequenosEncerraram();
                // verificarCaminhoesPequenosComecando();
                // caminhaoPequenoColeta();
                // caminhaoPequenoIrPraEstacao();
                // processarCaminhoesPequenos();
                // adicionarCaminhaoGrande();
                // processarCaminhoesGrandes();
                // CaminhoesChegamAoAterro();
                // atualizarMovimentoCaminhoes();
                // atualizarViagensAoAterro();


                //terminar de listar os metodos e testar. falar urgentemente com sekeff
        
                
                

                // 1. verificar quais caminhões pequenos encerraram suas atividades (FEITO)
                // 2. verificar quais caminhões pequenos iniciam suas atividades (FEITO)
                // 3. caminhões pequenos que chegaram às suas zonas, precisam ser carregados (FEITO)
                // 4. caminhões pequenos que precisam descarregar vão para as estações de transferencia (FEITO)
                // 5. caminhões pequenos nas estações de transferência precisam descarregar
                //         se for possível, descarrega, caso contrário, aguarda (processar a fila de descarga de caminhões pequenos) (FEITO?)
                // 6. verificar os tempos de espera nas estações de transferência e decidir se é o momento de adicionar um caminhão grande ou não (FEITO?)
                // 7. processar fila de caminhõe grandes nas estações de transferência (FEITO?)
                // 8. verificar quais camionhões grandes chegaram no aterro para descarregar (feito?)
                // 9. atualiza o movimento de cada caminhão que está no mapa. (feito?)
                //nao ta funcionando
            }
        }
    }, 0, INTERVALO_HORA_SIMULADA);  // Intervalo de tempo entre as execuções
}


    private void pararSimulacao() {
        System.out.println("Simulação encerrada após 24 horas.");
        timer.cancel();
    }

    private void inicializaCidade() {
        this.teresina = new Cidade();
    }

    private void criarEstacoes() {
        long tempoViagemNormalA = 300000;
        long tempoViagemPicoA = 360000;

        long tempoViagemNormalB = 360000;
        long tempoViagemPicoB = 480000;

        // criando as estações de transferência com os tempos de viagem até o aterro
        this.estacao_transferencia_a = new EstacaoTransferencia("Estação A", tempoViagemNormalA, tempoViagemPicoA);
        this.estacao_transferencia_b = new EstacaoTransferencia("Estação B", tempoViagemNormalB, tempoViagemPicoB);

        // adicionando as estações à cidade
        teresina.estacoes_transferencia.adicionar(estacao_transferencia_a);
        teresina.estacoes_transferencia.adicionar(estacao_transferencia_b);
    }

    private void criarZonas(int qtd_zonas, int tempo_maximo, int tempo_minimo) {
        for (int i = 0; i < qtd_zonas; i++) {
            EstacaoTransferencia estacao_descarga;
            long tempo_estacao;
            long tempo_estacao_pico;
            long tempo_min_viagem_normal = 10;
            long tempo_max_viagem_normal = 18;

            if (i <= 2) {
                estacao_descarga = estacao_transferencia_a;
                tempo_estacao = tempo_minimo + (int) (Math.random() * (tempo_maximo - tempo_minimo + 1));
                tempo_estacao_pico = tempo_estacao + 20;
            } else {
                estacao_descarga = estacao_transferencia_b;
                tempo_estacao = tempo_minimo + (int) (Math.random() * (tempo_maximo - tempo_minimo + 1));
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

            // usando o mesmo objeto, sorteie qtd_viagens_sorteada vezes um número entre 0 e
            // qtd_zonas e chame de lista_zonas
            CaminhaoPequeno cp = new CaminhaoPequeno(2, fila_zonas_caminhao_pequeno, "CaminhaoP2_" + i, qtd_viagens_sorteada);
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
            CaminhaoPequeno cp = new CaminhaoPequeno(4, fila_zonas_caminhao_pequeno, "CaminhaoP4_" + i, qtd_viagens_sorteada);
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
            CaminhaoPequeno cp = new CaminhaoPequeno(8, fila_zonas_caminhao_pequeno, "CaminhaoP8_" + i, qtd_viagens_sorteada);
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
            CaminhaoPequeno cp = new CaminhaoPequeno(10, fila_zonas_caminhao_pequeno, "CaminhaoP10_" + i, qtd_viagens_sorteada);
            teresina.caminhoes_pequenos.adicionar(cp);
        }

        // inicializando caminhões grandes
        for (int i = 0; i < qtd_grandes; i++) {
            if (i % 2 == 0) {
                CaminhaoGrande cg = new CaminhaoGrande("Caminhao Grande " + i, estacao_transferencia_a);
                teresina.caminhoes_grandes.adicionar(cg);
            } else {
                CaminhaoGrande cg = new CaminhaoGrande("Caminhao Grande " + i, estacao_transferencia_b);
                teresina.caminhoes_grandes.adicionar(cg);
            }
        }
    }
 

    //REFAZER TUDO APARTIR DAQUI!!!!!!!!!!!
    // verificar quais caminhões pequenos encerraram suas atividades

    public void verificarCaminhoesPequenosEncerraram(){
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while (atual != null) {
    CaminhaoPequeno dado = atual.dado;
    ListaEncadeada.No<CaminhaoPequeno> proximo = atual.prox;

    if (dado.zonas_de_atuacao == null) {
        System.out.println("Caminhão " + dado.id_caminhao_pequeno + " não tem mais zonas, encerrou o trabalho.");
        teresina.caminhoes_pequenos.remover(dado);
    }

    atual = proximo;
}
    }

    public void verificarCaminhoesPequenosComecando(){
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while(atual!=null){       
            if (atual.dado.num_viagens_realizadas == 0 && atual.dado.zonas_de_atuacao != null &&  atual.dado.zonas_de_atuacao.head != null) {
                System.out.println("Caminhão " + atual.dado.id_caminhao_pequeno + "Está começando agora na zona " + atual.dado.zonas_de_atuacao.head.dado.getNome() + ".");
                atual.dado.num_viagens_realizadas++;
                atual.dado.estaColetando = true;
                atual.dado.zona_atual = atual.dado.zonas_de_atuacao.head.dado;
            }
            atual = atual.prox;
        }
          
    }

    public void caminhaoPequenoColeta(){
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while(atual!=null){  
            if(atual.dado.zona_atual != null){
                if(atual.dado.carga_atual != atual.dado.capacidade){ //se o caminhão tiver podendo pegar coisa
                atual.dado.coletarLixo(atual.dado.zona_atual.lixo_atual);
                
            }
            }
            atual = atual.prox;
        }
    }

    
public void atualizarMovimentoCaminhoes() {
    ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
    while(atual != null) {
        
        // Verifica se está indo para a estação e não está coletando
        if (atual.dado.estacao_a_ir != null && !atual.dado.estaColetando) {
            atual.dado.tempo_restante_viagem -= INTERVALO_HORA_SIMULADA;
            
            // Verifica se chegou na estação
            if (atual.dado.tempo_restante_viagem <= 0) {
            atual.dado.estacao_atual = atual.dado.estacao_a_ir;
            atual.dado.estacao_a_ir = null;
            System.out.println("O caminhão " + atual.dado.id_caminhao_pequeno + " Chegou na estação " + atual.dado.estacao_atual);
            atual.dado.estacao_atual.fila_caminhao_pequeno.enfileirar(atual.dado);


             // Verifica se a zona atual está vazia, pq se tiver ele vai atender outra
             if (atual.dado.zona_atual != null && atual.dado.zona_atual.lixo_atual == 0) {
            atual.dado.zonas_de_atuacao.desenfileirar();
            if (!atual.dado.zonas_de_atuacao.estaVazia()) {
                atual.dado.zona_atual = atual.dado.zonas_de_atuacao.espiar();
            } else {
                atual.dado.zona_atual = null;
            }
        }
        }
       
        atual = atual.prox;
    }
}

}
public void caminhaoPequenoIrPraEstacao() {
    ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;

    while (atual != null) {
        if (atual.dado.carga_atual == atual.dado.capacidade && 
            atual.dado.estacao_atual == null) {

            if (atual.dado.zona_atual != null && atual.dado.zona_atual.estacao_descarga != null) {
                boolean horarioPico = teresina.estaEmHorarioPico();
                long tempoViagem;

                if (horarioPico) {
                    tempoViagem = atual.dado.zona_atual.tempo_viagem_estacao_pico;
                } else {
                    tempoViagem = atual.dado.zona_atual.tempo_viagem_estacao_normal;
                }

                atual.dado.tempo_restante_viagem = tempoViagem;
                atual.dado.estacao_a_ir = atual.dado.zona_atual.estacao_descarga;
                atual.dado.estaColetando = false;

                System.out.println("Caminhão " + atual.dado.id_caminhao_pequeno + 
                    " indo para " + atual.dado.estacao_a_ir.getNome() + 
                    " - Chega em " + (tempoViagem / 60000) + " minutos");
            } else {
                System.out.println("Caminhão " + atual.dado.id_caminhao_pequeno + 
                    " não pode ir para a estação porque a zona atual ou a estação de descarga está nula.");
            }
        }

        atual = atual.prox;
    }
}

public void atualizarViagensAoAterro() {
    ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;
    
    while (atual != null) {
        CaminhaoGrande caminhao = atual.dado;
        
        if (caminhao.emViagemAoAterro) {
            caminhao.tempoRestanteViagem -= INTERVALO_HORA_SIMULADA;
            
            // Verifica se voltou à estação (tempo de volta acabou e está vazio)
            if (caminhao.tempoRestanteViagem <= 0 && caminhao.espaco == caminhao.capacidade_maxima) {
                caminhao.emViagemAoAterro = false;
                caminhao.estacaoOrigem.fila_caminhao_grande.enfileirar(caminhao);
                System.out.println("Caminhão " + caminhao.id_caminhao_grande + 
                                 " retornou à " + caminhao.estacaoOrigem.getNome());
            }
        }
        atual = atual.prox;
    }
}


public void processarCaminhoesPequenos() {
    ListaEncadeada.No<EstacaoTransferencia> estacaoAtual = teresina.estacoes_transferencia.head;
    
    while (estacaoAtual != null) {
        EstacaoTransferencia estacao = estacaoAtual.dado;
        
        // Processa apenas caminhões pequenos
        if (!estacao.fila_caminhao_pequeno.estaVazia()) {
            CaminhaoPequeno caminhaoPequeno = estacao.fila_caminhao_pequeno.espiar();
            
            if (!estacao.fila_caminhao_grande.estaVazia()) {
                CaminhaoGrande caminhaoGrande = estacao.fila_caminhao_grande.espiar();
                
                // Verifica se o caminhão grande está disponível (não está coletando nem viajando)
                if (caminhaoGrande.coletando == false && !caminhaoGrande.emViagemAoAterro) {
                    // Realiza a transferência
                    int quantidade = Math.min(caminhaoPequeno.carga_atual, caminhaoGrande.espaco);
                    caminhaoPequeno.liberarCarga(caminhaoGrande);
                    caminhaoGrande.receberLixo(quantidade);
                    caminhaoGrande.coletando = true;
                    
                    // Reseta tempo de espera do pequeno
                    caminhaoPequeno.tempo_inicio_espera = 0;
                    caminhaoPequeno.tempo_espera_acumulado = 0;
                    
                    System.out.println("Transferidos " + quantidade + " toneladas para caminhão grande " + 
                                     caminhaoGrande.getIdCaminhaoGrande());
                    
                    // Verifica se o pequeno terminou
                    if (caminhaoPequeno.carga_atual == 0) {
                        estacao.fila_caminhao_pequeno.desenfileirar();
                        caminhaoPequeno.estacao_atual = null;
                        
                    }
                }
            } else {
                // Lógica de espera do caminhão pequeno
                long agora = System.currentTimeMillis();
                if (caminhaoPequeno.tempo_inicio_espera == 0) {
                    caminhaoPequeno.tempo_inicio_espera = agora;
                }
                caminhaoPequeno.tempo_espera_acumulado = agora - caminhaoPequeno.tempo_inicio_espera;
                System.out.println("Caminhão " + caminhaoPequeno.id_caminhao_pequeno + " Está esperando na estação para descarregar");
            }
        }
        estacaoAtual = estacaoAtual.prox;
    }
}

public void processarCaminhoesGrandes() {
    ListaEncadeada.No<EstacaoTransferencia> estacaoAtual = teresina.estacoes_transferencia.head;
    boolean horarioPico = teresina.estaEmHorarioPico();
    
    while (estacaoAtual != null) {
        EstacaoTransferencia estacao = estacaoAtual.dado;
        
        if (!estacao.fila_caminhao_grande.estaVazia()) {
            CaminhaoGrande caminhaoGrande = estacao.fila_caminhao_grande.espiar();
            
            // Se está coletando (recebeu carga recentemente)
            if (caminhaoGrande.coletando) {
                // Se encheu completamente, vai para o aterro
                if (caminhaoGrande.espaco == 0) {
                    estacao.fila_caminhao_grande.desenfileirar();
                    caminhaoGrande.iniciarViagemAoAterro(estacao, horarioPico);
                    caminhaoGrande.coletando = false;
                    System.out.println("Caminhão " + caminhaoGrande.getIdCaminhaoGrande() + 
                                     " indo para o aterro (tempo: " + 
                                     (caminhaoGrande.tempoRestanteViagem/60000) + " minutos)");
                }
                // Se não encheu completamente mas recebeu alguma carga, pode optar por ir embora
                else if (caminhaoGrande.espaco < caminhaoGrande.capacidade_maxima) {
                    estacao.fila_caminhao_grande.desenfileirar();
                    caminhaoGrande.iniciarViagemAoAterro(estacao, horarioPico);
                    caminhaoGrande.coletando = false;
                    System.out.println("Caminhão " + caminhaoGrande.getIdCaminhaoGrande() + 
                                     " indo para o aterro com carga parcial (" + 
                                     (caminhaoGrande.capacidade_maxima - caminhaoGrande.espaco) + " ton)");
                }
            }
        }
        estacaoAtual = estacaoAtual.prox;
    }
    
    // Atualiza caminhões que já estão em viagem ao aterro
    atualizarViagensAoAterro();
}

public void adicionarCaminhaoGrande() {
    // Verifica todas as estações de transferência
    ListaEncadeada.No<EstacaoTransferencia> estacaoAtual = teresina.estacoes_transferencia.head;
    while (estacaoAtual != null) {
        EstacaoTransferencia estacao = estacaoAtual.dado;
        
        // Verifica se há caminhões pequenos esperando na estação
        if (!estacao.fila_caminhao_pequeno.estaVazia()) {
            CaminhaoPequeno caminhaoPequeno = estacao.fila_caminhao_pequeno.espiar();
            
            // Verifica se o tempo máximo de espera foi atingido
            if (caminhaoPequeno.tempo_espera_acumulado >= caminhaoPequeno.tempo_max_espera) {
                // Cria um novo caminhão grande
                String novoId = "CG-Extra-" + System.currentTimeMillis() % 10000;
                CaminhaoGrande novoCaminhao = new CaminhaoGrande(novoId, estacao);
                
                // Adiciona o caminhão na fila da estação
                estacao.fila_caminhao_grande.enfileirar(novoCaminhao);
                teresina.caminhoes_grandes.adicionar(novoCaminhao);
                
                System.out.println("ALERTA: Adicionado caminhão grande extra " + novoId + 
                                 " na " + estacao.getNome() + 
                                 " devido ao tempo de espera do caminhão " + 
                                 caminhaoPequeno.id_caminhao_pequeno);
                
                // Reseta o tempo de espera do caminhão pequeno
                caminhaoPequeno.tempo_inicio_espera = 0;
                caminhaoPequeno.tempo_espera_acumulado = 0;
            }
        }
        estacaoAtual = estacaoAtual.prox;
    }
}

public void CaminhoesChegamAoAterro() {
    ListaEncadeada.No<CaminhaoGrande> atual = teresina.caminhoes_grandes.head;
    
    while (atual != null) {
        CaminhaoGrande caminhao = atual.dado;
        
        // Verifica se chegou ao aterro (tempo de ida acabou)
        if (caminhao.emViagemAoAterro && caminhao.tempoRestanteViagem <= 0 && caminhao.espaco < caminhao.capacidade_maxima) {
            caminhao.descargaNoAterro();
            System.out.println("Caminhão " + caminhao.id_caminhao_grande + " descarregou no aterro");
            
            // Configura a volta
            boolean horarioPico = teresina.estaEmHorarioPico();
            caminhao.tempoRestanteViagem = horarioPico ? 
                caminhao.estacaoOrigem.tempo_viagem_aterro_pico : 
                caminhao.estacaoOrigem.tempo_viagem_aterro_normal;
        }
        atual = atual.prox;
    }
}


    public void executar() {
        System.out.println("Iniciando simulação do dia...");
        System.out.println("");
        teresina.imprimeListaZonas();
        teresina.imprimeListaEstacoes();
        teresina.imprimeListaCaminhaoGrande();
        teresina.imprimeListaCaminhaoPequeno();
        iniciarSimulacao();
    }
}
