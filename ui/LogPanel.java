package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class LogPanel extends JPanel {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final JTextPane  textPane;
    private final StyledDocument doc;

    private final Style errorStyle;
    private final Style successStyle;
    private final Style infoStyle;
    private final Style defaultStyle;

    public LogPanel() {
        setLayout(new BorderLayout(4, 4));
        setBorder(titledBorder("System Log"));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textPane.setBackground(new Color(0x1E1E1E));

        doc = textPane.getStyledDocument();

        defaultStyle = textPane.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, new Color(0xCCCCCC));

        errorStyle = textPane.addStyle("error", null);
        StyleConstants.setForeground(errorStyle, new Color(0xFF5252));
        StyleConstants.setBold(errorStyle, true);

        successStyle = textPane.addStyle("success", null);
        StyleConstants.setForeground(successStyle, new Color(0x69F0AE));

        infoStyle = textPane.addStyle("info", null);
        StyleConstants.setForeground(infoStyle, new Color(0x40C4FF));

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x333333)));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        clearBtn.setBackground(new Color(0x424242));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.addActionListener(e -> {
            try { doc.remove(0, doc.getLength()); }
            catch (BadLocationException ignored) {}
        });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(new JLabel(" "), BorderLayout.CENTER);
        topBar.add(clearBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    
    public void append(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = "[" + LocalTime.now().format(TIME_FMT) + "] ";
            Style  style     = pickStyle(message);
            try {
                doc.insertString(doc.getLength(), timestamp, infoStyle);
                doc.insertString(doc.getLength(), message + "\n", style);
                // Auto-scroll to bottom
                textPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException ignored) {}
        });
    }


    private Style pickStyle(String msg) {
        if (msg == null) return defaultStyle;
        String upper = msg.toUpperCase();
        if (upper.startsWith("ERROR")) return errorStyle;
        if (upper.startsWith("SUCCESS") || upper.contains("COOKED") || upper.contains("RESTOCKED")) return successStyle;
        if (upper.startsWith("NEW ORDER") || upper.startsWith("GAME")) return infoStyle;
        return defaultStyle;
    }

    private static TitledBorder titledBorder(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0x555555), 2), title);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        b.setTitleColor(new Color(0xAAAAAA));
        return b;
    }
}
