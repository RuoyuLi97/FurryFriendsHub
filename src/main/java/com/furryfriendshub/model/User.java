package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    protected String userID;
    private String userName;
    private String email;
    private String password;
    protected String role;
    private String phoneNumber;
    private Date registeredAt;
    private Date lastLoginAt;

    // Constructor to initialize User with required information (only if needed
    // outside of Lombok)
    public User(String userName, String email, String password, String role, String phoneNumber) {
        this.userID = IDGenerator.generateId(IDGenerator.EntityType.USER); // Generate User ID
        this.userName = userName;
        this.email = email;
        this.password = hashPassword(password); // Hash the password
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.registeredAt = new Date(); // Set the registration date to the current date
    }

    // Hash the password using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
