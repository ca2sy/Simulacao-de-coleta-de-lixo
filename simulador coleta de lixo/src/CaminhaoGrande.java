import java.sql.Time;

public class CaminhaoGrande {
    public static final int capacidade_maxima = 20; 
    public static final int tolerancia = 40; 
    public int carga_atual;
    public String id_caminhao_grande;
    public Time tempo_esperado; //quanto tempo o caminhao ja esperou

    public CaminhaoGrande(String id_caminhao_grande) {
        this.carga_atual = 0;
        this.tempo_esperado = Time.valueOf("00:00:00");
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
    public Time getTempoEsperado() {
        return tempo_esperado;
    }
    
    public static int getTolerancia() {
        return tolerancia;
    }
    
    public int receberLixo(int quantidade) {
        int espaco_disponivel = capacidade_maxima - carga_atual; //ao acionar essa função, vejo o espaco do caminhao
        int lixo_carregado = Math.min(quantidade, espaco_disponivel); //o caminhao vai receber a quantidade dita ou a quantidade possivel
        carga_atual += lixo_carregado;
        System.out.println("Caminhão" + id_caminhao_grande + " carregou " + lixo_carregado + " toneladas. Carga atual: " + carga_atual + " toneladas.");
        if(carga_atual == capacidade_maxima){ //se o caminhão lotar, ele sai pro aterro
            System.out.println("Caminhão" + id_caminhao_grande + " atingiu a capacidade máxima e está saindo para o aterro sanitário.");
            descargaNoAterro();
        }
        return lixo_carregado; //isso pro caminhaozinho saber quanto ele depositou no caminhaozao
    }
    
    
    public void aguardarNaEstacao(Time tempo) {

        tempo_esperado = new Time(tempo_esperado.getTime() + tempo.getTime());

        long minutosDeEspera = tempo_esperado.getTime() / (60 * 1000); 
        System.out.println("Caminhão " + id_caminhao_grande + "  aguardou " + tempo + " . Tempo total de espera: " + tempo_esperado);

    if (minutosDeEspera >= tolerancia && carga_atual > 0) {
        System.out.println("Tempo de espera excedido e caminhão " + id_caminhao_grande + 
            " possui carga, partindo para o aterro.");
        descargaNoAterro();
    } else if (minutosDeEspera >= tolerancia && carga_atual == 0) {
        System.out.println("Tempo de espera excedido, mas caminhão " + id_caminhao_grande + 
            " está vazio, aguardando um carregamento.");
    }
}
    
    //caminhaozao vai pro aterro e fica zero bala 
    private void descargaNoAterro() {
        System.out.println("Caminhão G descarregou " + carga_atual + " toneladas no aterro sanitário.");
        carga_atual = 0;
        tempo_esperado = Time.valueOf("00:00:00");
    }
    
  
}