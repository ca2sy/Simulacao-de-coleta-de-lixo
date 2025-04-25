public class Cidade {
    public ListaEncadeada<Zona> zonas = new ListaEncadeada<Zona>(); //lista de zonas que a cidade vai ter
    public ListaEncadeada<CaminhaoPequeno> caminhoes_pequenos = new ListaEncadeada<CaminhaoPequeno>();
    public ListaEncadeada<CaminhaoGrande> caminhoes_grandes  = new ListaEncadeada<CaminhaoGrande>();

    public ListaEncadeada<EstacaoTransferencia> estacoes_transferencia = new ListaEncadeada<>();

  
 
    public static final int horario_pico_inicio1 = 12; //representam horario de pico
    public static final int horario_pico_fim1 = 14;
    public static final int horario_pico_inicio2 = 18; 
    public static final int horario_pico_fim2 = 20;

    public Cidade (){
        
    }

public void imprimeListaEstacoes(){
    ListaEncadeada.No<EstacaoTransferencia> atual = this.estacoes_transferencia.head;
    while (atual != null) {
        System.out.println(atual.dado.nome);
        System.out.println("Localização: " + atual.dado.localizacao.nome);
        atual = atual.prox;
    }

    System.out.println("");
}

    public void imprimeListaCaminhaoPequeno(){
        ListaEncadeada.No<CaminhaoPequeno> atual = this.caminhoes_pequenos.head;
        while (atual != null) {
            System.out.println(atual.dado.getIdCaminhaoPequeno());
            System.out.println("Capacidade " + atual.dado.getCapacidade());
            System.out.println("Zonas " + atual.dado.zonas_de_atuacao.listaString());
            System.out.println("");
            atual = atual.prox;
        }
        System.out.println("");
    }




    public void imprimeListaCaminhaoGrande(){
        ListaEncadeada.No<CaminhaoGrande> atual = this.caminhoes_grandes.head;
        while (atual != null) {
            System.out.println( atual.dado.getIdCaminhaoGrande());
            System.out.println("");
            atual = atual.prox;
        }
        System.out.println("");
    }

    public void imprimeListaZonas() {
        ListaEncadeada.No<Zona> atual = this.zonas.head;
        while (atual != null) {
            System.out.println(atual.dado.getNome());
            System.out.println("Descarrega seu lixo na " + atual.dado.estacao_descarga.nome);
            System.out.println("Para um caminhão ir dessa Zona até a estação de lixo precisa de " + atual.dado.tempo_viagem_estacao_normal + " em horario normal");
            System.out.println("Para um caminhão ir dessa Zona até a estação de lixo precisa de " + atual.dado.tempo_viagem_estacao_pico + " minutos em horario de pico");
            System.out.println("");
            atual = atual.prox;
            
        }
        System.out.println("");
}

 public boolean horarioPico(int hora) { //verifica se determinado horário é horarío de pico
        return (hora >= horario_pico_inicio1 && hora <= horario_pico_fim1) || (hora >= horario_pico_inicio2 && hora <= horario_pico_fim2);
    }
}
