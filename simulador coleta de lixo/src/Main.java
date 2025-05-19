import javax.swing.SwingUtilities;

public class Main { //classe principal rodada, roda minha simulação com base no que passo no construtor 
    public static void main(String[] args) {

        int qtdPequenos2 = 0;
        int qtdPequenos4 = 0;
        int qtdPequenos8 = 0;
        int qtdPequenos10 = 2;
        int qtdGrandes = 2;
        int qtdViagens = 3;


        Simulacao simulacao = new Simulacao(qtdPequenos2, qtdPequenos4, qtdPequenos8,
                qtdPequenos10, qtdGrandes, qtdViagens);

        SwingUtilities.invokeLater(() -> { //chamo a interface aqui
            InterfaceSimulacao interfaceSimulacao = new InterfaceSimulacao(simulacao);
            simulacao.setInterface(interfaceSimulacao);
            interfaceSimulacao.setVisible(true);
        });
 
        // simulacao.executar();
    }
}