package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.dto.HistoryGetAllResponseDTO;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class HistoryManager {
    private NamedParameterJdbcTemplate template;
    private Authenticator authenticator;


    public List<HistoryGetAllResponseDTO> getAll(int limit, long offset) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }
        return template.query(
                // language=PostgreSQL
                """
                    SELECT id, owner_id, part_id, type, part_name, part_qty FROM history
                    order by id
                """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(HistoryGetAllResponseDTO.class)
        );
    }
}
