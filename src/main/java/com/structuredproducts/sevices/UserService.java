package com.structuredproducts.sevices;

import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService implements UserDetailsService{

    private PasswordEncoder passwordEncoder;

    private User user;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if(user == null) {
            user = new User();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (!user.getUsername().equalsIgnoreCase(name)) {
            throw new UsernameNotFoundException("The user with name " + name + " was not found");
        }

        return user;
    }

    private static class User implements UserDetails {

        private String name = "admin";
        private String password = "saratov2016!";
        private List<String> roles = Lists.newArrayList("ROLE_admin", "ROLE_user");

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {

            List<String> roles = this.roles;

            if (roles == null) {
                return Collections.emptyList();
            }

            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            return authorities;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String getUsername() {
            return "admin";
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
