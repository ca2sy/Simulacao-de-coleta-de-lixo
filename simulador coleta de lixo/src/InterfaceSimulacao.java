import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceSimulacao extends JFrame {
    private Simulacao simulacao;
    private JTextArea logArea;
    private JButton iniciarButton;
    private JButton pararButton;
    private JButton estatisticasButton;
    private JPanel controlPanel;
    private JPanel statsPanel;
    private JLabel horaLabel;
    private Timer timerUI;

    public InterfaceSimulacao(Simulacao simulacao) {
        this.simulacao = simulacao;
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        setTitle("Simulação de Coleta de Lixo - Teresina");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Área de log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Botões de controle
        iniciarButton = new JButton("Iniciar Simulação");
        pararButton = new JButton("Parar Simulação");
        pararButton.setEnabled(false);
        estatisticasButton = new JButton("Mostrar Estatísticas");

        // Painel de controle
        controlPanel = new JPanel();
        controlPanel.add(iniciarButton);
        controlPanel.add(pararButton);
        controlPanel.add(estatisticasButton);

        // Painel de estatísticas
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(0, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas em Tempo Real"));

        // Label da hora atual
        horaLabel = new JLabel("Hora: 00:00", JLabel.CENTER);
        horaLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Layout principal
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(horaLabel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(controlPanel, BorderLayout.SOUTH);
        container.add(statsPanel, BorderLayout.EAST);
    }

    private void setupLayout() {
        atualizarEstatisticas();
    }

    private void setupListeners() {
        iniciarButton.addActionListener(e -> iniciarSimulacao());
        pararButton.addActionListener(e -> pararSimulacao());
        estatisticasButton.addActionListener(e -> mostrarEstatisticasCompletas());
    }

    private void iniciarSimulacao() {
        logArea.append("Iniciando simulação...\n");
        iniciarButton.setEnabled(false);
        pararButton.setEnabled(true);

        // Inicia a simulação em uma thread separada para não travar a interface
        new Thread(() -> {
            simulacao.executar();
        }).start();

        // Atualiza a UI periodicamente
        timerUI = new Timer(1000, e -> atualizarInterface());
        timerUI.start();
    }

    private void pararSimulacao() {
        logArea.append("Parando simulação...\n");
        iniciarButton.setEnabled(true);
        pararButton.setEnabled(false);

        if (timerUI != null) {
            timerUI.stop();
        }

        simulacao.pararSimulacao();
    }

    private void atualizarInterface() {
        // Atualiza a hora
        int horas = simulacao.minutosSimulados / 60;
        int minutos = simulacao.minutosSimulados % 60;
        horaLabel.setText(String.format("Hora: %02d:%02d", horas, minutos));

        // Atualiza estatísticas
        atualizarEstatisticas();
    }

    private void atualizarEstatisticas() {
        statsPanel.removeAll();

        // Cálculo das médias
        double mediaViagemPequenos = simulacao.totalViagensPequenos > 0
                ? (double) simulacao.somaTemposViagemPequenos / simulacao.totalViagensPequenos
                : 0;

        double mediaViagemGrandes = simulacao.totalViagensGrandes > 0
                ? (double) simulacao.somaTemposViagemGrandes / simulacao.totalViagensGrandes
                : 0;

        double mediaEsperaPequenos = simulacao.totalAtendimentosEstacoes > 0
                ? (double) simulacao.somaTemposEsperaPequenos / simulacao.totalAtendimentosEstacoes
                : 0;

        // Adiciona todas as estatísticas
        statsPanel.add(new JLabel("Tempo de simulação:"));
        statsPanel.add(new JLabel(String.format("%02d:%02d",
                simulacao.minutosSimulados / 60, simulacao.minutosSimulados % 60)));

        statsPanel.add(new JLabel("Média viagem pequenos:"));
        statsPanel.add(new JLabel(String.format("%.1f min", mediaViagemPequenos)));

        statsPanel.add(new JLabel("Média viagem grandes:"));
        statsPanel.add(new JLabel(String.format("%.1f min", mediaViagemGrandes)));

        statsPanel.add(new JLabel("Média espera pequenos:"));
        statsPanel.add(new JLabel(String.format("%.1f min", mediaEsperaPequenos)));

        statsPanel.add(new JLabel("Máximo espera pequenos:"));
        statsPanel.add(new JLabel(simulacao.maxTempoEsperaPequenos + " min"));

        statsPanel.add(new JLabel("Caminhões grandes:"));
        statsPanel.add(new JLabel(simulacao.totalCaminhoesGrandesAdicionados + " adicionados"));

        statsPanel.add(new JLabel("Lixo coletado:"));
        statsPanel.add(new JLabel(simulacao.totalLixoColetado + " ton"));

        statsPanel.add(new JLabel("Lixo transferido:"));
        statsPanel.add(new JLabel(simulacao.totalLixoTransferido + " ton"));

        statsPanel.add(new JLabel("Lixo descarregado:"));
        statsPanel.add(new JLabel(simulacao.totalLixoDescarregado + " ton"));

        statsPanel.add(new JLabel("Viagens pequenos:"));
        statsPanel.add(new JLabel(simulacao.totalViagensPequenos + ""));

        statsPanel.add(new JLabel("Viagens aterro:"));
        statsPanel.add(new JLabel(simulacao.totalViagensGrandes + ""));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void mostrarEstatisticasCompletas() {
        // Cálculo das médias
        double mediaViagemPequenos = simulacao.totalViagensPequenos > 0
                ? (double) simulacao.somaTemposViagemPequenos / simulacao.totalViagensPequenos
                : 0;

        double mediaViagemGrandes = simulacao.totalViagensGrandes > 0
                ? (double) simulacao.somaTemposViagemGrandes / simulacao.totalViagensGrandes
                : 0;

        double mediaEsperaPequenos = simulacao.totalAtendimentosEstacoes > 0
                ? (double) simulacao.somaTemposEsperaPequenos / simulacao.totalAtendimentosEstacoes
                : 0;

        StringBuilder stats = new StringBuilder();
        stats.append("\n=== ESTATÍSTICAS COMPLETAS ===\n");

        stats.append("Tempo de simulação: ")
                .append(String.format("%02d:%02d", simulacao.minutosSimulados / 60, simulacao.minutosSimulados % 60))
                .append("\n");

        stats.append(String.format("- Tempo médio de viagem (pequenos): %.1f min\n", mediaViagemPequenos));
        stats.append(String.format("- Tempo médio ao aterro (grandes): %.1f min\n", mediaViagemGrandes));
        stats.append(String.format("- Tempo médio de espera (pequenos): %.1f min\n", mediaEsperaPequenos));
        stats.append("- Máximo tempo de espera: ").append(simulacao.maxTempoEsperaPequenos).append(" min\n");
        stats.append("- Caminhões grandes adicionados: ").append(simulacao.totalCaminhoesGrandesAdicionados)
                .append("\n");
        stats.append("- Lixo coletado total: ").append(simulacao.totalLixoColetado).append(" ton\n");
        stats.append("- Lixo transferido entre caminhões: ").append(simulacao.totalLixoTransferido).append(" ton\n");
        stats.append("- Lixo efetivamente descarregado: ").append(simulacao.totalLixoDescarregado).append(" ton\n");
        stats.append("- Total viagens pequenos: ").append(simulacao.totalViagensPequenos).append("\n");
        stats.append("- Total viagens aterro: ").append(simulacao.totalViagensGrandes).append("\n");

        JOptionPane.showMessageDialog(this, stats.toString(), "Estatísticas Completas",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void adicionarLog(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(mensagem + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}