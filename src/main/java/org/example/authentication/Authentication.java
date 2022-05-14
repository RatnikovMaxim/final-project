package org.example.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Authentication {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STOREKEEPER = "STOREKEEPER";
    public static final String ROLE_ANONYMOUS = "ANONYMOUS";
    public static final long ID_ANONYMOUS = -1;
    private long id;
    private String login;
    private String password;
    private String role;
    private boolean removed;

    public boolean isAnonymous() {
        return id == ID_ANONYMOUS;
    }
}
