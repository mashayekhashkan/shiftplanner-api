package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.Store;
import com.shiftplanner.shiftplanner_api.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StoreService {

    private final StoreRepository repository;

    public StoreService(StoreRepository repository) {
        this.repository = repository;
    }

    public Store getStoreById (UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Store not found: " + id));
    }

    public List<Store> getStoreByName (String name) {
        return repository.findByNameIgnoreCase(name);
    }
}
