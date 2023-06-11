package org.greenway.backend.controller;

import jakarta.validation.Valid;
import org.greenway.backend.model.Category;
import org.greenway.backend.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/order/")
public class OrderController {

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Collection<?> order){
        System.out.println(order);
        return null;
    }
}
