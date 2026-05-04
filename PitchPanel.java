package Project;

import javax.swing.*;
import java.awt.*;

public class PitchPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final ScoutingService service;
    private final String scoutName;

    public PitchPanel(ScoutingService service, String scoutName) {
        this.service = service;
        this.scoutName = scoutName;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JButton btnCB   = new JButton("CB");
        JButton btnFB   = new JButton("FB");
        JButton btn6er  = new JButton("6er");
        JButton btn8er  = new JButton("8er");
        JButton btnWide = new JButton("Wide Player");
        JButton btnCF   = new JButton("CF");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);

        c.gridx = 0; c.gridy = 0; add(btnCB, c);
        c.gridx = 1; c.gridy = 0; add(btnFB, c);
        c.gridx = 0; c.gridy = 1; add(btn6er, c);
        c.gridx = 1; c.gridy = 1; add(btn8er, c);
        c.gridx = 0; c.gridy = 2; add(btnWide, c);
        c.gridx = 1; c.gridy = 2; add(btnCF, c);

        btnCB.addActionListener(e -> openPlayers(Position.CB));
        btnFB.addActionListener(e -> openPlayers(Position.FB));
        btn6er.addActionListener(e -> openPlayers(Position._6ER));
        btn8er.addActionListener(e -> openPlayers(Position._8ER));
        btnWide.addActionListener(e -> openPlayers(Position.WIDE_PLAYER));
        btnCF.addActionListener(e -> openPlayers(Position.CF));
    }

    private void openPlayers(Position pos) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        new PlayersDialog(owner, pos, service, scoutName).setVisible(true);
    }
}
