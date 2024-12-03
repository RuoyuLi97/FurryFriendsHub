package com.furryfriendshub.UI;

import com.furryfriendshub.util.IDGenerator;
import com.furryfriendshub.config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;

public class RegisterUI {
    private JFrame frame;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JComboBox<String> userTypeComboBox;

    public RegisterUI() {
        // 创建主窗口
        frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridLayout(1, 2));

        // 左侧图像面板
        JPanel leftPanel = new JPanel();
        JLabel imageLabel = new JLabel(new ImageIcon("path_to_image.jpg")); // 替换为您的图片路径
        leftPanel.add(imageLabel);

        // 右侧注册表单
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        JLabel createAccountTitle = new JLabel("Create Your Account");
        createAccountTitle.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(createAccountTitle, gbc);

        // 用户名
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Username (*):"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        rightPanel.add(usernameField, gbc);

        // 邮箱
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(new JLabel("Email (*):"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        rightPanel.add(emailField, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Password (*):"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        rightPanel.add(passwordField, gbc);

        // 电话号码
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(new JLabel("Phone Number (*):"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        rightPanel.add(phoneField, gbc);

        // 用户类型
        gbc.gridx = 0;
        gbc.gridy = 5;
        rightPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        userTypeComboBox = new JComboBox<>(new String[] { "Admin", "User" });
        rightPanel.add(userTypeComboBox, gbc);

        // 注册按钮
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(registerButton, gbc);

        // 按钮监听器
        registerButton.addActionListener(e -> registerUser());

        // 添加面板到主窗口
        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.setVisible(true);
    }

    private void registerUser() {
        try {
            // 获取输入信息
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String phone = phoneField.getText();
            String userType = (String) userTypeComboBox.getSelectedItem();

            // 验证输入
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || userType == null) {
                JOptionPane.showMessageDialog(frame, "Please fill out all required fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 生成唯一的 userID
            String userID = IDGenerator.generateId(IDGenerator.EntityType.USER);

            // 将新用户信息保存到 MongoDB
            MongoCollection<Document> usersCollection = MongoDBConnection.getDatabase().getCollection("users");
            Document newUser = new Document("userID", userID)
                    .append("userName", username)
                    .append("email", email)
                    .append("password", password)
                    .append("phoneNumber", phone)
                    .append("userType", userType);
            usersCollection.insertOne(newUser);

            // 显示注册成功的消息
            JOptionPane.showMessageDialog(frame, "Registration successful! Your User ID is: " + userID, "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // 清空表单字段
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error occurred during registration: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        userTypeComboBox.setSelectedIndex(0);
    }
}

