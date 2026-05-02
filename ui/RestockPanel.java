package ui;

import simulation.RestaurantEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;


public class RestockPanel extends JPanel {

    private final RestaurantEngine engine;
    private final LogPanel logPanel;
    private final InventoryPanel inventoryPanel;

    private final JComboBox<String> ingredientDropdown;
    private final JSpinner qtySpinner;
    private final JLabel costPreviewLabel;

    public RestockPanel(RestaurantEngine engine, LogPanel logPanel, InventoryPanel inventoryPanel) {
        this.engine=engine;
        this.logPanel=logPanel;
        this.inventoryPanel=inventoryPanel;

        setLayout(new BorderLayout(6, 6));
        setBorder(titledBorder("🛒  Restock"));

        List<String> ingredients=engine.getIngredientNames();
        ingredientDropdown=new JComboBox<>(ingredients.toArray(new String[0]));
        ingredientDropdown.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ingredientDropdown.setBackground(Color.WHITE);

        qtySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        qtySpinner.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ((JSpinner.DefaultEditor) qtySpinner.getEditor()).getTextField().setColumns(3);

        costPreviewLabel = new JLabel("Cost: $—");
        costPreviewLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        costPreviewLabel.setForeground(new Color(0x555555));

        ingredientDropdown.addActionListener(e -> updateCostPreview());
        qtySpinner.addChangeListener(e -> updateCostPreview());
        updateCostPreview();

        JButton buyBtn=new JButton("🛍  Buy Ingredient");
        buyBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        buyBtn.setBackground(new Color(0x1565C0));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFocusPainted(false);
        buyBtn.setBorderPainted(false);
        buyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buyBtn.setPreferredSize(new Dimension(0, 42));

        buyBtn.addActionListener(e->handleBuy());

        JPanel form=new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5, 6, 5, 6);
        gbc.fill=GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Ingredient:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(ingredientDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(qtySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(costPreviewLabel, gbc);

        JLabel infoLabel = new JLabel("<html><i>Select an ingredient and quantity,<br>then click Buy to restock.</i></html>");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(0x777777));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        add(infoLabel, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buyBtn, BorderLayout.SOUTH);
    }

    private void handleBuy() {
        String ingredient=(String)ingredientDropdown.getSelectedItem();
        int qty=(int)qtySpinner.getValue();

        if (ingredient==null) return;

        String result=engine.buyIngredient(ingredient, qty);
        logPanel.append(result);
        inventoryPanel.refresh();
        updateCostPreview();
    }

    private void updateCostPreview() {
        String ingredient = (String) ingredientDropdown.getSelectedItem();
        int    qty        = (int)    qtySpinner.getValue();
        if (ingredient == null) return;

        double unitCost = engine.getIngredientUnitCost(ingredient);
        double total    = unitCost * qty;
        costPreviewLabel.setText(String.format("Cost: $%.2f  (%.2f each)", total, unitCost));
    }

    public void refresh() {
        List<String> ingredients = engine.getIngredientNames();
        ingredientDropdown.removeAllItems();
        ingredients.forEach(ingredientDropdown::addItem);
        updateCostPreview();
    }

    private static TitledBorder titledBorder(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0x42A5F5), 2), title);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        b.setTitleColor(new Color(0x0D47A1));
        return b;
    }
}
