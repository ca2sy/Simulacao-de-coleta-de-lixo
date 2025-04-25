public class EstacaoTransferencia {
    public Fila<CaminhaoPequeno> fila_caminhao_pequeno;
    public Fila<CaminhaoGrande> fila_caminhao_grande;
    public String nome;
    public Zona localizacao;
    

    public EstacaoTransferencia( String nome) {
        this.nome = nome;
        this.fila_caminhao_pequeno = new Fila<>(); //fila de caminhoes pequenos
        this.fila_caminhao_grande = new Fila<>(); //fila de caminhoes grandes
       
    }
    
    
    public void adicionarCaminhaoPequeno(CaminhaoPequeno caminhaoPequeno) { //caminhaozinho chegou a estação e entra na fila
        fila_caminhao_pequeno.enfileirar(caminhaoPequeno);
        System.out.println("Caminhão" + caminhaoPequeno.getIdCaminhaoPequeno() + "chegou à estação de transferência.");
    }
    
  
    public void adicionarCaminhaoGrande(CaminhaoGrande caminhaoGrande) { //caminhaozao chegou na estação e entra na fila
        fila_caminhao_grande.enfileirar(caminhaoGrande);
        System.out.println("Caminhão" + caminhaoGrande.getIdCaminhaoGrande() + "chegou à estação de transferência.");
    }
    

    public void Transferencia() {
        if (fila_caminhao_pequeno.estaVazia()) {
            System.out.println("Nenhum caminhão P na estação de transferência.");
            return;
        }
        if (fila_caminhao_grande.estaVazia()) {
            System.out.println("Nenhum caminhão G disponível para carregamento.");
            return;
        }
        
        CaminhaoPequeno caminhaoPequeno = fila_caminhao_pequeno.espiar(); // "bizoio" o primeiro da fila
        CaminhaoGrande caminhaoGrande = fila_caminhao_grande.espiar(); 
        
        int cargaP = caminhaoPequeno.getCargaAtual();
        System.out.println("Iniciando transferência do caminhão "  + caminhaoPequeno.getIdCaminhaoPequeno() + "para o Caminhão " + caminhaoGrande.getIdCaminhaoGrande());
        
        int cargaTransferida = caminhaoGrande.receberLixo(cargaP);
        caminhaoPequeno.liberarCarga(cargaTransferida);
        
        System.out.println("Transferência realizada: " + cargaTransferida + " toneladas transferidas.");
        
 
        if(caminhaoPequeno.getCargaAtual() == 0) { //caso o caminhaozinho tiver liberado tudo, ele sai da fila
            fila_caminhao_pequeno.desenfileirar();
            System.out.println("Caminhão" + caminhaoPequeno.getIdCaminhaoPequeno() + " finalizou a transferência.");
        }
    }
    
    
    //ta certo isso aqui? 

   
   
}