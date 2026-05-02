package ui;

import simulation.RestaurantEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class OrdersPanel extends JPanel {

    private final RestaurantEngine engine;
    private final LogPanel logPanel;
    private final InventoryPanel inventoryPanel;

    private final DefaultListModel<String>listModel = new DefaultListModel<>();
    private final JList<String>orderList = new JList<>(listModel);
    private final JLabel queueSizeLabel;

    public OrdersPanel(RestaurantEngine engine, LogPanel logPanel, InventoryPanel inventoryPanel) {
        this.engine = engine;
        this.logPanel = logPanel;
        this.inventoryPanel = inventoryPanel;

        setLayout(new BorderLayout(6, 6));
        setBorder(titledBorder("📋  Orders Queue"));

        queueSizeLabel = new JLabel("Pending: 0", SwingConstants.RIGHT);
        queueSizeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        queueSizeLabel.setForeground(new Color(0xB71C1C));
        queueSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        orderList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.setBackground(new Color(0xFFFDE7));
        orderList.setCellRenderer(new OrderCellRenderer());

        JScrollPane scroll = new JScrollPane(orderList);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xFFCC80)));

        JButton cookBtn = new JButton("🍳  Cook Next Order");
        cookBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cookBtn.setBackground(new Color(0xE65100));
        cookBtn.setForeground(Color.WHITE);
        cookBtn.setFocusPainted(false);
        cookBtn.setBorderPainted(false);
        cookBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cookBtn.setPreferredSize(new Dimension(0, 42));

        cookBtn.addActionListener(e -> handleCookNext());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(new JLabel("Incoming Orders:"), BorderLayout.WEST);
        topBar.add(queueSizeLabel, BorderLayout.EAST);

        add(topBar,  BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        add(cookBtn, BorderLayout.SOUTH);
    }

    private void handleCookNext() {
        String result = engine.cookNext();   // engine returns a log message
        logPanel.append(result);
        refreshOrderList();
        inventoryPanel.refresh();
    }

    public void refreshOrderList() {
        listModel.clear();
        int position = 1;
        for (String name : engine.getQueueSnapshot()) {
            listModel.addElement(position + ".  " + name);
            position++;
        }
        queueSizeLabel.setText("Pending: " + listModel.size());
    }

    private static class OrderCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            if (!isSelected) {
                label.setBackground(index % 2 == 0 ? new Color(0xFFFDE7) : new Color(0xFFF9C4));
            }
            return label;
        }
    }

    private static TitledBorder titledBorder(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0xFFB300), 2), title);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        b.setTitleColor(new Color(0xE65100));
        return b;
    }
}
