package com.banking.model;

import java.time.LocalDateTime;

public class User {

    public enum Role { CUSTOMER, ADMIN }

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email    = email;
        this.role     = Role.CUSTOMER;
        this.active   = true;
    }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getUsername()                { return username; }
    public void setUsername(String username)   { this.username = username; }

    public String getPassword()                { return password; }
    public void setPassword(String password)   { this.password = password; }

    public String getFullName()                { return fullName; }
    public void setFullName(String fullName)   { this.fullName = fullName; }

    public String getEmail()                   { return email; }
    public void setEmail(String email)         { this.email = email; }

    public Role getRole()                      { return role; }
    public void setRole(Role role)             { this.role = role; }

    public boolean isActive()                  { return active; }
    public void setActive(boolean active)      { this.active = active; }

    public LocalDateTime getCreatedAt()              { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt()              { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', name='%s', role=%s}",
                id, username, fullName, role);
    }
}