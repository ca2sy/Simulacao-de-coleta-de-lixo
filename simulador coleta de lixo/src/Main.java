import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        int qtdPequenos2 = 1;
        int qtdPequenos4 = 0;
        int qtdPequenos8 = 0;
        int qtdPequenos10 = 0;
        int qtdGrandes = 1;
        int qtdViagens = 1;

        // int qtdPequenos2 = 5;
        // int qtdPequenos4 = 5;
        // int qtdPequenos8 = 5;
        // int qtdPequenos10 = 5;
        // int qtdGrandes = 4;
        // int qtdViagens = 5;

        Simulacao simulacao = new Simulacao(qtdPequenos2, qtdPequenos4, qtdPequenos8,
                qtdPequenos10, qtdGrandes, qtdViagens);

        SwingUtilities.invokeLater(() -> {
            InterfaceSimulacao interfaceSimulacao = new InterfaceSimulacao(simulacao);
            simulacao.setInterface(interfaceSimulacao);
            interfaceSimulacao.setVisible(true);
        });

        // simulacao.executar();
    }
}