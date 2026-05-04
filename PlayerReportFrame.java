package Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PlayerReportFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ScoutingService service;
    private final String scoutName;

    private JComboBox<String> cmbPlayer;
    private JComboBox<String> cmbRating;
    private JSpinner spnMinutes;
    private JSpinner spnGoals;
    private JComboBox<String> cmbCards;
    private JComboBox<String> cmbRatedPosition;
    private JTextArea txtComments;

    private static final String[] POSITIONS = {"CB", "FB", "6er", "8er", "Wide Player", "CF"};

    public PlayerReportFrame(ScoutingService service, String scoutName) {
        this.service = service;
        this.scoutName = scoutName;

        setTitle("Create Report");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JLabel header = new JLabel("CREATE REPORT");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
        header.setOpaque(true);
        header.setBackground(new Color(210, 255, 240));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBorder(new EmptyBorder(16, 0, 0, 0));
        root.add(content, BorderLayout.CENTER);

        GroupLayout gl = new GroupLayout(content);
        content.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        JLabel lblPlayerTitle = new JLabel("Player");
        lblPlayerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        cmbPlayer = new JComboBox<>();
        cmbPlayer.setEditable(false);
        cmbPlayer.setPrototypeDisplayValue("Select a player ");
        setPlayers(service.listPlayerNames());

        JLabel lblRating = new JLabel("Rating");
        cmbRating = new JComboBox<>();
        cmbRating.addItem("Select");
        for (String r : new String[]{"1","2","2.5","3","3.5","4","4.5","5"}) {
            cmbRating.addItem(r);
        }

        JLabel lblMinutes = new JLabel("Minutes Played");
        spnMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 120, 1));

        JLabel lblGoals = new JLabel("Goals Scored");
        spnGoals = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));

        JLabel lblCards = new JLabel("Received Cards");
        cmbCards = new JComboBox<>(new String[]{"None", "Yellow", "Red"});

        JLabel lblRatedPos = new JLabel("Rated Position");
        cmbRatedPosition = new JComboBox<>();
        cmbRatedPosition.addItem("Select");
        for (String p : POSITIONS) cmbRatedPosition.addItem(p);

        JLabel lblComments = new JLabel("Player Profile Comments");
        txtComments = new JTextArea(6, 20);
        txtComments.setLineWrap(true);
        txtComments.setWrapStyleWord(true);
        JScrollPane commentsScroll = new JScrollPane(txtComments);

        JButton btnSave = new JButton("Save Report");
        JButton btnCancel = new JButton("Cancel");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.add(btnCancel);
        actions.add(btnSave);
        root.add(actions, BorderLayout.SOUTH);

        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.LEADING)
              .addComponent(lblPlayerTitle)
              .addComponent(cmbPlayer)
              .addGroup(
                  gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblRating)
                        .addComponent(cmbRating, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblMinutes)
                        .addComponent(spnMinutes, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblGoals)
                        .addComponent(spnGoals, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblCards)
                        .addComponent(cmbCards, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
              )
              .addComponent(lblRatedPos)
              .addComponent(cmbRatedPosition)
              .addComponent(lblComments)
              .addComponent(commentsScroll)
        );

        gl.setVerticalGroup(
            gl.createSequentialGroup()
              .addComponent(lblPlayerTitle)
              .addComponent(cmbPlayer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addGap(16)
              .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                  .addComponent(lblRating)
                  .addComponent(lblMinutes)
                  .addComponent(lblGoals)
                  .addComponent(lblCards))
              .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                  .addComponent(cmbRating, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(spnMinutes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(spnGoals, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addComponent(cmbCards, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addGap(16)
              .addComponent(lblRatedPos)
              .addComponent(cmbRatedPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addGap(16)
              .addComponent(lblComments)
              .addComponent(commentsScroll)
        );

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());
    }

    public void setPlayers(List<String> players) {
        cmbPlayer.removeAllItems();
        cmbPlayer.addItem("Select a player");
        if (players != null) {
            for (String p : players) cmbPlayer.addItem(p);
        }
    }

    private void onSave() {
        if (!validateForm()) return;

        String playerName = (String) cmbPlayer.getSelectedItem();
        Integer playerId = service.findPlayerIdByName(playerName);
        if (playerId == null) {
            JOptionPane.showMessageDialog(this, "Player not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rating   = (String) cmbRating.getSelectedItem();
        int minutes     = (int) spnMinutes.getValue();
        int goals       = (int) spnGoals.getValue();
        String cards    = (String) cmbCards.getSelectedItem();
        String ratedPos = (String) cmbRatedPosition.getSelectedItem();
        String notes    = txtComments.getText().trim();

        int stars = 3;
        String comment = String.format(
            "Report — Rating:%s | Minutes:%d | Goals:%d | Cards:%s | Rated:%s. %s",
            rating, minutes, goals, cards, ratedPos, notes
        );

        service.addReview(playerId, scoutName, stars, comment);
        JOptionPane.showMessageDialog(this, "Report saved for " + playerName + ".", "Saved", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private boolean validateForm() {
        String player   = (String) cmbPlayer.getSelectedItem();
        String rating   = (String) cmbRating.getSelectedItem();
        String ratedPos = (String) cmbRatedPosition.getSelectedItem();

        if (player == null || player.startsWith("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a player.", "Validation", JOptionPane.WARNING_MESSAGE);
            cmbPlayer.requestFocusInWindow();
            return false;
        }
        if (rating == null || rating.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a Rating.", "Validation", JOptionPane.WARNING_MESSAGE);
            cmbRating.requestFocusInWindow();
            return false;
        }
        if (ratedPos == null || ratedPos.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select Rated Position.", "Validation", JOptionPane.WARNING_MESSAGE);
            cmbRatedPosition.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
