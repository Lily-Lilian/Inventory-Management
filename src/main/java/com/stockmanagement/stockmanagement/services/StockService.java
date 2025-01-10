package com.stockmanagement.stockmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmanagement.stockmanagement.models.Item;
import com.stockmanagement.stockmanagement.repositories.ItemRepository;

import java.util.*;

@Service
public class StockService {

    private final ItemRepository itemRepository;

    @Autowired
    public StockService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Get all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Add a new item
    public Map<String, Object> addItem(String name, int quantity) {
        Map<String, Object> response = new HashMap<>();

        // Check if item already exists
        List<Item> items = itemRepository.findByName(name);
        if (!items.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Item already exists");
            return response;
        }

        // Add the new item
        Item item = new Item();
        item.setName(name);
        item.setQuantity(quantity);
        item.setTotalQuantity(quantity);
        itemRepository.save(item);

        response.put("status", "success");
        response.put("message", "Item added successfully");
        return response;
    }


    public Map<String, Object> sellItem(String name, int quantityToSell) {
        Map<String, Object> response = new HashMap<>();
    
        // Fetch all items with the given name
        List<Item> items = itemRepository.findByName(name);
    
        if (items == null || items.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Item not found");
            return response;
        }
    
        // Calculate the total stock for all items with the same name
        int totalStock = items.stream().mapToInt(Item::getQuantity).sum();
    
        if (totalStock < quantityToSell) {
            response.put("status", "error");
            response.put("message", "Insufficient stock");
            return response;
        }
    
        // Deduct the quantityToSell from the items in order
        int remainingQuantityToSell = quantityToSell;
    
        for (Item item : items) {
            int currentStock = item.getQuantity();
    
            if (currentStock <= remainingQuantityToSell) {
                // Fully consume this item's stock
                item.setQuantity(0);
                remainingQuantityToSell -= currentStock;
            } else {
                // Partially consume this item's stock
                item.setQuantity(currentStock - remainingQuantityToSell);
                remainingQuantityToSell = 0;
            }
    
            itemRepository.save(item);
    
            // Break the loop if all quantity has been sold
            if (remainingQuantityToSell == 0) {
                break;
            }
        }
    
        response.put("status", "success");
        response.put("message", "Item sold successfully");
    
        // Add low-stock notifications for each item
        List<String> notifications = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() <= item.getTotalQuantity() / 4) {
                notifications.add(item.getName() + " quantity less than a quarter");
            }
            if (item.getQuantity() <= item.getTotalQuantity() / 20) {
                notifications.add(item.getName() + " quantity nearing zero");
            }
        }
    
        if (!notifications.isEmpty()) {
            response.put("notifications", notifications);
        }
    
        return response;
    }
    

    // Get total items sold
    public Map<String, Object> getTotalItemsSold() {
        int totalSold = itemRepository.getTotalItemsSold();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("total_sold", totalSold);
        return response;
    }

    // Get low stock alerts
    public List<Map<String, Object>> getLowStockAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        List<Item> items = itemRepository.findLowStockItems();

        for (Item item : items) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("name", item.getName());
            alert.put("quantity", item.getQuantity());
            alerts.add(alert);
        }

        return alerts;
    }
}

