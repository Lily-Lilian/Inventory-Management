package com.stockmanagement.stockmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockmanagement.stockmanagement.models.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByName(String name);

    @Query("SELECT SUM(i.totalQuantity - i.quantity) FROM Item i")
    int getTotalItemsSold();

    @Query("SELECT i FROM Item i WHERE i.quantity <= (i.totalQuantity * 0.25)")
    List<Item> findLowStockItems();
}

