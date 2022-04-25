package org.darccona.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Класс для настройки доступа пользователю страниц
 */
@Configuration
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()
                .authorizeRequests()
                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/blog/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/blog/newPassword/**").permitAll()
                .antMatchers("/blog/userRecord/**").permitAll()
                .anyRequest().authenticated()

            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/blog?new=1")
                .permitAll()

            .and()
                .logout()
                .permitAll();
    }
}