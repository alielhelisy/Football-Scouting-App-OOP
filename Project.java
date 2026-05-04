package Project;

import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Project extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JPasswordField Password;
    private JTextField Username;
    private JLabel Message;
    private char defaultEchoChar;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignore) {}

        EventQueue.invokeLater(() -> {
            Project frame = new Project();
            frame.setVisible(true);
        });
    }

    public Project() {
        setTitle("Library System — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 360);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JLabel header = new JLabel("SIGN IN");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        header.setOpaque(true);
        header.setBackground(new Color(210, 255, 240));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(header, BorderLayout.NORTH);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(16, 0, 0, 0));
        root.add(contentPane, BorderLayout.CENTER);

        JLabel lblUser = new JLabel("Username");
        Username = new JTextField();
        Username.setColumns(20);

        JLabel lblPass = new JLabel("Password");
        Password = new JPasswordField();
        defaultEchoChar = Password.getEchoChar();

        JCheckBox showPassword = new JCheckBox("Show Password");

        Message = new JLabel(" ");
        Message.setFont(Message.getFont().deriveFont(11f));
        Message.setForeground(new Color(200, 0, 0));

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setAutoCreateGaps(true);
        gl_contentPane.setAutoCreateContainerGaps(true);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addComponent(lblUser)
                .addComponent(Username)
                .addComponent(lblPass)
                .addComponent(Password)
                .addComponent(showPassword)
                .addComponent(Message)
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createSequentialGroup()
                .addComponent(lblUser)
                .addComponent(Username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(16)
                .addComponent(lblPass)
                .addComponent(Password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(showPassword)
                .addGap(8)
                .addComponent(Message)
        );
        contentPane.setLayout(gl_contentPane);

        JButton btnLogin  = new JButton("Login");
        JButton btnCreate = new JButton("Create Account");
        JButton btnCancel = new JButton("Cancel");

        btnCreate.setBorderPainted(false);
        btnCreate.setContentAreaFilled(false);
        btnCreate.setFocusPainted(false);
        btnCreate.setForeground(new Color(25, 118, 210));

        JPanel actions = new JPanel();
        root.add(actions, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnLogin);

        GroupLayout gl_actions = new GroupLayout(actions);
        gl_actions.setHorizontalGroup(
            gl_actions.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_actions.createSequentialGroup()
                    .addGap(226)
                    .addComponent(btnCreate)
                    .addGap(8)
                    .addComponent(btnCancel)
                    .addGap(8)
                    .addComponent(btnLogin))
        );
        gl_actions.setVerticalGroup(
            gl_actions.createParallelGroup(Alignment.LEADING)
                .addComponent(btnCreate)
                .addComponent(btnCancel)
                .addComponent(btnLogin)
        );
        actions.setLayout(gl_actions);

        showPassword.addItemListener(e ->
            Password.setEchoChar(e.getStateChange() == ItemEvent.SELECTED ? (char) 0 : defaultEchoChar)
        );

        btnCancel.addActionListener(e -> {
            Username.setText("");
            Password.setText("");
            Message.setText(" ");
            Username.requestFocusInWindow();
        });

        btnCreate.addActionListener(e -> new SignupDialog(this).setVisible(true));

        btnLogin.addActionListener(e -> {
            String u = Username.getText().trim();
            String p = new String(Password.getPassword());
            try {
                String role = Users.login(u, p);
                Message.setText("Welcome " + u + " (" + role + ")");
                new DashboardFrame(u, role).setVisible(true);
                dispose();
            } catch (AuthException ex) {
                Message.setText("Error: " + ex.getMessage());
            } finally {
                Password.setText("");
            }
        });
    }
}