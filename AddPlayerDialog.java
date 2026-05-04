package Project;

import javax.swing.*;
import java.awt.*;

public class AddPlayerDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final Position position;       
    private final ScoutingService service;
    private final String scoutName;

    private JTextField tfName;
    private JTextField tfTeam;
    private JComboBox<Position> cbPosition;

    public AddPlayerDialog(Window owner, Position position, ScoutingService service, String scoutName) {
        super(owner, "Add Player — " + position.display(), ModalityType.APPLICATION_MODAL);
        this.position = position;
        this.service = service;
        this.scoutName = scoutName;

        setSize(480, 260);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridBagLayout());
        add(form, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Name:"), c);
        tfName = new JTextField();
        c.gridx = 1; c.gridy = 0; c.weightx = 1.0; form.add(tfName, c);
        c.weightx = 0;

        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Team:"), c);
        tfTeam = new JTextField();
        c.gridx = 1; c.gridy = 1; c.weightx = 1.0; form.add(tfTeam, c);
        c.weightx = 0;

        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Position:"), c);
        cbPosition = new JComboBox<>(Position.values());
        cbPosition.setSelectedItem(position);
        c.gridx = 1; c.gridy = 2; c.weightx = 1.0; form.add(cbPosition, c);
        c.weightx = 0;

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnCancel = new JButton("Cancel");
        JButton btnSave = new JButton("Save");
        actions.add(btnCancel);
        actions.add(btnSave);
        add(actions, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnSave);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());
    }

    private void onSave() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter player name.", "Validation", JOptionPane.WARNING_MESSAGE);
            tfName.requestFocusInWindow();
            return;
        }

        String team = tfTeam.getText().trim();
        if (team.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter team name.", "Validation", JOptionPane.WARNING_MESSAGE);
            tfTeam.requestFocusInWindow();
            return;
        }

        Position chosenPos = (Position) cbPosition.getSelectedItem();
        if (chosenPos == null) {
            JOptionPane.showMessageDialog(this, "Select position.", "Validation", JOptionPane.WARNING_MESSAGE);
            cbPosition.requestFocusInWindow();
            return;
        }

        int stars = 1;     
        String comment = "";

        service.addPlayerWithReview(chosenPos, name, stars, comment, scoutName);
        dispose();
    }

    public Position getPosition() {
        return position;
    }
}