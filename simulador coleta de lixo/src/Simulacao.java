import java.sql.Time;
import java.util.Random;
public class Simulacao {
   public Cidade teresina;
   public EstacaoTransferencia estacao_transferencia_a;
   public EstacaoTransferencia estacao_transferencia_b;
   public int hora_atual;
   //criar um timer




   public Simulacao(int qtd_pequenos_2, int qtd_pequenos_4, int qtd_pequenos_8, int qtd_pequenos_10, int qtd_grandes, int qtd_viagens) {
    this.inicializaCidade();
    this.criarEstacoes();
    this.criarZonas(5, 20, 30);
    this.zonaEstacoes();
    this.inicializarCaminhoes(qtd_pequenos_2, qtd_pequenos_4,  qtd_pequenos_8, qtd_pequenos_10, qtd_grandes, qtd_viagens);
    hora_atual = 0;
    //instanciar o timer



}

private void inicializaCidade() {
    this.teresina = new Cidade();
}


public EstacaoTransferencia getEstacao_transferencia_a() {
    return estacao_transferencia_a;
}

public EstacaoTransferencia getEstacao_transferencia_b() {
    return estacao_transferencia_b;
}

public int getHora_atual() {
    return hora_atual;
}

public Cidade getTeresina() {
    return teresina;
}


   


private void criarEstacoes(){
    this.estacao_transferencia_a = new EstacaoTransferencia("Estação A"); 
    this.estacao_transferencia_b = new EstacaoTransferencia("Estação B");
    teresina.estacoes_transferencia.adicionar(estacao_transferencia_a);
    teresina.estacoes_transferencia.adicionar(estacao_transferencia_b);
    
}

private void criarZonas(int qtd_zonas, int tempo_maximo, int tempo_minimo){
    for (int i=0; i<qtd_zonas; i++) {
     EstacaoTransferencia estacao_descarga; 
     int tempo_estacao;
     int tempo_estacao_pico;
       if (i <= 2){
      estacao_descarga = estacao_transferencia_a; 
       tempo_estacao = tempo_minimo + (int) (Math.random() * (tempo_maximo - tempo_minimo + 1) );
       tempo_estacao_pico = tempo_estacao + 20;

       } else {
            estacao_descarga = estacao_transferencia_b;
            tempo_estacao = tempo_minimo + (int) (Math.random() * (tempo_maximo - tempo_minimo + 1) );
            tempo_estacao_pico = tempo_estacao + 20;
        }
        Zona z = new Zona("Zona "+i, 15, 25, 10, 18, estacao_descarga, tempo_estacao, tempo_estacao_pico);
        teresina.zonas.adicionar(z);
    }
}

private void zonaEstacoes(){

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




private void inicializarCaminhoes(int qtd_pequenos_2, int qtd_pequenos_4, int qtd_pequenos_8, int qtd_pequenos_10, int qtd_grandes, int qtd_viagens){
    Random random = new Random();
    // criar uma lista com os nomes de todas as zonas (pode ser um array)
    
    int total_zonas = teresina.zonas.tamanho;

    Zona[] arrayZonas = new Zona[total_zonas];
    ListaEncadeada.No<Zona> atual = teresina.zonas.head;
    
    for (int i = 0; i < total_zonas; i++) {
        arrayZonas[i] = atual.dado; 
        atual = atual.prox;
    }

//inicio caminhoes de capacidade 2
    for (int i=0; i<qtd_pequenos_2; i++){
        int qtd_viagens_sorteada = random.nextInt(qtd_viagens) + 1;
        Fila<Zona> fila_zonas_caminhao_pequeno = new Fila<>();
        for(int b =0; b<qtd_viagens_sorteada; b++){
            int indiceAleatorio = random.nextInt(total_zonas);
            fila_zonas_caminhao_pequeno.enfileirar(arrayZonas[indiceAleatorio]);
        }

        // usando o mesmo objeto, sorteie qtd_viagens_sorteada vezes um número entre 0 e qtd_zonas e chame de lista_zonas
        CaminhaoPequeno cp = new CaminhaoPequeno(2,fila_zonas_caminhao_pequeno, "CaminhaoP2_" + i );
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
        CaminhaoPequeno cp = new CaminhaoPequeno(4, fila_zonas_caminhao_pequeno, "CaminhaoP4_" + i);
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
        CaminhaoPequeno cp = new CaminhaoPequeno(8, fila_zonas_caminhao_pequeno, "CaminhaoP8_" + i);
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
        CaminhaoPequeno cp = new CaminhaoPequeno(10, fila_zonas_caminhao_pequeno, "CaminhaoP10_" + i);
        teresina.caminhoes_pequenos.adicionar(cp);
    }

//inicializando caminhões grandes
    for (int i=0; i<qtd_grandes; i++) {
        CaminhaoGrande cg = new CaminhaoGrande("Caminhao Grande "+i);
        teresina.caminhoes_grandes.adicionar(cg);
        
    }
}


public void adicionarCaminhaoGrandeExtraA(String id_caminhao_grande){
    int tempo_esperado_caminhao_pequeno = estacao_transferencia_a.fila_caminhao_pequeno.espiar().tempo_esperado;
    if (estacao_transferencia_a.fila_caminhao_pequeno.espiar().tempoMaximoEsperado(tempo_esperado_caminhao_pequeno) == true){ 
        CaminhaoGrande cg = new CaminhaoGrande(id_caminhao_grande);
        teresina.caminhoes_grandes.adicionar(cg);
        estacao_transferencia_a.fila_caminhao_grande.enfileirar(cg);
    }
}

public void adicionarCaminhaoGrandeExtraB(String id_caminhao_grande){
    int tempo_esperado_caminhao_pequeno = estacao_transferencia_b.fila_caminhao_pequeno.espiar().tempo_esperado;
    if (estacao_transferencia_b.fila_caminhao_pequeno.espiar().tempoMaximoEsperado(tempo_esperado_caminhao_pequeno) == true){
        CaminhaoGrande cg = new CaminhaoGrande(id_caminhao_grande);
        teresina.caminhoes_grandes.adicionar(cg);
        estacao_transferencia_b.fila_caminhao_grande.enfileirar(cg);
        
    }
}

public void executar() {
    System.out.println("Iniciando simulação do dia...");
    System.out.println("");
    teresina.imprimeListaZonas();
    teresina.imprimeListaEstacoes();
    teresina.imprimeListaCaminhaoGrande();
    teresina.imprimeListaCaminhaoPequeno();
    // iniciar o timer e fazer com que ele imprima os dados a cada segundo
  
 

}
}



