package com.ym.projectManager.repository;

import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.ItemSection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    int countItemByItemSection_ItemSectionIdAndStatus(Long sectionId, String status);
    int countItemByItemSection_ItemSectionId(Long sectionId);
    int countItemByStatus(String status);

    List<Item> findItemsByItemSection_ItemSectionId(Long sectionId);
    List<Item> findItemsByItemSection_ItemSectionId(Long sectionId, Pageable pageable);
    List<Item> getItemsByNameContainingIgnoreCase(String name);
    List<Item> findItemsByItemSectionAndStatus(Long sectionId, String status);
    Optional<List<Item>> findItemsByItemSection_ItemSectionIdAndStatus(Long sectionId, String status, Pageable pageable);
    List<Item> findItemsByStatus(String selectedStatus);
    List<Item> findItemsByStatus(String selectedStatus, Pageable pageable);
    List<Item> countAllBy();



}
