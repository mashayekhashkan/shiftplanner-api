package com.shiftplanner.shiftplanner_api.repository;

import com.shiftplanner.shiftplanner_api.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findByNameIgnoreCase (String name);
}
