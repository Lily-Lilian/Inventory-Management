package com.stockmanagement.stockmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockmanagement.stockmanagement.dto.AddItemRequest;
import com.stockmanagement.stockmanagement.dto.SellItemRequest;
import com.stockmanagement.stockmanagement.models.Item;
import com.stockmanagement.stockmanagement.services.StockService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Get all items
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(stockService.getAllItems());
    }

    // Add an item
    @PostMapping("/addItems")
    public ResponseEntity<Map<String, Object>> addItem(@Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.ok(stockService.addItem(request.getName(), request.getQuantity()));
    }

    // Sell an item
    @PostMapping("/sellItem")
    public ResponseEntity<Map<String, Object>> sellItem(@Valid @RequestBody SellItemRequest request) {
        return ResponseEntity.ok(stockService.sellItem(request.getName(), request.getQuantityToSell()));
    }

    // Get total items sold
    @GetMapping("/soldItems")
    public ResponseEntity<Map<String, Object>> getTotalItemsSold() {
        return ResponseEntity.ok(stockService.getTotalItemsSold());
    }

    // Get low stock alerts
    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockAlerts() {
        return ResponseEntity.ok(stockService.getLowStockAlerts());
    }
}
