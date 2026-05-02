package ui;

import simulation.RestaurantEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;


public class InventoryPanel extends JPanel {

    private final RestaurantEngine engine;

    private final JLabel cashLabel   = new JLabel();
    private final JPanel ingredientsGrid;

    public InventoryPanel(RestaurantEngine engine) {
        this.engine = engine;

        setLayout(new BorderLayout(6, 6));
        setBorder(titledBorder("Inventory"));

        cashLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        cashLabel.setForeground(new Color(0x1B5E20));

        JPanel cashRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        cashRow.setOpaque(false);
        cashRow.add(new JLabel("Cash:"));
        cashRow.add(cashLabel);

        ingredientsGrid = new JPanel();
        ingredientsGrid.setLayout(new BoxLayout(ingredientsGrid, BoxLayout.Y_AXIS));
        ingredientsGrid.setOpaque(false);

        JScrollPane scroll = new JScrollPane(ingredientsGrid);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xA5D6A7)));
        scroll.getViewport().setBackground(new Color(0xF1F8E9));

        add(cashRow,  BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        cashLabel.setText(String.format("$%.2f", engine.getCash()));

        ingredientsGrid.removeAll();

        Map<String, Integer> stock = engine.getInventorySnapshot();
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            ingredientsGrid.add(buildIngredientRow(entry.getKey(), entry.getValue()));
        }

        ingredientsGrid.revalidate();
        ingredientsGrid.repaint();
    }

    private JPanel buildIngredientRow(String name, int count) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel nameLabel  = new JLabel(ingredientEmoji(name) + "  " + name);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        countLabel.setForeground(count == 0 ? Color.RED : new Color(0x2E7D32));

        JProgressBar bar = new JProgressBar(0, 20);
        bar.setValue(Math.min(count, 20));
        bar.setPreferredSize(new Dimension(70, 10));
        bar.setForeground(count <= 3 ? new Color(0xE53935) : count <= 8 ? new Color(0xFB8C00) : new Color(0x43A047));
        bar.setBorderPainted(false);
        bar.setBackground(new Color(0xE0E0E0));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        right.setOpaque(false);
        right.add(bar);
        right.add(countLabel);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(right,     BorderLayout.EAST);

        row.setBorder(BorderFactory.createCompoundBorder (BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xDCEDC8)), BorderFactory.createEmptyBorder(3, 10, 3, 10)));

        return row;
    }

    private String ingredientEmoji(String name) {
        return switch (name.toUpperCase()) {
            case "BUNS"       -> "🍞";
            case "MEAT"       -> "🥩";
            case "VEGETABLES" -> "🥬";
            default           -> "📦";
        };
    }

    private static TitledBorder titledBorder(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x66BB6A), 2),
                title);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        b.setTitleColor(new Color(0x1B5E20));
        return b;
    }
}
