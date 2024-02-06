package tomastk.shelty.services.impl;


import org.springframework.security.core.context.SecurityContextHolder;
import tomastk.shelty.user.Role;
import tomastk.shelty.user.User;

public class SecurityContextHandler {
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static Role getUserRole() {
        return getUser().getRole();
    }
    public static long getUserId() {
        return getUser().getId();
    }

}
