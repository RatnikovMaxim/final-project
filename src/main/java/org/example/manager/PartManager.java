package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.example.exception.InvalidLimitException;
import org.example.exception.NofFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class PartManager {
    public static final int MAX_LIMIT = 100;
    public static final int MIN_LIMIT = 0;
    private NamedParameterJdbcTemplate template;
    private Authenticator authenticator;

    public List<PartGetAllResponseDTO> getAll(int limit, long offset) throws InvalidLimitException {
        if (limit > MAX_LIMIT) {
            throw new InvalidLimitException();
        }
        if (limit <= MIN_LIMIT) {
            throw new InvalidLimitException();
        }

        return template.query(
                // language=PostgreSQL
                """
                        SELECT id, name, description, quantity, image FROM spare_parts
                        WHERE removed = FALSE
                        ORDER BY id
                        limit :limit offset :offset
                        """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(PartGetAllResponseDTO.class)
        );
    }

    public PartGetByIdResponseDTO getById(long id) throws NofFoundException {
        try {

            return template.queryForObject(
                    // language=PostgreSQL
                    """
                            SELECT id, name, description, quantity, image
                            FROM spare_parts
                            WHERE removed = False AND id = :id
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(PartGetByIdResponseDTO.class)
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NofFoundException();
        }
    }


    public PartCreateResponseDTO create(PartCreateRequestDTO requestDTO) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }

        PartCreateResponseDTO responseDTO = template.queryForObject(
                // language=PostgreSQL
                """
                        INSERT INTO spare_parts(owner_id, name, description, quantity, image)
                        VALUES (:owner_id, :name, :description, :quantity, :image)
                        RETURNING id, owner_id, name, 'NEW' AS type, description, quantity, image
                        """,
                Map.of(
                        "owner_id", authentication.getId(),
                        "name", requestDTO.getName(),
                        "description", requestDTO.getDescription(),
                        "quantity", requestDTO.getQuantity(),
                        "image", requestDTO.getImage()
                ),
                BeanPropertyRowMapper.newInstance(PartCreateResponseDTO.class)
        );

        // регистрация в history
        template.update(
                // language=PostgreSQL
                """
                        INSERT INTO history(owner_id, part_id, type, part_name, part_qty)
                        VALUES (:owner_id, :part_id, 'NEW',  :part_name, :part_qty)
                        """,
                Map.of(
                        "owner_id", authentication.getId(),
                        "part_id", responseDTO.getId(),
                        "part_name", responseDTO.getName(),
                        "part_qty", responseDTO.getQuantity()
                )
        );
        return responseDTO;
    }


    public PartUpdateResponseDTO update(PartUpdateRequestDTO requestDTO) throws ForbiddenException, AccountRemovedException, NofFoundException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        try {
            PartUpdateResponseDTO responseDTO = template.queryForObject(
                    // language=PostgreSQL
                    """
                            UPDATE spare_parts
                            SET name = :name, description = :description, quantity = :quantity, image = :image
                            WHERE removed = FALSE AND id = :id
                            RETURNING id, owner_id, name,'New' AS type, description, quantity, image
                            """,
                    Map.of(
                            "id", requestDTO.getId(),
                            "name", requestDTO.getName(),
                            "description", requestDTO.getDescription(),
                            "quantity", requestDTO.getQuantity(),
                            "image", requestDTO.getImage()
                    ),
                    BeanPropertyRowMapper.newInstance(PartUpdateResponseDTO.class)
            );
            template.update(
                    // language=PostgreSQL
                    """
                            INSERT INTO history(owner_id, part_id, type, part_name, part_qty)
                            VALUES (:owner_id, :part_id, 'CHANGED',  :part_name, :part_qty)
                            """,
                    Map.of(
                            "owner_id", authentication.getId(),
                            "part_id", responseDTO.getId(),
                            "part_name", responseDTO.getName(),
                            "part_qty", responseDTO.getQuantity()
                    )
            );
            return responseDTO;
        } catch (EmptyResultDataAccessException e) {
            throw new NofFoundException();
        }
    }


    public PartRemoveByIdResponseDTO removeById(long id) throws ForbiddenException, NofFoundException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }

        try {
            if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {

                return template.queryForObject(
                        // language=PostgreSQL
                        """
                                UPDATE spare_parts SET removed = TRUE
                                WHERE id = :id
                                RETURNING id, name, description, quantity, image
                                """,
                        Map.of("id", id),
                        BeanPropertyRowMapper.newInstance(PartRemoveByIdResponseDTO.class)
                );
            }
            if (authentication.getRole().equals(Authentication.ROLE_STOREKEEPER)) {
                return template.queryForObject(
                        // language=PostgreSQL
                        """
                                UPDATE spare_parts SET removed = TRUE
                                WHERE id = :id
                                RETURNING id, name, description, quantity, image
                                """,
                        Map.of("id", id),
                        BeanPropertyRowMapper.newInstance(PartRemoveByIdResponseDTO.class)
                );
            }
            throw new ForbiddenException();
        } catch (EmptyResultDataAccessException e) {
            throw new NofFoundException();
        }
    }

    public PartRestoreByIdResponseDTO restoreById(long id) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }

        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    """
                            UPDATE spare_parts SET removed = FALSE
                            WHERE id = :id
                            RETURNING id, name, description, quantity, image
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(PartRestoreByIdResponseDTO.class)
            );
        }
        return template.queryForObject(
                // language=PostgreSQL
                """
                        UPDATE spare_parts SET removed = FALSE
                        WHERE id = :id
                        RETURNING id, name, description, quantity, image
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(PartRestoreByIdResponseDTO.class)
        );
    }

    public List<PartGetAllRemovedResponseDTO> getAllRemoved(int limit, long offset) throws ForbiddenException, AccountRemovedException {
        Authentication authentication = authenticator.authenticate();

        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.query(
                    // language=PostgreSQL
                    """
                            SELECT id, name, description, quantity, image FROM spare_parts
                            WHERE removed = TRUE
                            ORDER BY id
                            limit :limit offset :offset
                            """,
                    Map.of(
                            "limit", limit,
                            "offset", offset
                    ),
                    BeanPropertyRowMapper.newInstance(PartGetAllRemovedResponseDTO.class)
            );
        }
        return template.query(
                // language=PostgreSQL
                """
                        SELECT id, name, description, quantity, image FROM spare_parts
                        WHERE removed = TRUE 
                        ORDER BY id
                        limit :limit offset :offset
                        """,
                Map.of(
                        "owner_id", authentication.getId(),
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(PartGetAllRemovedResponseDTO.class)
        );
    }

    public PartWriteOffResponseDTO change(PartWriteOffRequestDTO requestDTO) throws ForbiddenException, InvalidAttributeValueException, AccountRemovedException, NofFoundException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (requestDTO.getQuantity() < 0) {
            throw new InvalidAttributeValueException();
        }
        try {
            PartWriteOffResponseDTO responseDTO = template.queryForObject(
                    // language=PostgreSQL
                    """
                            UPDATE spare_parts
                            SET quantity = :quantity
                            WHERE removed = FALSE AND id = :id
                            RETURNING id, owner_id, name, description, quantity, image
                            """,
                    Map.of(

                            "id", requestDTO.getId(),
                            "quantity", requestDTO.getQuantity()
                    ),
                    BeanPropertyRowMapper.newInstance(PartWriteOffResponseDTO.class)
            );


            template.update(
                    // language=PostgreSQL
                    """
                            INSERT INTO history(owner_id, part_id, type, part_name, part_qty)
                            VALUES (:owner_id, :part_id, 'CHANGED',  :part_name, :part_qty)
                                                                          
                            """,
                    Map.of(
                            "owner_id", authentication.getId(),
                            "part_id", responseDTO.getId(),
                            "part_name", responseDTO.getName(),
                            "part_qty", responseDTO.getQuantity()
                    )
            );
            return responseDTO;
        } catch (EmptyResultDataAccessException e) {
            throw new NofFoundException();
        }
    }
}
