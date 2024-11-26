package fr.iutrodez.salespath.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/client")
public class ClientController
{
    @GetMapping(value="/getAll/")
    public void getAllClient(){
        //TODO ***
    }

    @PostMapping(value="/addClient/")
    public void addNewClient() {
        //TODO ***
    }

    @PutMapping(value = "editClient")
    public void editClient() {
        //TODO ***
    }

}
