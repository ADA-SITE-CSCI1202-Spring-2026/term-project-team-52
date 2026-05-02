package ui;

import simulation.RestaurantEngine;

import javax.swing.*;
import java.awt.*;

/*
 *   ┌─────────────────────────────────────────┐
 *   │  [Orders Panel]    [Inventory Panel]    │
 *   │  [Restock Panel]   [System Log Panel]   │
 *   │         [Save]  [Load]  (toolbar)       │
 *   └─────────────────────────────────────────┘
 *
 */
public class MainWindow extends JFrame {

    private final RestaurantEngine engine;

    private final OrdersPanel ordersPanel;
    private final InventoryPanel inventoryPanel;
    private final RestockPanel restockPanel;
    private final LogPanel logPanel;

    public MainWindow() {
        super("The Silicon Spatula \uD83C\uDF54");

        engine = new RestaurantEngine();

        logPanel       = new LogPanel();
        inventoryPanel = new InventoryPanel(engine);
        ordersPanel    = new OrdersPanel(engine, logPanel, inventoryPanel);
        restockPanel   = new RestockPanel(engine, logPanel, inventoryPanel);

        engine.setOnOrderAdded(order -> SwingUtilities.invokeLater(() -> {
            ordersPanel.refreshOrderList();
            logPanel.append("New Order: " + order.getDisplayName());
        }));

        engine.setOnStateChanged(() -> SwingUtilities.invokeLater(() -> {
            inventoryPanel.refresh();
            ordersPanel.refreshOrderList();
        }));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));
        setLayout(new BorderLayout(8, 8));

        JPanel center = new JPanel(new GridLayout(2, 2, 8, 8));
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        center.add(ordersPanel);
        center.add(inventoryPanel);
        center.add(restockPanel);
        center.add(logPanel);
        add(center, BorderLayout.CENTER);

        add(buildSaveLoadBar(), BorderLayout.SOUTH);

        engine.start();
        logPanel.append("App started. Welcome to The Silicon Spatula!");

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildSaveLoadBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton saveBtn = new JButton("💾 Save Game");
        JButton loadBtn = new JButton("📂 Load Game");

        styleToolbarButton(saveBtn, new Color(0x2E7D32));
        styleToolbarButton(loadBtn, new Color(0x1565C0));

        saveBtn.addActionListener(e -> handleSave());
        loadBtn.addActionListener(e -> handleLoad());

        bar.add(saveBtn);
        bar.add(loadBtn);
        return bar;
    }

    private void handleSave() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Game");
        fc.setSelectedFile(new java.io.File("spatula_save.txt"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                engine.saveGame(fc.getSelectedFile().getAbsolutePath());
                logPanel.append("Game saved to: " + fc.getSelectedFile().getName());
                JOptionPane.showMessageDialog(this, "Game saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                logPanel.append("ERROR: Save failed — " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Save failed:\n" + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleLoad() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Load Game");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                engine.loadGame(fc.getSelectedFile().getAbsolutePath());
                inventoryPanel.refresh();
                ordersPanel.refreshOrderList();
                restockPanel.refresh();
                logPanel.append("Game loaded from: " + fc.getSelectedFile().getName());
                JOptionPane.showMessageDialog(this, "Game loaded!", "Load", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                logPanel.append("ERROR: Load failed — " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Load failed:\n" + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleToolbarButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
