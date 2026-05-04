package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PlayersDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final Position position;
    private final ScoutingService service;
    private final String scoutName;

    private JTable table;

    public PlayersDialog(Window owner, Position position, ScoutingService service, String scoutName) {
        super(owner, "Players — " + position.display(), ModalityType.APPLICATION_MODAL);
        this.position = position;
        this.service = service;
        this.scoutName = scoutName;

        setSize(750, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("Position: " + position.display(), SwingConstants.LEFT);
        title.setBorder(BorderFactory.createEmptyBorder(8, 12, 0, 12));
        add(title, BorderLayout.NORTH);

        table = new JTable();
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnAdd = new JButton("Add Player");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnDetails = new JButton("Details");
        JButton btnEditComment = new JButton("Edit Comment");
        JButton btnClose = new JButton("Close");

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnDetails);
        actions.add(btnEditComment);
        actions.add(btnClose);

        add(actions, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> dispose());

        btnAdd.addActionListener(e -> {
            new AddPlayerDialog(this, position, service, scoutName).setVisible(true);
            refreshTable();
        });

        btnDetails.addActionListener(e -> showDetails());

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a player first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int playerId = (int) table.getValueAt(row, 0);
            String currentName = (String) table.getValueAt(row, 1);

            Position[] options = {Position.CB, Position.FB, Position._6ER, Position._8ER, Position.WIDE_PLAYER, Position.CF};
            Position newPos = (Position) JOptionPane.showInputDialog(this,
                    "Select new position:", "Edit Player",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (newPos == null) return;

            String newName = JOptionPane.showInputDialog(this, "New name:", currentName);
            if (newName == null) return;

            if (service.updatePlayer(playerId, newName, newPos)) {
                service.saveToDisk();
                refreshTable();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a player first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int playerId = (int) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete this player and all reviews?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (service.deletePlayer(playerId)) {
                    service.saveToDisk();
                    refreshTable();
                }
            }
        });

        btnEditComment.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a player first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int playerId = (int) table.getValueAt(row, 0);
            Review last = service.getLatestReviewForPlayer(playerId);
            if (last == null) {
                JOptionPane.showMessageDialog(this, "This player has no reviews yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JTextArea area = new JTextArea(8, 40);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setText(last.getText());

            int res = JOptionPane.showConfirmDialog(
                    this,
                    new JScrollPane(area),
                    "Edit Last Comment",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (res == JOptionPane.OK_OPTION) {
                String newText = area.getText().trim();
                if (service.updateLastReviewText(playerId, newText)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Comment updated.", "Done", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update comment.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = service.tableModelFor(position);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(420);
    }

    private void showDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a player first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int playerId = (int) table.getValueAt(row, 0);
        java.util.List<Review> rs = service.getReviewsForPlayer(playerId);

        StringBuilder sb = new StringBuilder("Reviews:\n\n");
        for (Review r : rs) {
            sb.append(String.format("%s  [%d★]  by %s\n%s\n\n",
                    r.getDate(), r.getStars(), r.getScoutName(), r.getText()));
        }

        JOptionPane.showMessageDialog(this,
                new JScrollPane(new JTextArea(sb.toString())),
                "Player #" + playerId, JOptionPane.PLAIN_MESSAGE);
    }

    public String getScoutName() {
        return scoutName;
    }
}