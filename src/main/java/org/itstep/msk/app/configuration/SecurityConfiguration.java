package org.itstep.msk.app.configuration;

import org.itstep.msk.app.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    // Настройка БД для security
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Запрос пользователя для аутентификации
        String usersQuery = "SELECT email, password, 1 as active FROM users WHERE email = ?";
        // Запрос ролей пользователя для авторизации
        String authoritiesQuery =
                "SELECT u.email, ur.role "
                + "FROM users u "
                + "INNER JOIN user_roles ur ON ur.user_id = u.id "
                + "WHERE u.email = ?";

        // Настраиваем аутентификацию и авторизацию через БД (JDBC)
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(authoritiesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    // Настройка урлов
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/conversions").authenticated()
                .antMatchers("/admin/**").hasAnyAuthority(Role.ROLE_ADMIN.name())
                .anyRequest().permitAll();

        http.csrf().disable();

        // Настройка формы логина
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password");

        // Настройка разлогина
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");

        // Настройка обработки ошибок доступа
        http.exceptionHandling()
                .accessDeniedPage("/error");
    }

    // Настройка урлов, которые модуль security игнорирует
    // Обычно нужно для урлов со статическими файлами
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Игнорируем следующие урлы
        // То есть по таким урлам не будет проверок доступа
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/images/**");
    }
}
