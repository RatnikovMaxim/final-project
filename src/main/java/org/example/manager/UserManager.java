package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.example.exception.LoginAlreadyExistsException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserManager {
    private NamedParameterJdbcTemplate template;
    private Authenticator authenticator;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserGetAllResponseDTO> getAll(int limit, long offset) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.query(
                    // language=PostgreSQL
                    """
                            SELECT id, login, role FROM users
                            WHERE removed = FALSE
                            ORDER BY id
                            limit :limit offset :offset
                            """,
                    Map.of(
                            "limit", limit,
                            "offset", offset
                    ),
                    BeanPropertyRowMapper.newInstance(UserGetAllResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public UserGetByIdResponseDTO getById(long id) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    """
                            SELECT id, login, role FROM users
                            WHERE removed = FALSE AND id = :id
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(UserGetByIdResponseDTO.class)
            );
        }
        if (authentication.getId() == id) {
            return template.queryForObject(
                    // language=PostgreSQL
                    """
                            SELECT id, login, role FROM users
                            WHERE removed = FALSE AND  login = :login AND id = :id 
                            """,
                    Map.of(
                            "login", authentication.getLogin(),
                            "id", id),
                    BeanPropertyRowMapper.newInstance(UserGetByIdResponseDTO.class)
            );

        }
        throw new ForbiddenException();
    }




        public UserRegisterResponseDTO register(UserRegisterRequestDTO requestDTO) throws ForbiddenException, LoginAlreadyExistsException, AccountRemovedException {
        String encodePassword = passwordEncoder.encode(requestDTO.getPassword());
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            try {
                return template.queryForObject(
                        // language=PostgreSQL
                        """
                            INSERT INTO users(login, password, role)
                            VALUES (:login, :password, :role)
                            ON CONFLICT (login) DO NOTHING
                            RETURNING id, login, role      
                            """,
                    Map.of(
                            "login", requestDTO.getLogin(),
                            "password", encodePassword,
                            "role", Authentication.ROLE_STOREKEEPER
                    ),
                    BeanPropertyRowMapper.newInstance(UserRegisterResponseDTO.class)
            );
            } catch (EmptyResultDataAccessException e) {
                throw new LoginAlreadyExistsException();
            }
        }
        throw new ForbiddenException();

    }

    public UserMeResponseDTO me() throws AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        return new UserMeResponseDTO(
                authentication.getId(),
                authentication.getLogin(),
                authentication.getRole()
        );
    }

    public UserRemoveByIdResponseDTO removeById(long id) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    """
                            UPDATE users SET removed = TRUE WHERE id = :id
                            RETURNING id, login, role
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(UserRemoveByIdResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public UserRestoreByIdResponseDTO restoreById(long id) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }
        return template.queryForObject(
                // language=PostgreSQL
                """
                        UPDATE users SET removed = FALSE WHERE id = :id
                        RETURNING id, login, role
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(UserRestoreByIdResponseDTO.class)
        );

    }

}
