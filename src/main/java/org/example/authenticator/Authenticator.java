package org.example.authenticator;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.exception.AccountRemovedException;
import org.example.exception.NotAuthenticatedException;
import org.example.exception.PasswordNotMatchesException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class Authenticator {
    private HttpServletRequest request;
    private JdbcTemplate template;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Authentication authenticate() throws AccountRemovedException {
        String login = request.getHeader("X-Login");
        if (login == null) {
            return anonymous();
        }
        String password = request.getHeader("X-Password");
        if (password == null) {
            throw new NotAuthenticatedException();
        }


        Authentication authentication = template.queryForObject(
                //language=PostgreSQL
                """
                        SELECT id, login, password, role, removed From users
                        where login = ?
                        """,
                BeanPropertyRowMapper.newInstance(Authentication.class),
                login
        );

        if (!passwordEncoder.matches(password, authentication.getPassword())) {
            throw new PasswordNotMatchesException();
        }

        if (authentication.isRemoved()) {
            throw new AccountRemovedException();
        }

        authentication.setPassword("***");
        return authentication;
    }

    private Authentication anonymous() {
        return new Authentication(
                Authentication.ID_ANONYMOUS,
                "anonymous",
                "***",
                Authentication.ROLE_ANONYMOUS,
                false
        );
    }

}