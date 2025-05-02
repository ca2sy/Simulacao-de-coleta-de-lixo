import java.sql.Time;

public class Zona {
    public String nome; 
    public int lixo_minimo;
    public int lixo_maximo; //com esses atributos, eu posso gerar uma quantidade aleatória de lixo pra zona
    public Time tempo_min_viagem_pico, tempo_max_viagem_pico, tempo_min_viagem_normal, tempo_max_viagem_normal; //5:12
    public int lixo_zona;
    public EstacaoTransferencia estacao_descarga;
    public Time tempo_viagem_estacao_normal;
    public Time tempo_viagem_estacao_pico;
 
    // todo
    //public EstacaoTransferencia estacao_descarga;

    public Zona(String nome, int lixo_minimo, int lixo_maximo, Time tempo_min_viagem_pico, Time tempo_max_viagem_normal, 
    Time tempo_min_viagem_normal, Time tempo_max_viagem_pico, EstacaoTransferencia estacao_descarga, 
    Time tempo_viagem_estacao_normal, Time tempo_viagem_estacao_pico){ 
        this.lixo_maximo = lixo_maximo;
        this.lixo_minimo = lixo_minimo;
        this.tempo_max_viagem_normal =tempo_max_viagem_normal;
        this.tempo_min_viagem_normal =tempo_min_viagem_normal; 
        this.tempo_max_viagem_pico = tempo_max_viagem_pico;
        this.tempo_min_viagem_pico = tempo_min_viagem_pico;
        this.nome = nome;
        this.estacao_descarga = estacao_descarga;
        this.tempo_viagem_estacao_normal = tempo_viagem_estacao_normal;
        this.tempo_viagem_estacao_pico = tempo_viagem_estacao_pico;        
        lixo_zona = 0; 
    }

public int getLixo_maximo() {
    return lixo_maximo;
}

public int getLixo_minimo() {
    return lixo_minimo;
}

public int getLixo_zona() {
        return lixo_zona;
    }

public String getNome() {
        return nome;
    }
    

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.nome;
    }

    public void gerarLixoDiario() { //pesquisei como criar uma quantidade aleatória e usei como base pro código.
        lixo_zona = lixo_minimo + (int) (Math.random() * (lixo_maximo - lixo_minimo + 1));
        System.out.println("Zona " + nome + " gerou " + lixo_zona + " toneladas de lixo hoje.");
        
    }

    public int LixoColetado(int quantidadeSolicitada) {
        int coletado = Math.min(quantidadeSolicitada, lixo_zona); //Se o caminhão quiser pegar 10 toneladas, mas tem 15 toneladas disponíveis, ele pega somente as 10. Mas, caso ele queira 10, mas tenha só 5, ele pega as 5. 
        lixo_zona -= coletado; //Lixo da zona é diminuido
        System.out.println("Zona " + nome + ": " + coletado + " toneladas coletadas. Lixo restante: " + lixo_zona + " toneladas.");
        return coletado; //quanto o caminhão coletou é retornado
    }
   
}

