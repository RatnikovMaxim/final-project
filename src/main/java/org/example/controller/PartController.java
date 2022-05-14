package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.example.exception.InvalidLimitException;
import org.example.exception.NofFoundException;
import org.example.manager.PartManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InvalidAttributeValueException;
import java.util.List;

@RestController
@AllArgsConstructor
public class PartController {
    private PartManager manager;

    @RequestMapping("/parts.getAll")
    public List<PartGetAllResponseDTO> getAll(int limit, long offset) throws InvalidLimitException {
        return manager.getAll(limit, offset);
    }

    @RequestMapping("/parts.getById")
    public PartGetByIdResponseDTO getBtId(long id) throws NofFoundException {
        return manager.getById(id);
    }

    @RequestMapping("/parts.create")
    public PartCreateResponseDTO create(PartCreateRequestDTO requestDTO) throws ForbiddenException, AccountRemovedException {
        return manager.create(requestDTO);
    }

    @RequestMapping("/parts.update")
    public PartUpdateResponseDTO update(PartUpdateRequestDTO requestDTO) throws ForbiddenException, AccountRemovedException, NofFoundException {
        return manager.update(requestDTO);
    }

    @RequestMapping("/parts.removeById")
    public PartRemoveByIdResponseDTO removeById(long id) throws ForbiddenException, NofFoundException, AccountRemovedException {
        return manager.removeById(id);
    }

    @RequestMapping("/parts.restoreById")
    public PartRestoreByIdResponseDTO restoreById(long id) throws ForbiddenException, AccountRemovedException {
        return manager.restoreById(id);
    }

    @RequestMapping("/parts.getAllRemoved")
    public List<PartGetAllRemovedResponseDTO> getAllRemoved(int limit, long offset) throws ForbiddenException, AccountRemovedException {
        return manager.getAllRemoved(limit, offset);
    }

    @RequestMapping("/parts.change")
    public PartWriteOffResponseDTO change(PartWriteOffRequestDTO requestDTO) throws ForbiddenException, InvalidAttributeValueException, AccountRemovedException, NofFoundException {
        return manager.change(requestDTO);
    }
}
