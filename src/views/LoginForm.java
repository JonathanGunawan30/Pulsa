package views;

import controllers.UserController;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginForm extends javax.swing.JFrame {
    private UserController userController;
    private Color primaryColor = new Color(88, 86, 214);
    private Color secondaryColor = new Color(51, 51, 51);
    private int mouseX, mouseY;
    
    public LoginForm() {
        userController = new UserController();
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setupAnimations();
        setBackground(new Color(0, 0, 0, 0));
    }

    private void setupAnimations() {
        Timer fadeTimer = new Timer(30, new ActionListener() {
            float opacity = 0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1f) {
                    opacity = 1f;
                    ((Timer)e.getSource()).stop();
                }
                formPanel.setOpaque(true);
                formPanel.setBackground(new Color(255, 255, 255, (int)(opacity * 255)));
                repaint();
            }
        });
        fadeTimer.start();
    }

    private void initComponents() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(88, 86, 214),
                        getWidth(), getHeight(), new Color(120, 100, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE,
                        0, getHeight(), new Color(250, 250, 255));
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        formPanel.setBorder(new CompoundBorder(
            new ShadowBorder(), 
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.setOpaque(false);
        JButton closeButton = new JButton("×");
        styleCloseButton(closeButton);
        headerPanel.add(closeButton);

        titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(primaryColor);

        subtitleLabel = new JLabel("Welcome back!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(secondaryColor);

        usernameField = createStyledTextField("Username", new ImageIcon("icons/user.png"));
        passwordField = createStyledPasswordField("Password", new ImageIcon("icons/lock.png"));
        loginButton = createStyledButton("Login");
        
        signUpLabel = new JLabel("Don't have an account? Sign up");
        signUpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signUpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLabel.setForeground(primaryColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);

        formPanel.add(headerPanel, createGridBagConstraints(0, 0, GridBagConstraints.LINE_END));
        formPanel.add(titleLabel, createGridBagConstraints(0, 1, GridBagConstraints.CENTER));
        formPanel.add(Box.createVerticalStrut(10), createGridBagConstraints(0, 2, GridBagConstraints.CENTER));
        formPanel.add(subtitleLabel, createGridBagConstraints(0, 3, GridBagConstraints.CENTER));
        formPanel.add(Box.createVerticalStrut(30), createGridBagConstraints(0, 4, GridBagConstraints.CENTER));
        formPanel.add(usernameField, createGridBagConstraints(0, 5, GridBagConstraints.CENTER));
        formPanel.add(passwordField, createGridBagConstraints(0, 6, GridBagConstraints.CENTER));
        formPanel.add(Box.createVerticalStrut(20), createGridBagConstraints(0, 7, GridBagConstraints.CENTER));
        formPanel.add(loginButton, createGridBagConstraints(0, 8, GridBagConstraints.CENTER));
        formPanel.add(Box.createVerticalStrut(20), createGridBagConstraints(0, 9, GridBagConstraints.CENTER));
        formPanel.add(signUpLabel, createGridBagConstraints(0, 10, GridBagConstraints.CENTER));

        mainPanel.add(formPanel);
        add(mainPanel);

        loginButton.addActionListener(e -> handleLogin());
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(primaryColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(primaryColor);
            }
        });

        closeButton.addActionListener(e -> System.exit(0));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });
        
        signUpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            RegisterForm registerForm = new RegisterForm();
            registerForm.setVisible(true); 
        }
    });
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Please correct the following:\n");

        if (username.isEmpty() || username.equals("Username")) {
            highlightError(usernameField);
            errorMessage.append("- Username is required\n");
            isValid = false;
        } else {
            resetField(usernameField);
        }

        if (password.isEmpty() || password.equals("Password")) {
            highlightError(passwordField);
            errorMessage.append("- Password is required\n");
            isValid = false;
        } else {
            resetField(passwordField);
        }

        if (!isValid) {
            showErrorDialog(errorMessage.toString());
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        boolean loginSuccessful = userController.login(username, password);

        if (loginSuccessful) {
            showSuccessDialog("Welcome back, " + username + "!");
             Dashboard dashboard = new Dashboard(username);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            showErrorDialog("Invalid username or password");
            loginButton.setEnabled(true);
            loginButton.setText("Login");
            setupPlaceholder(usernameField, "Username");
            setupPlaceholder(passwordField, "Password");
        }
    }

    private void highlightError(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(20, Color.RED),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }

    private void resetField(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(20, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }

    private void showSuccessDialog(String message) {
        JDialog dialog = new JDialog(this, "Success", true);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new ShadowBorder());

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        messageLabel.setForeground(new Color(46, 125, 50));

        JButton okButton = createStyledButton("OK");
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> {
            dialog.dispose();
        });

        panel.add(messageLabel, createGridBagConstraints(0, 0, GridBagConstraints.CENTER));
        panel.add(Box.createVerticalStrut(20), createGridBagConstraints(0, 1, GridBagConstraints.CENTER));
        panel.add(okButton, createGridBagConstraints(0, 2, GridBagConstraints.CENTER));

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void styleCloseButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(new Color(150, 150, 150));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(150, 150, 150));
            }
        });
    }

    private void showErrorDialog(String message) {
        JDialog dialog = new JDialog(this, "Error", true);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new ShadowBorder());

        JLabel messageLabel = new JLabel("<html><center>" + message.replace("\n", "<br>") + "</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);

        JButton okButton = createStyledButton("OK");
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> dialog.dispose());

        panel.add(messageLabel, createGridBagConstraints(0, 0, GridBagConstraints.CENTER));
        panel.add(Box.createVerticalStrut(20), createGridBagConstraints(0, 1, GridBagConstraints.CENTER));
        panel.add(okButton, createGridBagConstraints(0, 2, GridBagConstraints.CENTER));

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = anchor;
        gbc.insets = new Insets(5, 0, 5, 0);
        return gbc;
    }

    private class RoundBorder extends AbstractBorder {
        private int radius;
        private Color color;

        RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    private class ShadowBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 20));
            for (int i = 0; i < 5; i++) {
                g2.drawRoundRect(x + i, y + i, width - (2 * i) - 1, height - (2 * i) - 1, 20, 20);
            }
            g2.dispose();
        }
    }

    private JTextField createStyledTextField(String placeholder, Icon icon) {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(20, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setPreferredSize(new Dimension(300, 45));
        setupPlaceholder(field, placeholder);
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder, Icon icon) {
        JPasswordField field = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(20, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setPreferredSize(new Dimension(300, 45));
        field.setEchoChar((char) 0);
        setupPlaceholder(field, placeholder);
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setPaint(primaryColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setPaint(primaryColor.brighter());
                } else {
                    g2.setPaint(primaryColor);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(getText(), g2);
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300, 45));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupPlaceholder(JTextComponent textComponent, String placeholder) {
        textComponent.setText(placeholder);
        textComponent.setForeground(new Color(150, 150, 150));

        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textComponent.getText().equals(placeholder)) {
                    textComponent.setText("");
                    textComponent.setForeground(Color.BLACK);
                    if (textComponent instanceof JPasswordField) {
                        ((JPasswordField) textComponent).setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textComponent.getText().isEmpty()) {
                    textComponent.setText(placeholder);
                    textComponent.setForeground(new Color(150, 150, 150));
                    if (textComponent instanceof JPasswordField) {
                        ((JPasswordField) textComponent).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    private JPanel mainPanel;
    private JPanel formPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel signUpLabel;
}