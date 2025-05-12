public class EstacaoTransferencia {
    // identificação
    public String nome;
    public Zona localizacao;

    // filas de caminhões
    public Fila<CaminhaoPequeno> fila_caminhao_pequeno;
    public Fila<CaminhaoGrande> fila_caminhao_grande;

    // tempos de viagem da estação pro aterro
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

    public void adicionarCaminhaoPequeno(CaminhaoPequeno caminhaoPequeno) {
        fila_caminhao_pequeno.enfileirar(caminhaoPequeno);
        System.out
                .println("Caminhão " + caminhaoPequeno.getIdCaminhaoPequeno() + " chegou à estação de transferência.");
    }

    public void adicionarCaminhaoGrande(CaminhaoGrande caminhaoGrande) {
        fila_caminhao_grande.enfileirar(caminhaoGrande);
        System.out.println("Caminhão " + caminhaoGrande.getIdCaminhaoGrande() + " chegou à estação de transferência.");
    }

    public int getQuantidadeCaminhoes() {
        return fila_caminhao_grande.getTamanho();
    }
}