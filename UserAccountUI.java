import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserAccountUI {
    public UserAccountUI(String username) { // 接收用户名作为参数

        // 创建主框架
        JFrame frame = new JFrame("User Account Management");
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

        JTextField searchField = new JTextField("Search by UserID", 20);
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
        JLabel userName = new JLabel(username); // 动态设置用户名
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
                if (!item.equals("ACCOUNT")) { // 避免当前页面关闭自己
                    frame.dispose(); // 关闭当前窗口
                    switch (item) {
                        case "ADOPTION":
                            new AdoptionUI(); // 打开 Adoption 页面
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

        // 表格部分
        String[] columnNames = {"UserID", "Username", "Email", "Phone Number", "LastLoginAt", "registeredAt", "Type", "Operation"};
        Object[][] data = {
                {"1", "JohnDoe", "john@example.com", "1234567890", "1/1/2024", "1/1/2023", "Admin", "DELETE"},
                {"2", "JaneDoe", "jane@example.com", "0987654321", "1/1/2024", "1/1/2023", "User", "EDIT"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // 仅操作列可编辑
            }
        };
        userTable.setRowHeight(30);

        // 自定义操作列
        userTable.getColumn("Operation").setCellEditor(new DefaultCellEditor(createComboBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 搜索按钮功能
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            boolean userFound = false;

            for (int i = 0; i < userTable.getRowCount(); i++) {
                String userID = userTable.getValueAt(i, 0).toString();
                if (userID.equals(searchText)) {
                    userTable.setRowSelectionInterval(i, i);
                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                JOptionPane.showMessageDialog(frame, "User not found", "Search Result", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 显示框架
        frame.setVisible(true);
    }

    // 始终下拉的 JComboBox
    private static JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"DELETE", "EDIT"});
        comboBox.setPrototypeDisplayValue("DELETE");
        comboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                JComboBox<?> box = (JComboBox<?>) e.getSource();
                BasicComboPopup popup = (BasicComboPopup) box.getUI().getAccessibleChild(box, 0);
                SwingUtilities.invokeLater(() -> popup.show(box, 0, box.getHeight()));
            }

            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });
        return comboBox;
    }

    public static void main(String[] args) {
        new UserAccountUI("DefaultUser"); // 主入口传递默认用户名
    }
}