package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.MyUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final MyUserService myUserService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, MyUserService myUserService) {
        this.successUserHandler = successUserHandler;
        this.myUserService = myUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключение CSRF (для тестирования)
                .authorizeRequests()
                .antMatchers("/", "/home", "/login").permitAll() // Разрешить всем
                .antMatchers("/admin/**").hasRole("ADMIN") // Только для ADMIN
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Для USER и ADMIN
                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                .and()
                .formLogin()
                .loginPage("/login") // Страница входа
                .successHandler(successUserHandler) // Обработчик успешного входа
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // URL для выхода
                .logoutSuccessUrl("/login?logout") // Перенаправление после выхода
                .invalidateHttpSession(true) // Уничтожение сессии
                .deleteCookies("JSESSIONID") // Удаление cookies
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**"); // Разрешить статические ресурсы
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()); // Использование DaoAuthenticationProvider
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(myUserService);
        return authProvider;
    }
}