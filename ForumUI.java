import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ForumUI {
    public ForumUI() {
        // 创建主框架
        JFrame frame = new JFrame("Forum Management");
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

        JTextField searchField = new JTextField("Search by PostID", 20);
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
        sidePanel.setPreferredSize(new Dimension(250, 0)); // 左边栏宽度调整为 250
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
            menuButton.setPreferredSize(new Dimension(200, 40)); // 按钮宽度调整为 200
            menuButton.addActionListener(e -> {
                if (!item.equals("FORUM")) { // 避免当前页面关闭自己
                    frame.dispose(); // 关闭当前窗口
                    switch (item) {
                        case "ACCOUNT":
                        new UserAccountUI("DefaultUser"); // 使用默认用户名
                            break;
                        case "ADOPTION":
                            new AdoptionUI(); // 打开 Adoption 页面
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
        String[] columnNames = {"PostID", "Tag", "Title", "Date", "Location", "Content", "Views", "Operation"};
        Object[][] data = {
                {"101", "Tag1", "Lost Dog", "2023-12-01", "NYC", "View", "100", "DELETE"},
                {"102", "Tag2", "Adoption Event", "2023-11-25", "LA", "View", "200", "EDIT"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable postTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 7; // 仅 Content 和 Operation 列可编辑
            }
        };
        postTable.setRowHeight(30);

        // 自定义 Content 和 Operation 列
        postTable.getColumn("Content").setCellEditor(new DefaultCellEditor(createContentComboBox()));
        postTable.getColumn("Operation").setCellEditor(new DefaultCellEditor(createOperationComboBox()));

        JScrollPane scrollPane = new JScrollPane(postTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 搜索按钮功能
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            boolean postFound = false;

            // 遍历表格数据
            for (int i = 0; i < postTable.getRowCount(); i++) {
                String postID = postTable.getValueAt(i, 0).toString();
                if (postID.equals(searchText)) {
                    postTable.setRowSelectionInterval(i, i); // 高亮显示找到的行
                    postFound = true;
                    break;
                }
            }

            if (!postFound) {
                JOptionPane.showMessageDialog(frame, "Post not found", "Search Result", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 显示框架
        frame.setVisible(true);
    }

    // 创建 Content 下拉菜单
    private static JComboBox<String> createContentComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"View", "Details", "Summary"});
        comboBox.setPrototypeDisplayValue("View");
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

    // 创建 Operation 下拉菜单
    private static JComboBox<String> createOperationComboBox() {
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
        new ForumUI();
    }

}