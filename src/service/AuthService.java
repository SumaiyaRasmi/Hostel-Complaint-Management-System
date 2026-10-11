package service;

import dao.UserDAO;
import model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private static User loggedInUser = null;

    public User login(String username, String password) {
        String hashedPassword = md5(password);
        User user = userDAO.loginUser(username, hashedPassword);
        if (user != null) {
            loggedInUser = user;
        }
        return user;
    }

    public boolean register(String fullName, String email, String username,
                            String password, String roomNumber, String phone) {
        if (userDAO.usernameExists(username)) return false;
        if (userDAO.emailExists(email)) return false;

        User user = new User(fullName, email, username, md5(password),
                             "STUDENT", roomNumber, phone);
        return userDAO.registerUser(user);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}