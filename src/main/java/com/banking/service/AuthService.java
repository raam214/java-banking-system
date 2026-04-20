package com.banking.service;

import com.banking.dao.UserDAO;
import com.banking.exception.AuthenticationException;
import com.banking.exception.ValidationException;
import com.banking.model.User;
import com.banking.util.InputValidator;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;
    private User loggedInUser;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User register(String username, String password,
                         String fullName, String email) throws SQLException {

        if (!InputValidator.isValidUsername(username))
            throw new ValidationException("Username must be 4-50 chars, letters/numbers/underscore only");

        if (!InputValidator.isValidPassword(password))
            throw new ValidationException("Password must be 8+ chars with at least one digit and one uppercase");

        if (!InputValidator.isValidEmail(email))
            throw new ValidationException("Invalid email format");

        if (userDAO.existsByUsername(username))
            throw new ValidationException("Username already taken");

        User user = new User(username, password, fullName, email);
        return userDAO.save(user);
    }

    public User login(String username, String password)
            throws SQLException, AuthenticationException {

        Optional<User> userOpt = userDAO.findByUsername(username);

        if (userOpt.isEmpty())
            throw new AuthenticationException("Invalid username or password");

        User user = userOpt.get();

        if (!user.getPassword().equals(password))
            throw new AuthenticationException("Invalid username or password");

        this.loggedInUser = user;
        return user;
    }

    public void logout() {
        this.loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }
}