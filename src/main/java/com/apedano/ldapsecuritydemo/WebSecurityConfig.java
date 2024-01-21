package com.apedano.ldapsecuritydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.server.UnboundIdContainer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
//                        the role of the user in *,ou=groups,dc=springframework,dc=org
                                .requestMatchers("/**").hasRole("DEVELOPERS")
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }




//    @Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
////                .contextSource().managerDn("cn=admin,dc=my-org,dc=local")
////                .managerPassword("password")
////                .and()
//                .userDnPatterns("uid={0},ou=People")
//                .groupSearchBase("ou=Groups")
//                .contextSource()
//                .url("ldap://localhost:389/dc=my-org,dc=local")
//                .and()
//                .passwordCompare()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .passwordAttribute("userpassword");
//    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // bind authentication :
        // obtain the DN for the user by substituting the user login name
        // in the supplied pattern and attempting to bind as that user with the login password
        /* simple bind version */
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
//                .groupSearchFilter("uniqueMember={0}")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org");
        /* LDAP search filter */
//        auth
//                .ldapAuthentication()
//                .userSearchBase("ou=people")
//                .userSearchFilter("(uid={0})")
//                .groupSearchBase("cn=developers,ou=groups")
//                .contextSource()
//                .url("ldap://localhost:389/dc=springframework,dc=org");

    }

}
