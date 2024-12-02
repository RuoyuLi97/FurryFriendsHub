import javax.swing.*;
import java.awt.*;

public class RegistrationForm {
    public RegistrationForm() {
        // 创建主框架
        JFrame frame = new JFrame("Registration Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLayout(new GridLayout(1, 2)); // 使用 GridLayout 分为左右两部分

        // 左侧面板
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(135, 174, 195));
        frame.add(leftPanel);

        // 左侧内容
        JLabel logo = new JLabel("LOGO", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setOpaque(true);
        logo.setBackground(new Color(135, 174, 195));
        logo.setForeground(Color.WHITE);

        JLabel welcomeText = new JLabel("Welcome Back!", SwingConstants.CENTER);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 24));

        JButton loginButton = new JButton("LOG IN");
        loginButton.setPreferredSize(new Dimension(120, 40));

        // 登录按钮点击事件
        loginButton.addActionListener(e -> {
            frame.dispose(); // 关闭当前窗口
            new LoginForm(); // 跳转到 LoginForm
        });

        // 布局管理
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(logo, gbc);

        gbc.gridy = 1;
        leftPanel.add(welcomeText, gbc);

        gbc.gridy = 2;
        leftPanel.add(loginButton, gbc);

        // 右侧面板
        JPanel rightPanel = new JPanel(new GridBagLayout());
        frame.add(rightPanel);

        // 右侧标题
        JLabel createAccountTitle = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        createAccountTitle.setFont(new Font("Arial", Font.BOLD, 24));

        // 输入字段
        JTextField usernameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField phoneField = new JTextField(20);

        // 用户类型
        JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"Individual", "Organization"});

        // 注册按钮
        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setPreferredSize(new Dimension(120, 40));

        // 注册按钮点击事件
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String phone = phoneField.getText().trim();
            String userType = (String) userTypeComboBox.getSelectedItem();

            // 检查必填项是否为空
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || userType == null) {
                JOptionPane.showMessageDialog(frame, "All fields marked with (*) are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // 打开 UserAccountUI 并传递用户名
                frame.dispose(); // 关闭当前窗口
                new UserAccountUI(username); // 跳转到 UserAccountUI 并传递用户名
            }
        });

        // 布局右侧内容
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(createAccountTitle, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Username (*):"), gbc);
        gbc.gridx = 1;
        rightPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(new JLabel("Email (*):"), gbc);
        gbc.gridx = 1;
        rightPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Password (*):"), gbc);
        gbc.gridx = 1;
        rightPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        rightPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        rightPanel.add(new JLabel("User Type (*):"), gbc);
        gbc.gridx = 1;
        rightPanel.add(userTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(signUpButton, gbc);

        // 显示框架
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}