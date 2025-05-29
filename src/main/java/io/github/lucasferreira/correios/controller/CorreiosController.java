package io.github.lucasferreira.correios.controller;

import io.github.lucasferreira.correios.model.Address;
import io.github.lucasferreira.correios.service.CorreiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorreiosController {

    @Autowired
    private CorreiosService service;


    @GetMapping("status")
    public String getStatus() {
        return "Service status: " + this.service.getStatus();
    }

    @GetMapping("zipcode/{zipcode}")
    public Address getAdressByZipcod(@PathVariable("zipcode") String zipcode) {
        return this.service.getAddressByZipcode(zipcode);
    }
}
