public class Cidade {
    public ListaEncadeada<Zona> zonas = new ListaEncadeada<>();
    public ListaEncadeada<CaminhaoPequeno> caminhoes_pequenos = new ListaEncadeada<>();
    public ListaEncadeada<CaminhaoGrande> caminhoes_grandes = new ListaEncadeada<>();
    public ListaEncadeada<EstacaoTransferencia> estacoes_transferencia = new ListaEncadeada<>();

    // Horários de pico em horas
    public int horario_pico_inicio1 = 12;
    public int horario_pico_fim1 = 14;
    public int horario_pico_inicio2 = 18;
    public int horario_pico_fim2 = 20;

    // Tempo da simulação em milissegundos
    private long tempoGlobalEmMs = 0;

    public Cidade() {
    }

    // Método que avança o tempo da cidade
    public void avancarTempoEmMinutos(long minutos) {
        // Converte minutos para milissegundos
        long milissegundos = minutos * 60 * 1000;
        tempoGlobalEmMs += milissegundos;
    }

    public int getHoraAtualSimulada() {
        long totalSegundos = tempoGlobalEmMs / 1000;
        long minutosTotais = totalSegundos / 60; // Total de minutos
        long horas = (minutosTotais / 60) % 24;
        return (int) horas;
    }

    // Retorna o minuto atual simulada (0 a 59)
    public int getMinutoAtualSimulado() {
        long totalSegundos = tempoGlobalEmMs / 1000;
        long minutosTotais = totalSegundos / 60;
        return (int) (minutosTotais % 60);
    }

    // Verifica se a hora atual simulada está em horário de pico
    public boolean estaEmHorarioPico() {
        int horaAtual = getHoraAtualSimulada();
        return (horaAtual >= horario_pico_inicio1 && horaAtual <= horario_pico_fim1)
                || (horaAtual >= horario_pico_inicio2 && horaAtual <= horario_pico_fim2);
    }

    // métodos de impressão das coisas da cidade: 

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
            System.out.println("Hora que começa: " + atual.dado.horario_inicio/60 + "horas.");
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
            System.out.println(
                    "Tempo de viagem em horário normal: " + atual.dado.tempo_viagem_estacao_normal + " minutos");
            System.out.println(
                    "Tempo de viagem em horário de pico: " + atual.dado.tempo_viagem_estacao_pico + " minutos");
            System.out.println("");
            atual = atual.prox;
        }
        System.out.println("");
    }
}
