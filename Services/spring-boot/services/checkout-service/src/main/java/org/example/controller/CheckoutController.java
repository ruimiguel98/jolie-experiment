package org.example.controller;

import org.example.OrderRequestDTO;
import org.example.OrderResponseDTO;
import org.example.entity.PurchaseOrder;
import org.example.service.CheckoutService;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class CheckoutController {

    @Autowired
    private CheckoutService service;

    @PostMapping("/create")
    public PurchaseOrder createOrder(@RequestBody OrderRequestDTO requestDTO){
        requestDTO.setOrderId(UUID.randomUUID());
        return this.service.createOrder(requestDTO);
    }

    @GetMapping("/all")
    public List<OrderResponseDTO> getOrders(){
        return this.service.getAll();
    }

}
