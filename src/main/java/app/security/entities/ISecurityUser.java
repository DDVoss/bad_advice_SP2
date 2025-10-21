package app.security.entities;

import app.security.entities.Role;

public interface ISecurityUser {
    boolean verifyPassword(String pw);
    void addRole(Role role);
}
