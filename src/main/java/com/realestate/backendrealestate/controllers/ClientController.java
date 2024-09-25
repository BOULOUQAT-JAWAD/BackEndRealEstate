package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.responses.ClientResponseDto;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.services.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/client")
@AllArgsConstructor
public class ClientController {

    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PutMapping("/changestatus/{clientId}")
    public ResponseEntity<DefaultResponseDto> changeClientActivation(@PathVariable Long clientId){
        return ResponseEntity.ok(clientService.changeClientActivation(clientId));
    }


}
