import engine.order.Order;
import engine.order.OrderFactory;
import engine.simulation.RestaurantEngine;
import inventory.Ingredient;
import inventory.InventoryManager;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.*;

// main GUI class for the restaurant game
public class RestaurantGUI extends JFrame {

    // how much per item at the store
    private static final int RESTOCK_QTY = 5;

    private InventoryManager inventory;
    private RestaurantEngine engine;

    // orders panel widgets
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private JButton cookBtn;

    // inventory panel widgets
    private JLabel cashLabel;
    private Map<Ingredient, JLabel> stockLabels = new EnumMap<>(Ingredient.class);

    // restock panel widgets
    private JComboBox<String> ingredientDropdown;
    private JButton buyBtn;
    private JLabel restockInfoLabel;

    // log panel
    private JTextArea logArea;

    // save file path
    private static final String SAVE_FILE = "savegame.dat";

    public RestaurantGUI() {
        inventory = InventoryManager.withRestaurantDefaults();
        engine = new RestaurantEngine(inventory);
        engine.setLogger(this::appendLog);

        setTitle("Silicon Spatula - Restaurant Tycoon");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setMinimumSize(new Dimension(800, 560));
        setLocationRelativeTo(null);

        buildMenuBar();
        buildUI();

        engine.startSimulation();
        refreshInventoryPanel();

        // keep orders list in sync with engine queue on a fast tick
        javax.swing.Timer syncTimer = new javax.swing.Timer(500, e -> syncOrderList());
        syncTimer.start();
    }

    // menu bar
    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> newGame());

        JMenuItem save = new JMenuItem("Save Game");
        save.addActionListener(e -> saveGame());

        JMenuItem load = new JMenuItem("Load Game");
        load.addActionListener(e -> loadGame());

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> System.exit(0));

        gameMenu.add(newGame);
        gameMenu.add(save);
        gameMenu.add(load);
        gameMenu.addSeparator();
        gameMenu.add(quit);
        bar.add(gameMenu);
        setJMenuBar(bar);
    }

    // layout
    private void buildUI() {
        // top row: orders (left) + inventory (right)
        // bottom row: restock (left) + log (right)
        JPanel top = new JPanel(new GridLayout(1, 2, 6, 0));
        top.add(buildOrdersPanel());
        top.add(buildInventoryPanel());

        JPanel bottom = new JPanel(new GridLayout(1, 2, 6, 0));
        bottom.add(buildRestockPanel());
        bottom.add(buildLogPanel());

        JPanel root = new JPanel(new GridLayout(2, 1, 0, 6));
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        root.add(top);
        root.add(bottom);

        add(root);
    }

    // order panel
    private JPanel buildOrdersPanel() {
        JPanel p = titledPanel("📋  Orders Queue");
        p.setLayout(new BorderLayout(4, 4));

        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        orderList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        orderList.setBackground(new Color(255, 253, 240));
        orderList.setSelectionBackground(new Color(255, 220, 100));

        JScrollPane scroll = new JScrollPane(orderList);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 160, 100)));

        cookBtn = new JButton("🍳  Cook Next Order");
        cookBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cookBtn.setBackground(new Color(220, 100, 60));
        cookBtn.setForeground(Color.WHITE);
        cookBtn.setOpaque(true);
        cookBtn.setBorderPainted(false);
        cookBtn.setFocusPainted(false);
        cookBtn.addActionListener(e -> doCookNext());

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        btnBar.add(cookBtn);

        p.add(scroll, BorderLayout.CENTER);
        p.add(btnBar, BorderLayout.SOUTH);

        return p;
    }

    // updates order list
    private void syncOrderList() {
        java.util.Queue<Order> q = engine.getQueue();
        orderListModel.clear();
        int pos = 1;
        for (Order ord : q) {
            orderListModel.addElement(pos + ".  " + ord.getName() + "  ($" + ord.getPrice() + ")");
            pos++;
        }
    }

    // inventory panel
    private JPanel buildInventoryPanel() {
        JPanel p = titledPanel("🥫  Inventory");
        p.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 8, 4, 8);
        c.weightx = 1;

        // cash row
        c.gridy = 0; c.gridx = 0;
        JLabel cashTitle = new JLabel("💵  Cash:");
        cashTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        p.add(cashTitle, c);

        c.gridx = 1;
        cashLabel = new JLabel("$???");
        cashLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        cashLabel.setForeground(new Color(0, 120, 40));
        p.add(cashLabel, c);

        // separator
        c.gridy = 1; c.gridx = 0; c.gridwidth = 2;
        p.add(new JSeparator(), c);
        c.gridwidth = 1;

        // one row per ingredient
        Ingredient[] ings = Ingredient.values();
        for (int i = 0; i < ings.length; i++) {
            Ingredient ing = ings[i];
            c.gridy = i + 2; c.gridx = 0;

            String emoji = emojiFor(ing);
            JLabel nameLabel = new JLabel(emoji + "  " + prettyName(ing) + ":");
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            p.add(nameLabel, c);

            c.gridx = 1;
            JLabel countLabel = new JLabel("?");
            countLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
            stockLabels.put(ing, countLabel);
            p.add(countLabel, c);
        }

        // push everything to top
        c.gridy = ings.length + 3; c.gridx = 0; c.gridwidth = 2;
        c.weighty = 1;
        p.add(Box.createVerticalGlue(), c);

        return p;
    }

    private void refreshInventoryPanel() {
        cashLabel.setText("$" + inventory.getCash());
        for (Ingredient ing : Ingredient.values()) {
            int count = inventory.getCount(ing);
            JLabel lbl = stockLabels.get(ing);
            lbl.setText(String.valueOf(count));
            lbl.setForeground(count == 0 ? Color.RED : count <= 2 ? new Color(200, 100, 0) : new Color(30, 100, 30));
        }
    }

    // restock panel
    private JPanel buildRestockPanel() {
        JPanel p = titledPanel("🛒  Restock");
        p.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 10, 6, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        // instructions
        c.gridy = 0; c.gridx = 0; c.gridwidth = 2;
        JLabel instr = new JLabel("<html>Select an ingredient and click Buy.<br>Buys <b>" + RESTOCK_QTY + " units</b> at the listed price.</html>");
        instr.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(instr, c);
        c.gridwidth = 1;

        // dropdown label
        c.gridy = 1; c.gridx = 0;
        p.add(new JLabel("Ingredient:"), c);

        // dropdown
        c.gridy = 1; c.gridx = 1;
        String[] options = buildDropdownOptions();
        ingredientDropdown = new JComboBox<>(options);
        ingredientDropdown.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ingredientDropdown.addActionListener(e -> updateRestockInfo());
        p.add(ingredientDropdown, c);

        // info label showing cost
        c.gridy = 2; c.gridx = 0; c.gridwidth = 2;
        restockInfoLabel = new JLabel(" ");
        restockInfoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        restockInfoLabel.setForeground(new Color(80, 80, 80));
        p.add(restockInfoLabel, c);
        c.gridwidth = 1;

        // buy button
        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        buyBtn = new JButton("🛍  Buy Ingredient");
        buyBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        buyBtn.setBackground(new Color(60, 140, 200));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setOpaque(true);
        buyBtn.setBorderPainted(false);
        buyBtn.setFocusPainted(false);
        buyBtn.addActionListener(e -> doBuyIngredient());
        p.add(buyBtn, c);

        // push to top
        c.gridy = 4; c.gridwidth = 2; c.weighty = 1;
        p.add(Box.createVerticalGlue(), c);

        updateRestockInfo();
        return p;
    }

    private String[] buildDropdownOptions() {
        Ingredient[] ings = Ingredient.values();
        String[] opts = new String[ings.length];
        for (int i = 0; i < ings.length; i++) {
            opts[i] = emojiFor(ings[i]) + " " + prettyName(ings[i]) + "  ($" + ings[i].getUnitCost() + "/ea)";
        }
        return opts;
    }

    private void updateRestockInfo() {
        int idx = ingredientDropdown.getSelectedIndex();
        if (idx < 0) return;
        Ingredient ing = Ingredient.values()[idx];
        int totalCost = ing.getUnitCost() * RESTOCK_QTY;
        restockInfoLabel.setText("Will buy " + RESTOCK_QTY + " " + prettyName(ing) + " for $" + totalCost + " total.");
    }

    // system log
    private JPanel buildLogPanel() {
        JPanel p = titledPanel("📜  System Log");
        p.setLayout(new BorderLayout(4, 4));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(20, 20, 20));
        logArea.setForeground(new Color(180, 230, 140));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setMargin(new Insets(4, 6, 4, 6));

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        p.add(scroll, BorderLayout.CENTER);

        appendLog("--- Silicon Spatula started ---");
        appendLog("Auto-orders will arrive every ~4 seconds. Good luck!");

        return p;
    }

    private void appendLog(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    // actions
    private void doCookNext() {
        engine.cookNextOrder();
        syncOrderList();
        refreshInventoryPanel();
    }

    private void doBuyIngredient() {
        int idx = ingredientDropdown.getSelectedIndex();
        if (idx < 0) return;

        Ingredient ing = Ingredient.values()[idx];
        int totalCost = ing.getUnitCost() * RESTOCK_QTY;

        if (inventory.getCash() < totalCost) {
            appendLog("ERROR: Not enough cash to restock " + prettyName(ing) + "! Need $" + totalCost + ", have $" + inventory.getCash());
            JOptionPane.showMessageDialog(this, "Not enough cash!\nNeed $" + totalCost + " but only have $" + inventory.getCash(), "Broke :(", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = inventory.buyIngredient(ing, RESTOCK_QTY, totalCost);
        if (ok) {
            appendLog("Restocked " + RESTOCK_QTY + " " + prettyName(ing) + " for $" + totalCost);
            refreshInventoryPanel();
        } else {
            appendLog("ERROR: Restock failed for " + prettyName(ing) + " (unexpected)");
        }
    }

    // save/load/newgame
    private void saveGame() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
            pw.println("FORMAT=V2");
            pw.println("CASH=" + inventory.getCash());
            for (Ingredient ing : Ingredient.values()) {
                pw.println(ing.name() + "=" + inventory.getCount(ing));
            }
            pw.println("QUEUE_START");
            for (Order ord : engine.getQueue()) {
                pw.println(OrderFactory.templateKey(ord));
            }
            pw.println("QUEUE_END");
            appendLog("Game saved to " + SAVE_FILE);
            JOptionPane.showMessageDialog(this, "Game saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            appendLog("ERROR: Save failed - " + ex.getMessage());
        }
    }

    private void loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No save file found!", "Load", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            engine.getQueue().clear();

            boolean inQueue = false;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if ("QUEUE_START".equals(line)) {
                    inQueue = true;
                    continue;
                }
                if ("QUEUE_END".equals(line)) {
                    inQueue = false;
                    continue;
                }
                if (inQueue) {
                    try {
                        Order restored = OrderFactory.fromTemplateKey(line);
                        engine.getQueue().add(restored);
                    } catch (IllegalArgumentException ex) {
                        appendLog("WARN: Skipped unknown queue entry: " + line);
                    }
                    continue;
                }
                if (!line.contains("=")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                if ("FORMAT".equals(key)) {
                    continue;
                }
                int val;
                try {
                    val = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException ex) {
                    continue;
                }
                if ("CASH".equals(key)) {
                    inventory.setCash(val);
                } else {
                    try {
                        Ingredient ing = Ingredient.valueOf(key);
                        inventory.setCount(ing, val);
                    } catch (IllegalArgumentException ignored) {
                        // unknown key line in older saves
                    }
                }
            }
            appendLog("Game loaded from " + SAVE_FILE);
            refreshInventoryPanel();
            syncOrderList();
            JOptionPane.showMessageDialog(this, "Game loaded!", "Load", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            appendLog("ERROR: Load failed - " + ex.getMessage());
        }
    }

    private void newGame() {
        int confirm = JOptionPane.showConfirmDialog(this, "Start a new game? All progress will be lost.", "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        engine.stopSimulation();
        engine.getQueue().clear();
        orderListModel.clear();

        inventory = InventoryManager.withRestaurantDefaults();
        engine = new RestaurantEngine(inventory);
        engine.setLogger(this::appendLog);
        engine.startSimulation();

        refreshInventoryPanel();
        appendLog("--- New game started ---");
    }

    // helpers
    private JPanel titledPanel(String title) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(140, 140, 180), 1), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13), new Color(60, 60, 120)), new EmptyBorder(4, 4, 4, 4)));
        return p;
    }

    private String prettyName(Ingredient ing) {
        String s = ing.name();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    private String emojiFor(Ingredient ing) {
        switch (ing) {
            case BUNS:return "🍞";
            case MEAT:return "🥩";
            case VEGETABLES:return "🥬";
            case POTATO:return "🥔";
            case SYRUP:return "🧃";
            default:return "📦";
        }
    }

    // entry
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new RestaurantGUI().setVisible(true));
    }
}
