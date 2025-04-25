public class CaminhaoPequeno {
    public int capacidade; // 2, 4, 8 ou 10
    public int carga_atual; // Carga atual do caminhão em toneladas
    public Fila<Zona> zonas_de_atuacao; // Fila das zonas em que o caminhão vai
    public String id_caminhao_pequeno;
    public int num_viagens_realizadas;
    public int tempo_esperado;
    public int tempo_maximo_espera; // tempo máximo de espera permitido para caminhões pequenos

    public CaminhaoPequeno(int capacidade, Fila<Zona> zonas_de_atuacao, String id_caminhao_pequeno) {
        this.capacidade = capacidade;
        this.zonas_de_atuacao = zonas_de_atuacao;
        this.carga_atual = 0;
        this.id_caminhao_pequeno = id_caminhao_pequeno;
        this.num_viagens_realizadas = 0; 
        tempo_esperado = 0;
        this.tempo_maximo_espera = 20;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getCargaAtual() {
        return carga_atual;
    }

    public int getNumViagensRealizadas(){
        return num_viagens_realizadas;
    }

    public String getIdCaminhaoPequeno(){
        return id_caminhao_pequeno;
    }

 public void coletarLixo(){ //o caminhão coleta lixo de uma zona específica
    if (zonas_de_atuacao.estaVazia()) { //verifico se o caminhão tem onde coletar lixo
        System.out.println("Nenhuma zona na fila de atuação. Caminhão não tem pra onde ir");

    } else { 
        Zona zonaAtual = zonas_de_atuacao.desenfileirar(); // a zona que o caminhao vai trabalhar agora é a primeira da fila
        System.out.println("Caminhão " + id_caminhao_pequeno + " iniciando coleta na zona " + zonaAtual.getNome());
        int capacidadeRestante = capacidade - carga_atual; 
        int lixoColetado = zonaAtual.LixoColetado(capacidadeRestante);
        carga_atual += lixoColetado;
        num_viagens_realizadas++;
        System.out.println("Caminhão" + id_caminhao_pequeno + " coletou " + lixoColetado + " toneladas na zona " + zonaAtual.getNome() + ". Carga atual: " + carga_atual + " toneladas.");
        // se o caminhão não tiver mais espaço pra lixo, ele vai pra estação de transferencia.
    }

 }


public void liberarCarga(int quantidade) {
    if (quantidade > carga_atual) { //se o caminhao tentar liberar mais do que ele pode, ele so vai liberar o que ele pode
        quantidade = carga_atual;
    }
    carga_atual -= quantidade; //a carga do caminhao diminui oq foi liberado, podendo zerar ou ainda depositar em outro caminhao grande
    System.out.println("Caminhão P reduziu sua carga em " + quantidade + " toneladas. Carga restante: " + carga_atual + " toneladas.");
}
 

public boolean tempoMaximoEsperado(int tempo){
    tempo_esperado += tempo;
    if (tempo_esperado >= tempo_maximo_espera) {
        return true;
} else {
    return false;
}

}

}