package gui;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import agents.AgentPracovnika;
import charts.LineChart;
import entities.Miesto;
import entities.Pracovnik;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.Config;
import simulation.MySimulation;
import simulation.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class GUI extends JFrame implements ISimDelegate {

    private int iterationCount = 0;

    private int sleepTime = 0;
    private int pocetRadov = 1;
    private int strategia;

    private final JLabel resultLabel;
    private final JLabel replicationLabel;
    private final JLabel intervalSpolahlivostiLabel;
    private final JButton start;
    private final JButton pause;
    private final JButton stop;

    private boolean isRunning = false;

    private int pocetZakaznikov = 0;

    private final JTable[] tables = new JTable[5];
    private final JTextField[] zamestnanciField = new JTextField[3];
    private final JTextField pocetOpakovani;
    private final JCheckBox jCheckBoxParkovisko = new JCheckBox("Parkovisko");
    private final JCheckBox jCheckBoxReklamy = new JCheckBox("Reklamy");

    private static final Calendar calendar = Calendar.getInstance();

    MySimulation salonSimulation;

    public GUI() {
        super("Salón krásy - (ABA - Agent Based Architecture)");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        if (bounds.height <= 720) {
            JOptionPane.showMessageDialog(null, "Pre spravne zobrazenie komponentov treba aplikaciu spustit aspon na FULL HD monitore.");
        }

        final int width = (int) (bounds.width * 0.6);
        final int height = (int) (bounds.height * 0.9);
        final int buttonWidth = 72;
        final int buttonHeight = 24;
        final int space = 5;

        this.setSize(width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        start = new JButton("Start");
        stop = new JButton("Stop");
        pause = new JButton("Pause");
        JButton test = new JButton("Test");
        JButton graf = new JButton("Graf");
        JRadioButton strategia1 = new JRadioButton("Strategia 1");
        JRadioButton strategia2 = new JRadioButton("Strategia 2");
        strategia1.setToolTipText("Prechadza rady rad za radom");
        strategia2.setToolTipText("Ak je prvych 5 miest obsadenych a este som nepreskumal dalsie rady, idem dalej");
        strategia2.setSelected(true);
        strategia = 2;
        ButtonGroup group = new ButtonGroup();
        group.add(strategia1);
        group.add(strategia2);
        pocetOpakovani = new JTextField();
        for (int i = 0; i < 3; i++) {
            zamestnanciField[i] = new JTextField();
        }

        int[] spinnerValues = {1000, 1000 / 2, 1000 / 5, 1000 / 10, 1000 / 25, 1000 / 50, 1000 / 100, 1000 / 250, 1000 / 500, 1, -1, 0};
        String[] spinnerData = {"x1", "x2", "x5", "x10", "x25", "x50", "x100", "x250", "x500", "x1000", "x2000", "Virtual"};
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerData);
        JSpinner spinner = new JSpinner(spinnerListModel);
        spinner.setValue("x500");

        int[] spinnerValues2 = {1, 2, 3};
        String[] spinnerData2 = {"1", "2", "3"};
        SpinnerListModel spinnerListModel2 = new SpinnerListModel(spinnerData2);
        JSpinner spinner2 = new JSpinner(spinnerListModel2);
        spinner2.setValue("1");

        resultLabel = new JLabel();
        replicationLabel = new JLabel();
        intervalSpolahlivostiLabel = new JLabel();
        JLabel resultLabelHint = new JLabel("Simulačný čas:");
        JLabel replicationLabelHint = new JLabel("Replikácia č.:");
        JLabel intervalSpolahlivostiLabelHint = new JLabel("90% interval spoľahlivosti času v salóne:");


        pocetOpakovani.setToolTipText("Pocet opakovani");
        zamestnanciField[0].setToolTipText("Recepcne");
        zamestnanciField[1].setToolTipText("Kadernicky");
        zamestnanciField[2].setToolTipText("Kozmeticky");
        pocetOpakovani.setText("1000");
        zamestnanciField[0].setText("2");
        zamestnanciField[1].setText("9");
        zamestnanciField[2].setText("8");

        jCheckBoxReklamy.setToolTipText("Ak su zapnute, prichadza o 20% viac zakaznikov");
        jCheckBoxParkovisko.setToolTipText("Ak je zapnute, prichadzaju zakaznici aj na aute");

        spinner.setToolTipText("Rychlost");
        spinner2.setToolTipText("Pocet radov");
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spinner2.getEditor()).getTextField().setEditable(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane[] tablesScrollPane = new JScrollPane[tables.length];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = new JTable();
            tables[i].setDefaultRenderer(String.class, centerRenderer);
            tables[i].setDefaultRenderer(Integer.class, centerRenderer);
            tables[i].setDefaultRenderer(Double.class, centerRenderer);
            tablesScrollPane[i] = new JScrollPane(tables[i]);
            panel.add(tablesScrollPane[i]);
        }
        tables[0].setModel(new DefaultTableModel(new Object[][]{{"Recepcia", 0, 0.0, 0.0}, {"Ucesy", 0, 0.0, 0.0}, {"Licenie", 0, 0.0, 0.0}}, new String[]{"Pracovisko", "Aktualny rad", "Priemerna dlzka", "Celkova priemerna dlzka"}));
        String[] table4Data = new String[Config.miestRadu + 1];
        table4Data[0] = "Rad";
        for (int i = 1; i < table4Data.length; i++) {
            table4Data[i] = Config.miestRadu - i + 1 + "";
        }
        tables[4].setModel(new DefaultTableModel(new Object[][]{{"Rad 1"}, {"Rad 2"}, {"Rad 3"}}, table4Data));

        for (int i = 0; i < 4; i++) {
            TableColumn column = tables[0].getColumnModel().getColumn(i);
            if (i != 3) {
                column.setPreferredWidth(buttonWidth);
            } else {
                column.setPreferredWidth(buttonWidth + 30);
            }
        }

        for (JTable table : tables) {
            table.setDefaultRenderer(String.class, centerRenderer);
            table.setDefaultRenderer(Integer.class, centerRenderer);
            table.setDefaultRenderer(Double.class, centerRenderer);
        }


        start.addActionListener(e -> {
            if (!isRunning) {
                isRunning = true;
                start.setEnabled(false);
                this.iterationCount = Integer.parseInt(pocetOpakovani.getText());
                int recepcne = Integer.parseInt(zamestnanciField[0].getText());
                int kadernicky = Integer.parseInt(zamestnanciField[1].getText());
                int kozmeticky = Integer.parseInt(zamestnanciField[2].getText());

                Object[][] tableData = new Object[recepcne + kadernicky + kozmeticky][6];
                for (int i = 0; i < recepcne + kadernicky + kozmeticky; i++) {
                    for (int j = 0; j < 6; j++) {
                        tableData[i][j] = " ";
                        if (i < recepcne && j == 0) {
                            tableData[i][j] = "Recepčná č. " + (i + 1);
                        } else if (i < recepcne + kadernicky && j == 0) {
                            tableData[i][j] = "Kaderníčka č. " + (i + 1 - recepcne);
                        } else if (j == 0) {
                            tableData[i][j] = "Kozmetička č. " + (i + 1 - recepcne - kadernicky);
                        }
                    }
                }
                tables[1].setModel(new DefaultTableModel(tableData, new String[]{"Zamestnanec", "Obsluhuje", "Zákaznika č.", "Odpracovaný čas", "Využitie"}));
                tables[2].setModel(new DefaultTableModel(null, new String[]{"Zákaznik", "Stav", "Prichod", "Objednávka", "Účes", "Hlbkové čistenie", "Líčenie", "Platba", "Odchod", "Celkový čas", "Autom", "Parkuje", "Poloha"}));
                tables[2].getColumnModel().getColumn(0).setPreferredWidth(buttonWidth + 50);
                tables[2].getColumnModel().getColumn(1).setPreferredWidth(buttonWidth + 80);
                tables[2].getColumnModel().getColumn(3).setPreferredWidth(buttonWidth + 40);
                tables[2].getColumnModel().getColumn(5).setPreferredWidth(buttonWidth + 60);
                tables[2].getColumnModel().getColumn(9).setPreferredWidth(buttonWidth + 60);
                tables[2].getColumnModel().getColumn(10).setPreferredWidth(buttonWidth - 10);
                tables[2].getColumnModel().getColumn(11).setPreferredWidth(buttonWidth - 5);

                tables[4].setModel(new DefaultTableModel(new Object[][]{{"Rad 1"}, {"Rad 2"}, {"Rad 3"}}, table4Data));

                pocetZakaznikov = 0;

                for (int i = 0; i < 4; i++) {
                    TableColumn column = tables[1].getColumnModel().getColumn(i);
                    if (i == 0) {
                        column.setPreferredWidth(buttonWidth + 40);
                    } else if (i == 3) {
                        column.setPreferredWidth(buttonWidth + 60);
                    } else {
                        column.setPreferredWidth(buttonWidth);
                    }
                }

                Properties properties = new Properties(recepcne, kadernicky, kozmeticky, jCheckBoxParkovisko.isSelected(), pocetRadov, strategia, jCheckBoxReklamy.isSelected());
                salonSimulation = new MySimulation(properties);
                salonSimulation.registerDelegate(this);

                String valueStr = (String) spinner.getValue();
                int value = 100;
                for (int i = 0; i < spinnerData.length; i++) {
                    if (spinnerData[i].equals(valueStr)) {
                        value = spinnerValues[i];
                        break;
                    }
                }
                int sleepTime = value;
                if (salonSimulation != null) {
                    setSleepTime(sleepTime);
                }

                Object[][] tableData2 = new Object[salonSimulation.getStatsNames().length][2];
                for (int i = 0; i < salonSimulation.getStatsNames().length; i++) {
                    tableData2[i][0] = salonSimulation.getStatsNames()[i];
                }
                tables[3].setModel(new DefaultTableModel(tableData2, new String[]{"Názov", "Hodnota", "Priemerná hodnota"}));
                tables[3].getColumnModel().getColumn(0).setPreferredWidth(buttonWidth * 2);
                tables[3].getColumnModel().getColumn(2).setPreferredWidth(buttonWidth * 2 - 20);

                SimulationThread simulationThread = new SimulationThread();
                simulationThread.start();

                replicationLabel.setText(salonSimulation.currentReplication() + 1 + "");

                salonSimulation.onSimulationWillStart(s -> {
                });

                salonSimulation.onReplicationWillStart(s -> replicationLabel.setText(salonSimulation.currentReplication() + 1 + ""));

                salonSimulation.onReplicationDidFinish(s -> setSleepTime(this.sleepTime));

                salonSimulation.onSimulationDidFinish(s -> {
                    this.refresh(salonSimulation);

                    BufferedWriter writer;
                    BufferedWriter writer2;
                    try {
                        File f = new File("statistiky.csv");
                        if (!f.exists()) {
                            writer = new BufferedWriter(new FileWriter("statistiky.csv", true));
                            writer.write("Recepcne,Kadernicky,Kozmeticky,Cas v salone (hod),Cas na objednavku (min),Zaparkovalo (%),Obsluzenych\n");
                        } else {
                            writer = new BufferedWriter(new FileWriter("statistiky.csv", true));
                        }
                        f = new File("spokojnost.csv");
                        if (!f.exists()) {
                            writer2 = new BufferedWriter(new FileWriter("spokojnost.csv", true));
                            writer2.write("Recepcne,Kadernicky,Kozmeticky,Spokojnost,Pocet radov,Strategia\n");
                        } else {
                            writer2 = new BufferedWriter(new FileWriter("spokojnost.csv", true));
                        }
                        writer.write(recepcne + "," + kadernicky + "," + kozmeticky + "," + salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.currentReplication() + "," + salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.currentReplication() + "," + salonSimulation.getStatsAllVykonov()[11] / salonSimulation.getStatsAllVykonov()[10] * 100.0 + "," + salonSimulation.getStatsAllVykonov()[9] / salonSimulation.currentReplication() + "\n");
                        writer2.write(recepcne + "," + kadernicky + "," + kozmeticky + "," + salonSimulation.getStatsAllVykonov()[12] / salonSimulation.getStatsAllVykonov()[11] + "," + pocetRadov + "," + strategia + "\n");
                        writer.close();
                        writer2.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                stop.setEnabled(true);
                pause.setEnabled(true);
            }
        });

        stop.setEnabled(false);
        pause.setEnabled(false);

        stop.addActionListener(e -> {
            if (salonSimulation != null) {
                salonSimulation.stopReplication();
                salonSimulation.stopSimulation();
            }
            this.stop();
        });

        pause.addActionListener(e -> {
            if (salonSimulation != null) {
                if (salonSimulation.isPaused()) {
                    salonSimulation.resumeSimulation();
                    pause.setText("Pause");
                    pause.setSize(buttonWidth, buttonHeight);
                } else {
                    salonSimulation.pauseSimulation();
                    pause.setText("Continue");
                    pause.setSize(buttonWidth + 12, buttonHeight);
                }
            }
        });

        test.addActionListener(e -> {
            TestThread thread = new TestThread();
            thread.start();
        });

        graf.addActionListener(e -> {
            GrafThread thread = new GrafThread();
            thread.start();
        });

        pocetOpakovani.addActionListener(e -> this.iterationCount = Integer.parseInt(pocetOpakovani.getText()));

        spinner.addChangeListener(e -> {
            String valueStr = (String) spinner.getValue();
            int value = 100;
            for (int i = 0; i < spinnerData.length; i++) {
                if (spinnerData[i].equals(valueStr)) {
                    value = spinnerValues[i];
                    break;
                }
            }
            int sleepTime = value;
            if (salonSimulation != null) {
                setSleepTime(sleepTime);
            }
        });

        spinner2.addChangeListener(e -> {
            String valueStr = (String) spinner2.getValue();
            int value = 2;
            for (int i = 0; i < spinnerData2.length; i++) {
                if (spinnerData2[i].equals(valueStr)) {
                    value = spinnerValues2[i];
                    break;
                }
            }
            this.pocetRadov = value;
        });

        ActionListener actionListener = e -> {
            switch (e.getActionCommand()) {
                case "1":
                    strategia = 1;
                    break;
                case "2":
                    strategia = 2;
                    break;

            }
        };

        strategia1.setActionCommand("1");
        strategia2.setActionCommand("2");

        strategia1.addActionListener(actionListener);
        strategia2.addActionListener(actionListener);

        start.setBounds(130, space, buttonWidth, buttonHeight);
        stop.setBounds(130, space + buttonHeight, buttonWidth, buttonHeight);
        pause.setBounds(130, space + buttonHeight * 2, buttonWidth, buttonHeight);
        test.setBounds(width - space - 80, space, 60, buttonHeight);
        graf.setBounds(width - space - 80, space + buttonHeight, 60, buttonHeight);

        for (int i = 0; i < 3; i++) {
            zamestnanciField[i].setBounds(space + 40 * i, space, 40, buttonHeight);
        }
        pocetOpakovani.setBounds(space, space + buttonHeight, 120, buttonHeight);
        spinner.setBounds(space, 2 * space + buttonHeight * 2, buttonWidth - 5, buttonHeight + 10);
        jCheckBoxReklamy.setBounds(space, 3 * space + buttonHeight * 3 + 8, buttonWidth + 10, buttonHeight);
        resultLabelHint.setBounds((width / 4) - (buttonWidth / 2), space + buttonHeight + 1, buttonWidth + 20, buttonHeight);
        resultLabel.setBounds((width / 4) - (buttonWidth / 2) + buttonWidth + 20, space + buttonHeight + 1, buttonWidth * 2, buttonHeight);
        replicationLabelHint.setBounds((width / 4) - (buttonWidth / 2), space + 1, buttonWidth + 20, buttonHeight);
        replicationLabel.setBounds((width / 4) - (buttonWidth / 2) + buttonWidth + 20, space + 1, buttonWidth * 2, buttonHeight);
        intervalSpolahlivostiLabelHint.setBounds(space, height - buttonHeight - 60, buttonWidth + 160, buttonHeight);
        intervalSpolahlivostiLabel.setBounds(space + buttonWidth + 160, height - buttonHeight - 60, buttonWidth * 2, buttonHeight);
        tablesScrollPane[0].setBounds(space, space + buttonHeight * 5, 480, 72);
        tablesScrollPane[1].setBounds(space, space * 4 + buttonHeight * 5 + 72, 480, 232 + 9);
        tablesScrollPane[2].setBounds(space, space * 8 + buttonHeight * 5 + 72 + 232, 960, 300);
        tablesScrollPane[3].setBounds(space * 4 + 480, space + buttonHeight * 5, 360, 248 + 16 * 5);
        tablesScrollPane[4].setBounds(space, space * 8 + buttonHeight * 5 + 72 + 232 + 320, 840, 72);

        jCheckBoxParkovisko.setBounds(space * 4 + 480, space, buttonWidth * 4, buttonHeight);
        spinner2.setBounds(space * 4 + 480 + space, space + buttonHeight + 5, buttonWidth - 15, buttonHeight + 10);

        strategia1.setBounds(space * 4 + 480, space + buttonHeight * 3, buttonWidth + 20, buttonHeight);
        strategia2.setBounds(space * 4 + 480 + buttonWidth + 20, space + buttonHeight * 3, buttonWidth + 20, buttonHeight);

        panel.add(pocetOpakovani);
        panel.add(start);
        panel.add(stop);
        panel.add(pause);
        panel.add(test);
        panel.add(graf);
        panel.add(spinner);
        panel.add(spinner2);
        for (int i = 0; i < 3; i++) {
            panel.add(zamestnanciField[i]);
        }
        panel.add(resultLabel);
        panel.add(replicationLabel);
        panel.add(intervalSpolahlivostiLabel);
        panel.add(resultLabelHint);
        panel.add(replicationLabelHint);
        panel.add(intervalSpolahlivostiLabelHint);
        panel.add(jCheckBoxParkovisko);
        panel.add(jCheckBoxReklamy);
        panel.add(strategia1);
        panel.add(strategia2);

        setContentPane(panel);

        int x = bounds.x + bounds.width - insets.right - this.getWidth();
        int y = bounds.y + insets.top + 2;
        setLocation(x, y);

        this.setVisible(true);

    }

    private void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        if (sleepTime == 0) {
            salonSimulation.setMaxSimSpeed();
        } else if (sleepTime == -1) {
            salonSimulation.setSimSpeed(1, 1 / 2000.0);
        } else {
            salonSimulation.setSimSpeed(1, sleepTime / 1000.0);
        }
    }


    private void stop() {
        if (salonSimulation.isPaused()) {
            pause.setText("Pause");
            pause.setSize(72, 24);
        }
        if (!salonSimulation.isRunning()) {
            start.setEnabled(true);
            pause.setEnabled(false);
            stop.setEnabled(false);
        }
        isRunning = false;
    }

    private void graf() {
        int kadernicky = Integer.parseInt(zamestnanciField[1].getText());
        int kozmeticky = Integer.parseInt(zamestnanciField[2].getText());
        LineChart lineChart = new LineChart(String.format("%d Kaderníčok, %d Kozmetičiek", kadernicky, kozmeticky), "Recepčné", "Priemerná dĺžka radu");
        lineChart.setSize(740, 620);
        lineChart.pack();
        lineChart.setVisible(true);
        boolean parkovisko = jCheckBoxParkovisko.isSelected();
        boolean reklamy = jCheckBoxReklamy.isSelected();
        int pocetRadov = this.pocetRadov;
        int strategia = this.strategia;
        for (int recepcne = 1; recepcne <= 10; recepcne++) {
            Properties properties = new Properties(recepcne, kadernicky, kozmeticky, parkovisko, pocetRadov, strategia, reklamy);
            MySimulation salonSimulation = new MySimulation(properties);
            salonSimulation.simulate(Integer.parseInt(pocetOpakovani.getText()));
            lineChart.addPoint(recepcne, salonSimulation.getCelkoveDlzkyRadov()[0] / salonSimulation.currentReplication());
        }
    }

    private void test() throws IOException {
        int min = Integer.MAX_VALUE;
        String solution = "";
        BufferedWriter writer = new BufferedWriter(new FileWriter("testData.csv"));
        writer.write("Recepcne,Kadernicky,Kozmeticky,Cas v salone (hod),Cas na objednavku (min),Zaparkovalo (%)\n");
        boolean parkovisko = jCheckBoxParkovisko.isSelected();
        boolean reklamy = jCheckBoxReklamy.isSelected();
        int pocetRadov = this.pocetRadov;
        int strategia = this.strategia;
        for (int r = 1; r <= 5; r++) {
            for (int u = 5; u <= 15; u++) {
                for (int l = 5; l <= 15; l++) {
                    Properties properties = new Properties(r, u, l, parkovisko, pocetRadov, strategia, reklamy);
                    MySimulation salonSimulation = new MySimulation(properties);
                    salonSimulation.simulate(Integer.parseInt(pocetOpakovani.getText()));
                    System.out.println(r + "," + u + "," + l);
                    System.out.println(salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.currentReplication());
                    System.out.println(salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.currentReplication());
                    System.out.println(salonSimulation.getStatsAllVykonov()[11] / salonSimulation.getStatsAllVykonov()[10] * 100.0);
                    System.out.println();
                    writer.write(r + "," + u + "," + l + "," + salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.currentReplication() + "," + salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.currentReplication() + "," + salonSimulation.getStatsAllVykonov()[11] / salonSimulation.getStatsAllVykonov()[10] * 100.0 + "\n");
                    if (salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.currentReplication() <= 3 && salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.currentReplication() <= 5.5 && salonSimulation.getStatsAllVykonov()[11] / salonSimulation.getStatsAllVykonov()[10] * 100.0 >= 80) {
                        if (r + u + l < min) {
                            min = r + u + l;
                            solution = r + " " + u + " " + l;
                        } else if (r + u + l == min) {
                            solution += "\n" + r + " " + u + " " + l;
                        }
                    }
                }
            }
        }
        writer.close();
        JOptionPane.showMessageDialog(null, "Best solutions: " + solution);
    }

    private static String getTime(int seconds, int startHour) {
        calendar.set(0, Calendar.JANUARY, 0, startHour, 0, 0);
        calendar.add(Calendar.SECOND, seconds);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return (hour < 10 ? "" + 0 + hour : hour) + ":" + (minute < 10 ? "" + 0 + minute : minute) + ":" + (second < 10 ? "" + 0 + second : second);
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {
        switch (simState) {
            case running:
            case replicationRunning:
            case replicationStopped:
                break;
            case stopped:
                this.stop();
                break;
        }
    }

    @Override
    public void refresh(Simulation simulation) {
        MySimulation salonSimulation = (MySimulation) simulation;

        resultLabel.setText(getTime((int) simulation.currentTime(), 9));
        if (salonSimulation.currentReplication() > 30) {
            double s = Math.sqrt((1.0 / (salonSimulation.currentReplication() - 1) * salonSimulation.getxI()[0]) - (1.0 / (salonSimulation.currentReplication() - 1) * salonSimulation.getxI()[1]));
            double start = salonSimulation.getxI()[1] / salonSimulation.currentReplication() - s * 1.645 / Math.sqrt(salonSimulation.currentReplication());
            double end = salonSimulation.getxI()[1] / salonSimulation.currentReplication() + s * 1.645 / Math.sqrt(salonSimulation.currentReplication());
            intervalSpolahlivostiLabel.setText(String.format("<%s, %s>", getTime((int) start, 0), getTime((int) end, 0)));
        }


        tables[0].getModel().setValueAt(salonSimulation.agentRecepcie().getRadSize(), 0, 1);
        tables[0].getModel().setValueAt(salonSimulation.agentUcesov().getRadSize(), 1, 1);
        tables[0].getModel().setValueAt(salonSimulation.agentLicenia().getRadSize(), 2, 1);

        for (int i = 0; i < 3; i++) {
            AgentPracovnika pracovisko;
            if (i == 0)
                pracovisko = salonSimulation.agentRecepcie();
            else if (i == 1)
                pracovisko = salonSimulation.agentUcesov();
            else
                pracovisko = salonSimulation.agentLicenia();
            tables[0].getModel().setValueAt(pracovisko.getDlzkaRaduStat().getValue(), i, 2);
            tables[0].getModel().setValueAt(salonSimulation.getCelkoveDlzkyRadov()[i] / (salonSimulation.currentReplication() + 1), i, 3);
        }

        for (int i = 0; i < salonSimulation.getZamestnanci().size(); i++) {
            Pracovnik zamestnanec = salonSimulation.getZamestnanci().get(i);
            tables[1].getModel().setValueAt(zamestnanec.isObsluhuje() ? "X" : "", i, 1);
            tables[1].getModel().setValueAt(zamestnanec.isObsluhuje() ? zamestnanec.getObsluhujeZakaznika() : "", i, 2);
            tables[1].getModel().setValueAt(getTime((int) zamestnanec.getOdpracovanyCas(), 0), i, 3);
            tables[1].getModel().setValueAt(Math.round(zamestnanec.getVyuzitie() * 100 * 100) / 100.0, i, 4);
        }

        if (salonSimulation.getZakaznici().size() <= 1 && pocetZakaznikov > 1) {
            tables[2].setModel(new DefaultTableModel(null, new String[]{"Zákaznik", "Stav", "Prichod", "Objednávka", "Účes", "Hlbkové čistenie", "Líčenie", "Platba", "Odchod", "Celkový čas", "Autom", "Parkuje", "Poloha"}));
            tables[2].getColumnModel().getColumn(0).setPreferredWidth(72 + 50);
            tables[2].getColumnModel().getColumn(1).setPreferredWidth(72 + 80);
            tables[2].getColumnModel().getColumn(3).setPreferredWidth(72 + 40);
            tables[2].getColumnModel().getColumn(5).setPreferredWidth(72 + 60);
            tables[2].getColumnModel().getColumn(9).setPreferredWidth(72 + 60);
            tables[2].getColumnModel().getColumn(10).setPreferredWidth(72 - 10);
            tables[2].getColumnModel().getColumn(11).setPreferredWidth(72 - 5);
            pocetZakaznikov = 0;
        }

        for (int i = 0; i < salonSimulation.getZakaznici().size(); i++) {
            Zakaznik zakaznikSalonu = salonSimulation.getZakaznici().get(i);
            if (pocetZakaznikov < salonSimulation.getZakaznici().size()) {
                for (int j = pocetZakaznikov; j < salonSimulation.getZakaznici().size(); j++) {
                    DefaultTableModel model = (DefaultTableModel) tables[2].getModel();
                    model.addRow(new Object[]{"Zákazník č. " + ++pocetZakaznikov});
                }

            }
            tables[2].getModel().setValueAt(zakaznikSalonu.getStavZakaznika(), i, 1);

            if (zakaznikSalonu.getCasPrichodu() > 0) {
                tables[2].getModel().setValueAt(getTime((int) zakaznikSalonu.getCasPrichodu(), 9), i, 2);
            }

            if (zakaznikSalonu.getCasOdchodu() > 0) {
                tables[2].getModel().setValueAt(getTime((int) zakaznikSalonu.getCasOdchodu(), 9), i, 8);
                tables[2].getModel().setValueAt(getTime((int) (zakaznikSalonu.getCasOdchodu() - zakaznikSalonu.getCasPrichodu()), 0), i, 9);
            }

            tables[2].getModel().setValueAt(zakaznikSalonu.isAutom() ? "X" : "", i, 10);
            tables[2].getModel().setValueAt(zakaznikSalonu.isAutom() && zakaznikSalonu.getStavZakaznika() != StavZakaznika.ODISIEL && zakaznikSalonu.getStavZakaznika() != StavZakaznika.PARKOVANIE && zakaznikSalonu.getStavZakaznika() != StavZakaznika.NEZAPARKOVANE && zakaznikSalonu.getMiesto() != null ? "R" + (zakaznikSalonu.getMiesto().getRad() + 1) + " P" + (zakaznikSalonu.getMiesto().getPozicia() + 1) : "", i, 11);
            tables[2].getModel().setValueAt(zakaznikSalonu.getMiesto() != null ? "R" + (zakaznikSalonu.getMiesto().getRad() + 1) + " P" + (zakaznikSalonu.getMiesto().getPozicia() + 1) : "", i, 12);

            for (int j = 0; j < 5; j++) {
                if (zakaznikSalonu.getCasZaciatkuObsluhy(j) > 0) {
                    tables[2].getModel().setValueAt(getTime((int) zakaznikSalonu.getCasZaciatkuObsluhy(j), 9), i, 3 + j);
                }
            }
        }

        for (int i = 0; i < salonSimulation.getStatsNames().length - 9; i++) {
            tables[3].getModel().setValueAt(salonSimulation.getStatsVykonov()[i], i, 1);
            tables[3].getModel().setValueAt(salonSimulation.getStatsAllVykonov()[i] / (salonSimulation.currentReplication() + 1), i, 2);
        }
        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.agentRecepcie().getCasObjednavky() / salonSimulation.getStatsVykonov()[9]), 0), 10, 1);
        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[1] / (salonSimulation.currentReplication() + 1)), 0), 10, 2);

        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.agentOkolia().getCasVSalone() / salonSimulation.getStatsVykonov()[9]), 0), 11, 1);
        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[0] / (salonSimulation.currentReplication() + 1)), 0), 11, 2);

        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.agentUcesov().getCasUcesov() / (salonSimulation.getStatsVykonov()[0] + salonSimulation.getStatsVykonov()[4])), 0), 12, 1);
        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[2] / (salonSimulation.currentReplication() + 1)), 0), 12, 2);

        tables[3].getModel().setValueAt(salonSimulation.agentParkoviska().getAutom().getValue(), 14, 1);
        tables[3].getModel().setValueAt(salonSimulation.agentParkoviska().getZaparkovalo().getValue(), 15, 1);
        if (salonSimulation.agentParkoviska().getAutom().getValue() != 0)
            tables[3].getModel().setValueAt(1.0 * salonSimulation.agentParkoviska().getZaparkovalo().getValue() / salonSimulation.agentParkoviska().getAutom().getValue() * 100.0 + " %", 16, 1);
        if (salonSimulation.agentParkoviska().getZaparkovalo().getValue() != 0)
            tables[3].getModel().setValueAt(1.0 * salonSimulation.agentParkoviska().getSpokojnost().getValue() / salonSimulation.agentParkoviska().getZaparkovalo().getValue(), 17, 1);
        tables[3].getModel().setValueAt(salonSimulation.agentParkoviska().getAktualObsadenost() * 100 + " %", 18, 1);

        tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[3] / (salonSimulation.currentReplication() + 1)), 0), 13, 2);

        tables[3].getModel().setValueAt(salonSimulation.getStatsAllVykonov()[10] / (salonSimulation.currentReplication() + 1), 14, 2);
        tables[3].getModel().setValueAt(salonSimulation.getStatsAllVykonov()[11] / (salonSimulation.currentReplication() + 1), 15, 2);
        if (salonSimulation.getStatsAllVykonov()[10] != 0)
            tables[3].getModel().setValueAt((salonSimulation.getStatsAllVykonov()[11] / (salonSimulation.currentReplication() + 1)) / (salonSimulation.getStatsAllVykonov()[10] / (salonSimulation.currentReplication() + 1)) * 100.0 + " %", 16, 2);
        if (salonSimulation.getStatsAllVykonov()[11] != 0)
            tables[3].getModel().setValueAt(salonSimulation.getStatsAllVykonov()[12] / salonSimulation.getStatsAllVykonov()[11], 17, 2);

        tables[3].getModel().setValueAt(salonSimulation.getStatsAllVykonov()[13] / (salonSimulation.currentReplication() + 1) * 100 + " %", 18, 2);


        Miesto[][] parkovisko = salonSimulation.agentParkoviska().parkovisko();
        for (int i = 0; i < parkovisko.length; i++) {
            for (int j = 0; j < parkovisko[0].length; j++) {
                if (parkovisko[i][j].getZakaznik() == null) {
                    tables[4].getModel().setValueAt("", i, parkovisko[0].length - j);
                } else {
                    tables[4].getModel().setValueAt("Z" + parkovisko[i][j].getZakaznik().getPoradie(), i, parkovisko[0].length - j);
                }
            }
        }

    }

    private class SimulationThread extends Thread {
        @Override
        public void run() {
            salonSimulation.simulate(iterationCount);
        }
    }

    private class TestThread extends Thread {
        @Override
        public void run() {
            try {
                test();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class GrafThread extends Thread {
        @Override
        public void run() {
            graf();
        }
    }

}

