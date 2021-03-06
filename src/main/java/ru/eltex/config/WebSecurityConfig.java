package ru.eltex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletRequest;
import javax.sql.DataSource;


/**
 * Класс конфигурации Web security config.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    /**
     * Поле подключения PasswordEncoder, для храниения и сравнения паролей в неявном виде (кодировка BCrypt).
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Создание бина кодировщика пароля (кодировка BCrypt).
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/signup", "/playrooms", "/playrooms/create").permitAll()
//                    .antMatchers("/authorization", "/signup").anonymous()
                .anyRequest().permitAll()
                .mvcMatchers("/playrooms").permitAll()
                .and()
                .formLogin()
                .loginPage("/authorization")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password from user where username = ?");
    }
}