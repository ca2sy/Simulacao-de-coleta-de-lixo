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
                hora_atual++;
                if (hora_atual > 24) {
                    hora_atual = 1;
                }

                System.out.println("Hora atual simulada: " + hora_atual + "h");
                verificarCaminhoesPequenosEncerraram();

                //metodo pra cada uma dessas coisas
                // 1. verificar quais caminhões pequenos encerraram suas atividades (FEITO)
                // 2. verificar quais caminhões pequenos iniciam suas atividades (travei)
                // 3. caminhões pequenos que chegaram às suas zonas, precisam ser carregados
                // 4. caminhões pequenos nas estações de transferência precisam descarregar
                //         se for possível, descarrega, caso contrário, aguarda (processar a fila de descarga de caminhões pequenos)
                // 5. verificar os tempos de espera nas estações de transferência e decidir se é o momento de adicionar um caminhão grande ou não
                // 6. processar fila de caminhõe grandes nas estações de transferência
                // 7. verificar quais camionhões grandes chegaram no aterro para descarregar
                // 8. atualiza o movimento de cada caminhão que está no mapa.




               
                    pararSimulacao();
               
            }
        }, 0, INTERVALO_HORA_SIMULADA);
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
            CaminhaoGrande cg = new CaminhaoGrande("Caminhao Grande " + i);
            teresina.caminhoes_grandes.adicionar(cg);

        }
    }
 
    //Metodos do que vai ser realizado durante a simulação


    // public void adicionarCaminhaoGrandeExtraA(String id_caminhao_grande) {
    //     CaminhaoPequeno cp = estacao_transferencia_a.fila_caminhao_pequeno.espiar();
    //     if (cp != null && cp.tempoMaximoEsperado()) {
    //         CaminhaoGrande cg = new CaminhaoGrande(id_caminhao_grande);
    //         teresina.caminhoes_grandes.adicionar(cg);
    //         estacao_transferencia_a.fila_caminhao_grande.enfileirar(cg);
    //     }
    // }

    // public void adicionarCaminhaoGrandeExtraB(String id_caminhao_grande) {
    //     CaminhaoPequeno cp = estacao_transferencia_b.fila_caminhao_pequeno.espiar();
    //     if (cp != null && cp.tempoMaximoEsperado()) {
    //         CaminhaoGrande cg = new CaminhaoGrande(id_caminhao_grande);
    //         teresina.caminhoes_grandes.adicionar(cg);
    //         estacao_transferencia_b.fila_caminhao_grande.enfileirar(cg);
    //     }
    // }

    // public void direcionarCaminhaoGrandeParaEstacao(CaminhaoGrande caminhaoGrande) {
    //     // verifica o número de caminhões em cada estação
    //     int numCaminhoesEstacaoA = estacao_transferencia_a.getQuantidadeCaminhoes();
    //     int numCaminhoesEstacaoB = estacao_transferencia_b.getQuantidadeCaminhoes();

    //     // se a estação A tem menos caminhões ou as duas tem a mesma quantidade
    //     if (numCaminhoesEstacaoA <= numCaminhoesEstacaoB) {
    //         caminhaoGrande.irParaEstacao(estacao_transferencia_a);
    //         System.out.println("Caminhão " + caminhaoGrande.getIdCaminhaoGrande() + " foi direcionado para a "
    //                 + estacao_transferencia_a.getNome());
    //     } else {
    //         caminhaoGrande.irParaEstacao(estacao_transferencia_b);
    //         System.out.println("Caminhão " + caminhaoGrande.getIdCaminhaoGrande() + " foi direcionado para a "
    //                 + estacao_transferencia_b.getNome());
    //     }

    //     // basicamente, o caminhao vai pra onde tiver menos caminhoes.
    // }

    // public void coletaCaminhoesPequenos(CaminhaoPequeno caminhaoPequeno) {

    //     if (caminhaoPequeno.zonas_de_atuacao.estaVazia()) {
    //         System.out.println("Caminhão " + caminhaoPequeno.id_caminhao_pequeno + " não tem mais zonas para visitar.");
    //         return;
    //     }

    //     Zona zona_atual_de_atuacao = caminhaoPequeno.zonas_de_atuacao.espiar();

    //     if (caminhaoPequeno.carga_atual >= caminhaoPequeno.capacidade) {
    //         caminhaoPequeno.irParaEstacao(zona_atual_de_atuacao.estacao_descarga);
    //         return;
    //     }

    //     int capacidade_restante_caminhao = caminhaoPequeno.capacidade - caminhaoPequeno.carga_atual;
    //     int LixoColetado = zona_atual_de_atuacao.LixoColetado(capacidade_restante_caminhao);
    //     caminhaoPequeno.carga_atual += LixoColetado;
    //     caminhaoPequeno.num_viagens_realizadas++;

    //     System.out.println("Caminhão " + caminhaoPequeno.id_caminhao_pequeno + " coletou " + LixoColetado
    //             + " toneladas da zona " + zona_atual_de_atuacao.nome + " . Carga atual:" + caminhaoPequeno.carga_atual
    //             + "/" + caminhaoPequeno.capacidade);

    //     if (zona_atual_de_atuacao.lixo_zona == 0) {
    //         caminhaoPequeno.zonas_de_atuacao.desenfileirar();
    //     }

    // }

    // public void transferencia(EstacaoTransferencia estacao) {
    //     // Verificar se há caminhões pequenos e grandes disponíveis na estação
    //     if (estacao.fila_caminhao_pequeno.estaVazia() || estacao.fila_caminhao_grande.estaVazia()) {
    //         System.out.println("Não há caminhões suficientes na estação para realizar a transferência.");
    //         return;
    //     }

    //     // Acessar os caminhões
    //     CaminhaoPequeno pequeno = estacao.fila_caminhao_pequeno.espiar();
    //     CaminhaoGrande grande = estacao.fila_caminhao_grande.espiar();

    //     int cargaDoPequeno = pequeno.getCargaAtual();

    //     // Verificar se o caminhão pequeno está vazio
    //     if (cargaDoPequeno == 0) {
    //         System.out.println("Caminhão pequeno " + pequeno.getIdCaminhaoPequeno() + " está vazio.");
    //         estacao.fila_caminhao_pequeno.desenfileirar(); // Remover caminhão pequeno da fila
    //         return;
    //     }

    //     // Realizar a transferência de lixo
    //     int transferido = grande.receberLixo(cargaDoPequeno);
    //     pequeno.liberarCarga(transferido); // Liberar a carga do caminhão pequeno após transferência

    //     // Verificar se o caminhão pequeno ficou sem carga
    //     if (pequeno.getCargaAtual() == 0) {
    //         estacao.fila_caminhao_pequeno.desenfileirar(); // Remover o caminhão pequeno da fila se a carga estiver
    //                                                        // zerada
    //         System.out.println("Caminhão pequeno " + pequeno.getIdCaminhaoPequeno() + " liberado da estação.");
    //     }

    //     // Verificar a carga do caminhão grande e se ele atingiu a capacidade máxima
    //     if (grande.getCargaAtual() >= CaminhaoGrande.getCapacidademaxima()) {
    //         grande.descargaNoAterro(); // Descarregar no aterro se a carga máxima foi atingida
    //     } else {
    //         // Se o caminhão grande não estiver cheio, ele continua na estação
    //         System.out.println("Caminhão grande " + grande.getIdCaminhaoGrande()
    //                 + " não está cheio, continuando a transferência.");
    //     }
    // }

    //REFAZER TUDO APARTIR DAQUI!!!!!!!!!!!
    // verificar quais caminhões pequenos encerraram suas atividades

    public void verificarCaminhoesPequenosEncerraram(){
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
         while (atual != null) {
        if (atual.dado.zonas_de_atuacao == null) {
            System.out.println("Caminhão " + atual.dado.id_caminhao_pequeno + " não tem mais zonas, encerrou o trabalho.");
            teresina.caminhoes_pequenos.remover(atual.dado); 
        }
        atual = atual.prox; // avança para o próximo
    }
    }

    public void verificarCaminhoesPequenosComecando(){
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while(atual!=null){       
            if(atual.dado.num_viagens_realizadas == 0){
                System.out.println("Caminhão " + atual.dado.id_caminhao_pequeno + "Está começando agora na zona " + atual.dado.zonas_de_atuacao.head + ".");
                atual.dado.num_viagens_realizadas++;
                atual.dado.zona_atual = atual.dado.zonas_de_atuacao.head.dado;
            }
            atual = atual.prox;
        }
    }

    public void caminhaoPequenoColeta(){
        ListaEncadeada.No<CaminhaoPequeno> atual = teresina.caminhoes_pequenos.head;
        while(atual!=null){  
            if(atual.dado.carga_atual != atual.dado.capacidade){
                
            }
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
