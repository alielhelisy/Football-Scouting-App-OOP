package Project;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Person user;
    private final ScoutingService service = new ScoutingService();

    public Person getUser() {
        return user;
    }
    /**
     * @wbp.parser.constructor
     */
    public DashboardFrame(String username, String role) {
        this(makePerson(username, role));
    }

    public DashboardFrame(Person user) {
        this.user = user;

        setTitle(
            "Scouting Dashboard — " + user.getName() +
            " | Role: " + user.getClass().getSimpleName() +
            " | Quota: " + user.dailyQuota()
        );

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                service.saveToDisk();
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        String roleText = (user instanceof Admin) ? "ADMIN" : "SCOUT";
        JLabel lbl = new JLabel("Welcome, " + user.getName() + " (" + roleText + ")");
        top.add(lbl, BorderLayout.WEST);

        JLabel quotaLbl = new JLabel("Daily Quota: " + user.dailyQuota());
        quotaLbl.setForeground(Color.DARK_GRAY);
        top.add(quotaLbl, BorderLayout.SOUTH);

        JLabel typeLbl = new JLabel("Actual Object Type: " + user.getClass().getSimpleName());
        typeLbl.setForeground(Color.BLUE);
        top.add(typeLbl, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnSearch = new JButton("Search");
        JButton btnCreateReport = new JButton("Create Report");
        right.add(btnCreateReport);
        right.add(btnSearch);
        top.add(right, BorderLayout.EAST);

        getContentPane().add(top, BorderLayout.NORTH);

        btnSearch.addActionListener(e -> new SearchFrame(service).setVisible(true));

        btnCreateReport.addActionListener(e -> {
            PlayerReportFrame f = new PlayerReportFrame(service, getUser().getName());
            f.setLocationRelativeTo(this);
            f.setVisible(true);
        });

        getContentPane().add(new PitchPanel(service, getUser().getName()), BorderLayout.CENTER);
    }

    private static Person makePerson(String username, String role) {
        if (role != null && role.equalsIgnoreCase("ADMIN")) {
            return new Admin(username);
        }
        return new Scout(username, "SC-" + username.toUpperCase());
    }
}