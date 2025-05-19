public class EstacaoTransferencia {
    // identificação e localização (a estação tem que estar em uma zona)
    public String nome;
    public Zona localizacao;

    // filas de caminhões esperando para descarregar e receber, respectivamente
    public Fila<CaminhaoPequeno> fila_caminhao_pequeno;
    public Fila<CaminhaoGrande> fila_caminhao_grande;

    // tempos de viagem da estação pro aterro, dependendo do pico
    public int tempo_viagem_aterro_normal;
    public int tempo_viagem_aterro_pico;

    public EstacaoTransferencia(String nome, int tempo_viagem_aterro_normal, int tempo_viagem_aterro_pico) {
        this.nome = nome;
        this.fila_caminhao_pequeno = new Fila<>();
        this.fila_caminhao_grande = new Fila<>();
        this.tempo_viagem_aterro_normal = tempo_viagem_aterro_normal;
        this.tempo_viagem_aterro_pico = tempo_viagem_aterro_pico;
    }

    public String getNome() {
        return nome;
    }

}