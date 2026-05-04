package Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ScoutingService service;
    private JCheckBox chkCB, chkFB, chk6, chk8, chkWide, chkCF;
    private JTable tblResults;
    private JButton btnSearch, btnDetails, btnClose;

    public SearchFrame(ScoutingService service) {
        this.service = service;

        setTitle("Search Players by Position");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(8, 8));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        chkCB   = new JCheckBox("CB");
        chkFB   = new JCheckBox("FB");
        chk6    = new JCheckBox("6er");
        chk8    = new JCheckBox("8er");
        chkWide = new JCheckBox("Wide Player");
        chkCF   = new JCheckBox("CF");

        filters.add(new JLabel("Positions:"));
        filters.add(chkCB);
        filters.add(chkFB);
        filters.add(chk6);
        filters.add(chk8);
        filters.add(chkWide);
        filters.add(chkCF);

        btnSearch = new JButton("Search");
        filters.add(btnSearch);
        getContentPane().add(filters, BorderLayout.NORTH);

        tblResults = new JTable();
        getContentPane().add(new JScrollPane(tblResults), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnDetails = new JButton("Open Details");
        btnClose   = new JButton("Close");
        actions.add(btnDetails);
        actions.add(btnClose);
        getContentPane().add(actions, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> dispose());
        btnSearch.addActionListener(e -> runSearch());
        btnDetails.addActionListener(e -> openDetails());

        tblResults.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID","Name","Position","Stars","Last Review","Last By"}) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void runSearch() {
        Set<Position> set = new LinkedHashSet<>();
        if (chkCB.isSelected())   set.add(Position.CB);
        if (chkFB.isSelected())   set.add(Position.FB);
        if (chk6.isSelected())    set.add(Position._6ER);
        if (chk8.isSelected())    set.add(Position._8ER);
        if (chkWide.isSelected()) set.add(Position.WIDE_PLAYER);
        if (chkCF.isSelected())   set.add(Position.CF);

        if (set.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one position.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DefaultTableModel model = service.searchTableModel(set);
        tblResults.setModel(model);

        if (tblResults.getColumnCount() >= 6) {
            tblResults.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblResults.getColumnModel().getColumn(1).setPreferredWidth(180);
            tblResults.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblResults.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblResults.getColumnModel().getColumn(4).setPreferredWidth(320);
            tblResults.getColumnModel().getColumn(5).setPreferredWidth(100);
        }
    }

    private void openDetails() {
        int row = tblResults.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a player first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int playerId = (int) tblResults.getValueAt(row, 0);
        java.util.List<Review> rs = service.getReviewsForPlayer(playerId);

        StringBuilder sb = new StringBuilder("Reviews:\n\n");
        for (Review r : rs) {
            sb.append(String.format("%s  [%d★]  by %s\n%s\n\n",
                    r.getDate(), r.getStars(), r.getScoutName(), r.getText()));
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString())),
                "Player #" + playerId, JOptionPane.PLAIN_MESSAGE);
    }
}