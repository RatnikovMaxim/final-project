package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.example.exception.LoginAlreadyExistsException;
import org.example.manager.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserManager manager;

    @RequestMapping("/users.getAll")
    public List<UserGetAllResponseDTO> getAll(int limit, long offset) throws ForbiddenException, AccountRemovedException {
        return manager.getAll(limit, offset);
    }

    @RequestMapping("/users.getById")
    public UserGetByIdResponseDTO getById(long id) throws ForbiddenException, AccountRemovedException {
        return manager.getById(id);
    }

    @RequestMapping("/users.register")
    public UserRegisterResponseDTO register(UserRegisterRequestDTO requestDTO) throws ForbiddenException, LoginAlreadyExistsException, AccountRemovedException {
        return manager.register(requestDTO);
    }

    @RequestMapping("/users.me")
    public UserMeResponseDTO me() throws AccountRemovedException {
        return manager.me();
    }

    @RequestMapping("/users.removeById")
    public UserRemoveByIdResponseDTO removeById(long id) throws ForbiddenException, AccountRemovedException {
        return manager.removeById(id);
    }

    @RequestMapping("/users.restoreById")
    public UserRestoreByIdResponseDTO restoreById(long id) throws ForbiddenException, AccountRemovedException {
        return manager.restoreById(id);
    }
}
