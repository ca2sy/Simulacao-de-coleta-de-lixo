public class EstacaoTransferencia {
    public Fila<CaminhaoPequeno> fila_caminhao_pequeno;
    public Fila<CaminhaoGrande> fila_caminhao_grande;
    public String nome;
    public Zona localizacao;
    public long tempo_viagem_aterro_normal;
    public long tempo_viagem_aterro_pico;
    

    public EstacaoTransferencia( String nome, long tempo_viagem_aterro_normal, long tempo_viagem_aterro_pico) {
        this.nome = nome;
        this.fila_caminhao_pequeno = new Fila<>(); //fila de caminhoes pequenos
        this.fila_caminhao_grande = new Fila<>(); //fila de caminhoes grandes
       this.tempo_viagem_aterro_normal = tempo_viagem_aterro_normal;
       this.tempo_viagem_aterro_pico = tempo_viagem_aterro_pico;
    }

    public String getNome() {
        return nome;
    }
    
    public void adicionarCaminhaoPequeno(CaminhaoPequeno caminhaoPequeno) { //caminhaozinho chegou a estação e entra na fila
        fila_caminhao_pequeno.enfileirar(caminhaoPequeno);
        System.out.println("Caminhão" + caminhaoPequeno.getIdCaminhaoPequeno() + "chegou à estação de transferência.");
    }
    
  
    public void adicionarCaminhaoGrande(CaminhaoGrande caminhaoGrande) { //caminhaozao chegou na estação e entra na fila
        fila_caminhao_grande.enfileirar(caminhaoGrande);
        System.out.println("Caminhão" + caminhaoGrande.getIdCaminhaoGrande() + "chegou à estação de transferência.");
    }
    
    
    public int getQuantidadeCaminhoes() {
        return fila_caminhao_grande.getTamanho(); 
    }

   
}