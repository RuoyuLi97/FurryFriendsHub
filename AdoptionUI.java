import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdoptionUI {
    public AdoptionUI() {
        JFrame frame = new JFrame("Adoption Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLayout(new BorderLayout());

        // 顶部部分
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(0, 80));
        topPanel.setBackground(Color.WHITE);
        frame.add(topPanel, BorderLayout.NORTH);

        // 搜索框部分
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField searchField = new JTextField("Search by PetID", 20);
        JButton searchButton = new JButton("Search");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchPanel.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        searchPanel.add(searchButton, gbc);

        topPanel.add(searchPanel, BorderLayout.WEST);

        // 用户信息部分
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setBackground(Color.WHITE);
        JLabel userIcon = new JLabel();
        userIcon.setOpaque(true);
        userIcon.setBackground(new Color(135, 174, 195));
        userIcon.setPreferredSize(new Dimension(40, 40));
        userIcon.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        JLabel userName = new JLabel("Admin Name");
        userName.setFont(new Font("Arial", Font.PLAIN, 16));
        userInfoPanel.add(userIcon);
        userInfoPanel.add(userName);
        topPanel.add(userInfoPanel, BorderLayout.EAST);

        // 左侧控制面板
        JPanel sidePanel = new JPanel(new GridBagLayout());
        sidePanel.setPreferredSize(new Dimension(250, 0));
        sidePanel.setBackground(new Color(135, 174, 195));
        frame.add(sidePanel, BorderLayout.WEST);

        String[] menuItems = {"ACCOUNT", "ADOPTION", "FORUM", "NOTIFICATION"};
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setFont(new Font("Arial", Font.BOLD, 16));
            menuButton.setForeground(Color.WHITE);
            menuButton.setBackground(new Color(72, 108, 123));
            menuButton.setBorderPainted(false);
            menuButton.setFocusPainted(false);
            menuButton.setPreferredSize(new Dimension(200, 40));
            menuButton.addActionListener(e -> {
                if (!item.equals("ADOPTION")) { // 避免当前页面关闭自己
                    frame.dispose(); // 关闭当前窗口
                    switch (item) {
                        case "ACCOUNT":
                        new UserAccountUI("DefaultUser"); // 传递用户名
                        break;
                        case "FORUM":
                            new ForumUI(); // 打开 Forum 页面
                            break;
                        case "NOTIFICATION":
                            new NotificationUI(); // 打开 Notification 页面
                            break;
                    }
                }
            });
            sidePanel.add(menuButton, gbc);
            gbc.gridy++;
        }

        // 中间内容部分
        JPanel contentPanel = new JPanel(new BorderLayout());
        frame.add(contentPanel, BorderLayout.CENTER);

        String[] columnNames = {"PetID", "Name", "BasicInfo", "Date", "Location", "AdoptionStatus", "Views", "Operation"};
        Object[][] data = {
                {"001", "Buddy", "View", "2023-12-01", "NYC", "Available", "100", "DELETE"},
                {"002", "Luna", "View", "2023-11-25", "LA", "Adopted", "200", "EDIT"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 7; // 仅 BasicInfo 和 Operation 列可编辑
            }
        };
        userTable.setRowHeight(30);

        userTable.getColumn("BasicInfo").setCellEditor(new DefaultCellEditor(createBasicInfoComboBox()));
        userTable.getColumn("Operation").setCellEditor(new DefaultCellEditor(createOperationComboBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            boolean petFound = false;

            for (int i = 0; i < userTable.getRowCount(); i++) {
                String petID = userTable.getValueAt(i, 0).toString();
                if (petID.equals(searchText)) {
                    userTable.setRowSelectionInterval(i, i); // 高亮显示找到的行
                    petFound = true;
                    break;
                }
            }

            if (!petFound) {
                JOptionPane.showMessageDialog(frame, "Pet not found", "Search Result", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static JComboBox<String> createBasicInfoComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"View", "Details", "Summary"});
        return comboBox;
    }

    private static JComboBox<String> createOperationComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"DELETE", "EDIT"});
        return comboBox;
    }

    public static void main(String[] args) {
        new AdoptionUI();
    }
}