package com.sopromadze.blogapi.configurationSecurity;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class SpringSecurityTestConfig {



    //@Bean("customUserDetailsServiceImpl")
    @Bean("customUserDetailsServiceImpl")
    @Primary
    public UserDetailsService userDetailsService() {

        List<Role> rolesAdmin = new ArrayList<Role>();
        rolesAdmin.add(new Role(RoleName.ROLE_ADMIN));
        rolesAdmin.add(new Role(RoleName.ROLE_USER));
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        User admin = new User ();
                admin.setUsername("user");
                admin.setPassword("user");
                admin.setRoles(rolesAdmin);
        UserPrincipal AdminP= UserPrincipal.create(admin);

        User user = new User();
                user.setUsername("user");
                user.setPassword("user");
        user.setRoles(rolesUser);

        UserPrincipal UserP= UserPrincipal.create(user);

        List<UserPrincipal> usuariosPrincipales = new ArrayList<UserPrincipal>();

        usuariosPrincipales.add(AdminP);
        usuariosPrincipales.add(UserP);


        return new InMemoryUserDetailsManager((UserDetails) usuariosPrincipales);

    }
}
