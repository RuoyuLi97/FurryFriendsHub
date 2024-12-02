import javax.swing.*;
import java.awt.*;

public class LoginForm {

    public LoginForm() {
        // 创建主框架
        JFrame frame = new JFrame("Login UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLayout(new GridLayout(1, 2)); // 使用 GridLayout 分为左右两部分

        // 左侧面板：登录部分
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);

        // 添加Logo
        JLabel logoLabel = new JLabel("LOGO", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        logoLabel.setOpaque(true);
        logoLabel.setBackground(new Color(135, 174, 195)); // 背景色
        logoLabel.setForeground(Color.WHITE); // 字体颜色
        logoLabel.setPreferredSize(new Dimension(80, 80)); // 小正方形

        // 登录标题
        JLabel loginTitle = new JLabel("LOG IN TO THE HUB", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));

        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField usernameField = new JTextField(20);

        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField passwordField = new JPasswordField(20);

        // “记住我”复选框
        JCheckBox rememberMe = new JCheckBox("Remember Me");
        rememberMe.setBackground(Color.WHITE);
        rememberMe.setFont(new Font("Arial", Font.PLAIN, 14));

        // 登录按钮
        JButton loginButton = new JButton("LOG IN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        // 添加登录按钮点击事件
loginButton.addActionListener(e -> {
    String username = usernameField.getText().trim(); // 获取用户名
    String password = new String(passwordField.getPassword()).trim(); // 获取密码

    if (username.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Username is required", "Error", JOptionPane.ERROR_MESSAGE);
    } else if (password.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Password is required", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        frame.dispose(); // 关闭当前窗口
        new UserAccountUI(username); // 将用户名传递到 UserAccountUI
    }
});

        // 布局管理
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 调整边距，更贴近左上角
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo 放置更靠左上角
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST; // 更靠左上角
        gbc.insets = new Insets(10, 10, 20, 10); // 左上更靠近边界
        loginPanel.add(logoLabel, gbc);

        // 登录标题
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // 居中对齐
        gbc.insets = new Insets(20, 10, 10, 10); // 标题和Logo间距
        loginPanel.add(loginTitle, gbc);

        // 用户名标签和输入框
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // 左对齐
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // “记住我”复选框
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(rememberMe, gbc);

        // 登录按钮
        gbc.gridy = 5;
        loginPanel.add(loginButton, gbc);

        // 右侧面板：注册部分
        JPanel signUpPanel = new JPanel(new GridBagLayout());
        signUpPanel.setBackground(new Color(135, 174, 195)); // 深蓝背景

        // 欢迎文字
        JLabel welcomeText = new JLabel("Hello Friend!", SwingConstants.CENTER);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeText.setForeground(Color.WHITE);

        // 注册按钮
        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));

        // 添加注册按钮点击事件
        signUpButton.addActionListener(e -> {
            frame.dispose(); // 关闭当前窗口
            new RegistrationForm(); // 跳转到 RegistrationForm
        });

        // 布局右侧内容
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        signUpPanel.add(welcomeText, gbc);

        gbc.gridy = 1;
        signUpPanel.add(signUpButton, gbc);

        // 将面板添加到框架
        frame.add(loginPanel);
        frame.add(signUpPanel);

        // 显示框架
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}