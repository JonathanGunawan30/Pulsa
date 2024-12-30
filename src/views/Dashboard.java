package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.*;
import java.time.*;
import java.time.format.*;

public class Dashboard extends JFrame {
    private String username;
    private Color primaryColor = new Color(88, 86, 214);
    private Color secondaryColor = new Color(51, 51, 51);
    private Color bgColor = new Color(245, 245, 250);
    private JPanel sidebarPanel;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    
    public Dashboard(String username) {
        this.username = username;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setBackground(bgColor);
    }

    private void initComponents() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(bgColor);

        createSidebar();

        createMainPanel();

        containerPanel.add(sidebarPanel, BorderLayout.WEST);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        add(containerPanel);

        addWindowControls();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setLayout(new BorderLayout());

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logoPanel.setBackground(Color.WHITE);
        JLabel logoLabel = new JLabel("MyApp");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(primaryColor);
        logoPanel.add(logoLabel);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        
        String[] menuItems = {"Dashboard", "Analytics", "Reports", "Settings", "Profile"};
        for (String item : menuItems) {
            JPanel menuItem = createMenuItem(item);
            menuPanel.add(menuItem);
            menuPanel.add(Box.createVerticalStrut(5));
        }
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutPanel.setBackground(Color.WHITE);
        JButton logoutButton = createStyledButton("Logout", new Color(220, 53, 69));
        logoutPanel.add(logoutButton);

        sidebarPanel.add(logoPanel, BorderLayout.NORTH);
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(logoutPanel, BorderLayout.SOUTH);

        sidebarPanel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            LoginForm LoginForm = new LoginForm();
            LoginForm.setVisible(true); 
        }
    });
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createHeader();
        createContent();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);

        JLabel welcomeLabel = new JLabel("Welcome back, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(secondaryColor);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        JLabel dateLabel = new JLabel(now.format(formatter));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(128, 128, 128));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(bgColor);
        textPanel.add(welcomeLabel);
        textPanel.add(dateLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
    }

    private void createContent() {
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        String[][] cardData = {
            {"Total Users", "1,234", "↑ 12%"},
            {"Revenue", "$45,678", "↑ 8%"},
            {"Active Projects", "42", "↑ 5%"},
            {"Tasks Complete", "89%", "↑ 3%"}
        };
        
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        for (int i = 0; i < cardData.length; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            contentPanel.add(createDashboardCard(cardData[i][0], cardData[i][1], cardData[i][2]), gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(createChartPanel("Revenue Overview"), gbc);
        
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        contentPanel.add(createChartPanel("User Activity"), gbc);
    }

    private JPanel createMenuItem(String text) {
        JPanel menuItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        menuItem.setBackground(Color.WHITE);
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(secondaryColor);
        
        menuItem.add(label);
        
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(245, 245, 250));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(Color.WHITE);
            }
        });
        
        return menuItem;
    }

    private JPanel createDashboardCard(String title, String value, String change) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            }
        };
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(128, 128, 128));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel changeLabel = new JLabel(change);
        changeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changeLabel.setForeground(new Color(40, 167, 69));
        
        card.add(titleLabel, gbc);
        card.add(Box.createVerticalStrut(5), gbc);
        card.add(valueLabel, gbc);
        card.add(Box.createVerticalStrut(5), gbc);
        card.add(changeLabel, gbc);
        
        return card;
    }

    private JPanel createChartPanel(String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel chartPlaceholder = new JLabel("Chart goes here");
        chartPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        chartPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chartPlaceholder.setForeground(new Color(128, 128, 128));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(chartPlaceholder, BorderLayout.CENTER);
        
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));
        
        return button;
    }

    private void addWindowControls() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false);
        
        JButton minimizeButton = new JButton("─");
        JButton maximizeButton = new JButton("□");
        JButton closeButton = new JButton("×");
        
        JButton[] buttons = {minimizeButton, maximizeButton, closeButton};
        for (JButton button : buttons) {
            styleWindowButton(button);
            controlPanel.add(button);
        }
        
        closeButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));
        maximizeButton.addActionListener(e -> {
            if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                setExtendedState(JFrame.NORMAL);
            }
        });
        
        mainPanel.add(controlPanel, BorderLayout.NORTH);
    }

    private void styleWindowButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(new Color(128, 128, 128));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getText().equals("×")) {
                    button.setForeground(new Color(220, 53, 69));
                } else {
                    button.setForeground(primaryColor);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(128, 128, 128));
            }
        });
    }

    private class ShadowBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 20));
            for (int i = 0; i < 5; i++) {
                g2.drawRoundRect(x + i, y + i, width - (2 * i) - 1, height - (2 * i) - 1, 15, 15);
            }
            g2.dispose();
        }
    }

    
}