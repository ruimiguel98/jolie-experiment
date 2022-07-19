package org.example.controller;

import org.example.entity.Address;
import org.example.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/address")
    public Address addressRetrieve(@RequestParam(name="id", required = true) Long addressId) {
        return addressService.fetchAddressById(addressId);
    }
}
