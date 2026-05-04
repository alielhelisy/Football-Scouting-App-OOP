package Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignupDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField txtUser;
    private JPasswordField pwd1, pwd2;
    private JComboBox<String> cmbRole;

    public SignupDialog(Frame owner) {
        super(owner, "Create a new account", true);
        setSize(360, 280);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        txtUser = new JTextField(18);
        pwd1    = new JPasswordField(18);
        pwd2    = new JPasswordField(18);
        cmbRole = new JComboBox<>(new String[] { "SCOUT", "ADMIN" });

        int r = 0;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Username"), gc);
        gc.gridx = 1; form.add(txtUser, gc); r++;

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Password"), gc);
        gc.gridx = 1; form.add(pwd1, gc); r++;

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Confirm Password"), gc);
        gc.gridx = 1; form.add(pwd2, gc); r++;

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Role"), gc);
        gc.gridx = 1; form.add(cmbRole, gc); r++;

        root.add(form, BorderLayout.CENTER);

        JButton btnCancel = new JButton("Cancel");
        JButton btnCreate = new JButton("Create");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.add(btnCancel);
        actions.add(btnCreate);
        root.add(actions, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnCreate);
        btnCancel.addActionListener(e -> dispose());
        btnCreate.addActionListener(e -> onCreate());
    }

    private void onCreate() {
        String u = txtUser.getText().trim();
        char[] p1 = pwd1.getPassword();
        char[] p2 = pwd2.getPassword();
        String role = ((String) cmbRole.getSelectedItem()).toUpperCase();

        if (u.isEmpty() || p1.length == 0 || p2.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!java.util.Arrays.equals(p1, p2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!role.equals("SCOUT") && !role.equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Role must be SCOUT or ADMIN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = Users.createAccount(u, new String(p1), role);
        java.util.Arrays.fill(p1, '\0');
        java.util.Arrays.fill(p2, '\0');

        if (ok) {
            JOptionPane.showMessageDialog(this, "Account created successfully as " + role + "!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username exists or invalid data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}