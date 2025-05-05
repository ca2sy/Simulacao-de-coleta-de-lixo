public class Cidade {
    public ListaEncadeada<Zona> zonas = new ListaEncadeada<>();
    public ListaEncadeada<CaminhaoPequeno> caminhoes_pequenos = new ListaEncadeada<>();
    public ListaEncadeada<CaminhaoGrande> caminhoes_grandes  = new ListaEncadeada<>();
    public ListaEncadeada<EstacaoTransferencia> estacoes_transferencia = new ListaEncadeada<>();

    // Horários de pico em horas
    public static final int horario_pico_inicio1 = 12;
    public static final int horario_pico_fim1 = 14;
    public static final int horario_pico_inicio2 = 18;
    public static final int horario_pico_fim2 = 20;

    // Tempo da simulação em milissegundos
    private long tempoGlobalEmMs = 0;

    public Cidade() {}

    // Método que avança o tempo da cidade (ex: 1 min real = 1 min simulado = 60_000ms)
    public void avancarTempo(long milissegundos) {
        tempoGlobalEmMs += milissegundos;
    }

    // Converte tempo atual em horas (0 a 23)
    public int getHoraAtualSimulada() {
        long totalSegundos = tempoGlobalEmMs / 1000;
        long minutos = totalSegundos / 60;
        long horas = (minutos / 60) % 24;
        return (int) horas;
    }

    // Verifica se a hora atual simulada está em horário de pico
    public boolean estaEmHorarioPico() {
        int horaAtual = getHoraAtualSimulada();
        return (horaAtual >= horario_pico_inicio1 && horaAtual <= horario_pico_fim1)
            || (horaAtual >= horario_pico_inicio2 && horaAtual <= horario_pico_fim2);
    }

    public void imprimeListaEstacoes() {
        ListaEncadeada.No<EstacaoTransferencia> atual = estacoes_transferencia.head;
        while (atual != null) {
            System.out.println(atual.dado.nome);
            System.out.println("Localização: " + atual.dado.localizacao.nome);
            atual = atual.prox;
        }
        System.out.println("");
    }

    public void imprimeListaCaminhaoPequeno() {
        ListaEncadeada.No<CaminhaoPequeno> atual = caminhoes_pequenos.head;
        while (atual != null) {
            System.out.println(atual.dado.getIdCaminhaoPequeno());
            System.out.println("Capacidade " + atual.dado.getCapacidade());
            System.out.println("Zonas " + atual.dado.zonas_de_atuacao.listaString());
            System.out.println("");
            atual = atual.prox;
        }
        System.out.println("");
    }

    public void imprimeListaCaminhaoGrande() {
        ListaEncadeada.No<CaminhaoGrande> atual = caminhoes_grandes.head;
        while (atual != null) {
            System.out.println(atual.dado.getIdCaminhaoGrande());
            System.out.println("");
            atual = atual.prox;
        }
        System.out.println("");
    }

    public void imprimeListaZonas() {
        ListaEncadeada.No<Zona> atual = zonas.head;
        while (atual != null) {
            System.out.println(atual.dado.getNome());
            System.out.println("Lixo da zona: " + atual.dado.lixo_zona);
            System.out.println("Descarrega seu lixo na " + atual.dado.estacao_descarga.nome);
            System.out.println("Tempo de viagem em horário normal: " + atual.dado.tempo_viagem_estacao_normal + " minutos");
            System.out.println("Tempo de viagem em horário de pico: " + atual.dado.tempo_viagem_estacao_pico + " minutos");
            System.out.println("");
            atual = atual.prox;
        }
        System.out.println("");
    }
}
