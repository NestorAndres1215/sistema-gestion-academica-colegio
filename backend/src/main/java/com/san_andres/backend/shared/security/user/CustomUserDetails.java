package com.san_andres.backend.shared.security.user;

import com.san_andres.backend.users.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails  implements UserDetails {

    private  final User user;

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toUpperCase()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @NonNull
    public String getUsername() {
        return user.getUsername() != null
                ? user.getUsername()
                : user.getEmail();
    }


    @Override
    public boolean isEnabled() {
        return user.getStatus() != null && user.getStatus().equals("ACTIVE");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getStatus().equals("BLOCKED");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public User getUser() {
        return this.user;
    }

}
