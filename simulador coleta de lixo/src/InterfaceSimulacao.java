import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class InterfaceSimulacao extends JFrame {
    private Simulacao simulacao;
    private JTextArea logArea;
    private JSlider velocidade;
    private JButton iniciarButton;
    private JButton pararButton;
    private JButton estatisticasButton;
    private JPanel controlPanel;
    private JPanel statsPanel;
    private JLabel horaLabel;
    private Timer timerUI;
    private int vel;
    
    // Painéis para as diferentes seções
    private JPanel caminhoesPequenosPanel;
    private JPanel estacaoAPanel;
    private JPanel estacaoBPanel;
    private JPanel aterroPanel;

    public InterfaceSimulacao(Simulacao simulacao) {
        this.simulacao = simulacao;
        initComponents();
        setupLayout();
        setupListeners();
        redirectSystemOutput();
    }

    private void initComponents() {
        setTitle("Simulação de Coleta de Lixo - Teresina");
        setSize(1200, 800); // Aumentei o tamanho para acomodar mais informações
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Área de log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Botões de controle
        velocidade = new JSlider();
        velocidade.setMaximum(900);
        velocidade.setMinimum(0);
        iniciarButton = new JButton("Iniciar Simulação");
        pararButton = new JButton("Parar Simulação");
        pararButton.setEnabled(false);
        estatisticasButton = new JButton("Mostrar Estatísticas");

        // Painel de controle
        controlPanel = new JPanel();
        controlPanel.add(velocidade);
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

        // Painel para caminhões pequenos
        caminhoesPequenosPanel = new JPanel();
        caminhoesPequenosPanel.setLayout(new BorderLayout());
        caminhoesPequenosPanel.setBorder(BorderFactory.createTitledBorder("Caminhões Pequenos"));
        JTextArea caminhoesPequenosArea = new JTextArea();
        caminhoesPequenosArea.setEditable(false);
        caminhoesPequenosArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane cpScroll = new JScrollPane(caminhoesPequenosArea);
        caminhoesPequenosPanel.add(cpScroll, BorderLayout.CENTER);

        // Painel para Estação A
        estacaoAPanel = new JPanel();
        estacaoAPanel.setLayout(new BorderLayout());
        estacaoAPanel.setBorder(BorderFactory.createTitledBorder("Estação A"));
        JTextArea estacaoAArea = new JTextArea();
        estacaoAArea.setEditable(false);
        estacaoAArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        estacaoAPanel.add(new JScrollPane(estacaoAArea), BorderLayout.CENTER);

        // Painel para Estação B
        estacaoBPanel = new JPanel();
        estacaoBPanel.setLayout(new BorderLayout());
        estacaoBPanel.setBorder(BorderFactory.createTitledBorder("Estação B"));
        JTextArea estacaoBArea = new JTextArea();
        estacaoBArea.setEditable(false);
        estacaoBArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        estacaoBPanel.add(new JScrollPane(estacaoBArea), BorderLayout.CENTER);

        // Painel para Aterro
        aterroPanel = new JPanel();
        aterroPanel.setLayout(new BorderLayout());
        aterroPanel.setBorder(BorderFactory.createTitledBorder("Aterro"));
        JTextArea aterroArea = new JTextArea();
        aterroArea.setEditable(false);
        aterroArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        aterroPanel.add(new JScrollPane(aterroArea), BorderLayout.CENTER);

        // Painel para os painéis laterais
        JPanel sidePanels = new JPanel();
        sidePanels.setLayout(new GridLayout(4, 1));
        sidePanels.add(caminhoesPequenosPanel);
        sidePanels.add(estacaoAPanel);
        sidePanels.add(estacaoBPanel);
        sidePanels.add(aterroPanel);

        // Layout principal
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(horaLabel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(controlPanel, BorderLayout.SOUTH);
        container.add(sidePanels, BorderLayout.EAST);
    }

    private void redirectSystemOutput() {
    OutputStream out = new OutputStream() {
        @Override
        public void write(int b) {
            // Não faz nada, mas precisa ser implementado
        }

        @Override
        public void write(byte[] b, int off, int len) {
            final String text = new String(b, off, len);
            SwingUtilities.invokeLater(() -> {
                logArea.append(text);
                logArea.setCaretPosition(logArea.getDocument().getLength());
            });
        }
    };

    System.setOut(new PrintStream(out, true));
    System.setErr(new PrintStream(out, true));
}
    private void setupLayout() {
        atualizarEstatisticas();
        atualizarPainelCaminhoesPequenos();
        atualizarPainelEstacaoA();
        atualizarPainelEstacaoB();
        atualizarPainelAterro();
    }

    private void setupListeners() {
        iniciarButton.addActionListener(e -> iniciarSimulacao());
        pararButton.addActionListener(e -> pararSimulacao());
        velocidade.addChangeListener(e -> mudarVelocidade());
        estatisticasButton.addActionListener(e -> mostrarEstatisticasCompletas());
    }

    private void mudarVelocidade(){
        this.vel = 1000 - this.velocidade.getValue();
        this.simulacao.VELOCIDADE = this.vel;
        this.simulacao.INTERVALO_REAL_MS = this.vel;
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
        timerUI = new Timer(1000, e -> {
            atualizarInterface();
            atualizarPainelCaminhoesPequenos();
            atualizarPainelEstacaoA();
            atualizarPainelEstacaoB();
            atualizarPainelAterro();
        });
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

        double mediaEsperaPequenos = simulacao.teresina.caminhoes_pequenos.tamanho > 0
                ? (double) simulacao.somaTemposEsperaPequenos / simulacao.teresina.caminhoes_pequenos.tamanho
                : 0;

        // Adiciona todas as estatísticas
        statsPanel.add(new JLabel("Tempo de simulação:"));
        statsPanel.add(new JLabel(String.format("%02d:%02d",
                simulacao.minutosSimulados / 60, simulacao.minutosSimulados % 60)));

        statsPanel.add(new JLabel("Média espera pequenos:"));
        statsPanel.add(new JLabel(String.format("%.1f min", mediaEsperaPequenos)));

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

    private void atualizarPainelCaminhoesPequenos() {
        JTextArea textArea = (JTextArea) ((JScrollPane) caminhoesPequenosPanel.getComponent(0)).getViewport().getView();
        textArea.setText("");
        
        // Contadores para status
        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("Coletando", 0);
        statusCount.put("Indo para Estação", 0);
        statusCount.put("Esperando Transferência", 0);
        statusCount.put("Indo para Zona", 0);
        statusCount.put("Inativo", 0);
        
        int totalLixoColetado = 0;
        int totalTempoEspera = 0;
        
        if (simulacao.teresina.caminhoes_pequenos != null) {
            ListaEncadeada.No<CaminhaoPequeno> atual = simulacao.teresina.caminhoes_pequenos.head;
            
            textArea.append("=== CAMINHÕES PEQUENOS ===\n");
            textArea.append(String.format("Total: %d\n\n", simulacao.teresina.caminhoes_pequenos.tamanho));
            
            while (atual != null) {
                CaminhaoPequeno caminhao = atual.dado;
                totalLixoColetado += caminhao.carga_atual;
                totalTempoEspera += caminhao.tempo_espera_acumulado;
                
                String status;
                if (caminhao.esta_coletando && caminhao.esta_na_zona) {
                    status = "Coletando";
                } else if (caminhao.esta_indo_pra_estacao) {
                    status = "Indo para Estação";
                } else if (caminhao.esta_na_estacao) {
                    status = "Esperando Transferência";
                } else if (caminhao.esta_indo_pra_zona) {
                    status = "Indo para Zona";
                } else {
                    status = "Inativo";
                }
                
                statusCount.put(status, statusCount.get(status) + 1);
                
                textArea.append(String.format("%s: %s (Carga: %d/%d)\n", 
                    caminhao.id_caminhao_pequeno, 
                    status,
                    caminhao.carga_atual,
                    caminhao.capacidade));
                
                atual = atual.prox;
            }
            
            textArea.append("\n=== RESUMO ===\n");
            textArea.append(String.format("Total Lixo Coletado: %d ton\n", totalLixoColetado));
            textArea.append(String.format("Total Tempo Espera: %d min\n", totalTempoEspera));
            textArea.append("\n=== STATUS ===\n");
            for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
                textArea.append(String.format("%s: %d\n", entry.getKey(), entry.getValue()));
            }
        }
    }

    private void atualizarPainelEstacaoA() {
        JTextArea textArea = (JTextArea) ((JScrollPane) estacaoAPanel.getComponent(0)).getViewport().getView();
        textArea.setText("");
        
        EstacaoTransferencia estacaoA = simulacao.estacao_transferencia_a;
        
        textArea.append("=== ESTAÇÃO A ===\n\n");
        textArea.append(String.format("Lixo Transferido: %d ton\n\n", simulacao.totalLixoTransferido));
        
        textArea.append("=== FILA PEQUENOS ===\n");
        if (estacaoA.fila_caminhao_pequeno.estaVazia()) {
            textArea.append("Nenhum caminhão pequeno\n");
        } else {
            Fila.No<CaminhaoPequeno> atual = estacaoA.fila_caminhao_pequeno.head;
            while (atual != null) {
                CaminhaoPequeno cp = atual.dado;
                textArea.append(String.format("%s: %d/%d ton (Espera: %d min)\n", 
                    cp.id_caminhao_pequeno, 
                    cp.carga_atual, 
                    cp.capacidade,
                    cp.tempo_espera_acumulado));
                atual = atual.prox;
            }
        }
        
        textArea.append("\n=== FILA GRANDES ===\n");
        if (estacaoA.fila_caminhao_grande.estaVazia()) {
            textArea.append("Nenhum caminhão grande\n");
        } else {
            Fila.No<CaminhaoGrande> atual = estacaoA.fila_caminhao_grande.head;
            while (atual != null) {
                CaminhaoGrande cg = atual.dado;
                textArea.append(String.format("%s: %d/%d ton (Espera: %d min)\n", 
                    cg.id_caminhao_grande, 
                    cg.carga_atual, 
                    cg.capacidade_maxima,
                    cg.tempo_espera_acumulado));
                atual = atual.prox;
            }
        }
    }

    private void atualizarPainelEstacaoB() {
        JTextArea textArea = (JTextArea) ((JScrollPane) estacaoBPanel.getComponent(0)).getViewport().getView();
        textArea.setText("");
        
        EstacaoTransferencia estacaoB = simulacao.estacao_transferencia_b;
        
        textArea.append("=== ESTAÇÃO B ===\n\n");
        textArea.append(String.format("Lixo Transferido: %d ton\n\n", simulacao.totalLixoTransferido));
        
        textArea.append("=== FILA PEQUENOS ===\n");
        if (estacaoB.fila_caminhao_pequeno.estaVazia()) {
            textArea.append("Nenhum caminhão pequeno\n");
        } else {
            Fila.No<CaminhaoPequeno> atual = estacaoB.fila_caminhao_pequeno.head;
            while (atual != null) {
                CaminhaoPequeno cp = atual.dado;
                textArea.append(String.format("%s: %d/%d ton (Espera: %d min)\n", 
                    cp.id_caminhao_pequeno, 
                    cp.carga_atual, 
                    cp.capacidade,
                    cp.tempo_espera_acumulado));
                atual = atual.prox;
            }
        }
        
        textArea.append("\n=== FILA GRANDES ===\n");
        if (estacaoB.fila_caminhao_grande.estaVazia()) {
            textArea.append("Nenhum caminhão grande\n");
        } else {
            Fila.No<CaminhaoGrande> atual = estacaoB.fila_caminhao_grande.head;
            while (atual != null) {
                CaminhaoGrande cg = atual.dado;
                textArea.append(String.format("%s: %d/%d ton (Espera: %d min)\n", 
                    cg.id_caminhao_grande, 
                    cg.carga_atual, 
                    cg.capacidade_maxima,
                    cg.tempo_espera_acumulado));
                atual = atual.prox;
            }
        }
    }

    private void atualizarPainelAterro() {
        JTextArea textArea = (JTextArea) ((JScrollPane) aterroPanel.getComponent(0)).getViewport().getView();
        textArea.setText("");
        
        textArea.append("=== ATERRO ===\n\n");
        textArea.append(String.format("Total Viagens: %d\n", simulacao.totalViagensGrandes));
        textArea.append(String.format("Lixo Descarregado: %d ton\n\n", simulacao.totalLixoDescarregado));
        
        textArea.append("=== CAMINHÕES NO ATERRO ===\n");
        boolean temCaminhoes = false;
        
        if (simulacao.teresina.caminhoes_grandes != null) {
            ListaEncadeada.No<CaminhaoGrande> atual = simulacao.teresina.caminhoes_grandes.head;
            while (atual != null) {
                CaminhaoGrande cg = atual.dado;
                if (cg.esta_no_aterro) {
                    textArea.append(String.format("%s: %d ton\n", cg.id_caminhao_grande, cg.carga_atual));
                    temCaminhoes = true;
                }
                atual = atual.prox;
            }
        }
        
        if (!temCaminhoes) {
            textArea.append("Nenhum caminhão no aterro\n");
        }
    }

    private void mostrarEstatisticasCompletas() {
        // Cálculo das médias
        double mediaEsperaPequenos = simulacao.teresina.caminhoes_pequenos.tamanho > 0
                ? (double) simulacao.somaTemposEsperaPequenos / simulacao.teresina.caminhoes_pequenos.tamanho
                : 0;

        StringBuilder stats = new StringBuilder();
        stats.append("\n=== ESTATÍSTICAS COMPLETAS ===\n");

        stats.append("Tempo de simulação: ")
                .append(String.format("%02d:%02d", simulacao.minutosSimulados / 60, simulacao.minutosSimulados % 60))
                .append("\n");

        stats.append(String.format("- Tempo médio de espera (pequenos): %.1f min\n", mediaEsperaPequenos));
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