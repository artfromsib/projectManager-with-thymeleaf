package com.ym.projectManager.service;

import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.ItemSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ItemService {


    Item createOrUpdateItem(Item item, Optional<String> newSection);


    List<Item> getAllItems();


    Optional<Item> getItemById(Long id);

    List<Item> getItemsBySection(Long sectionId);

    List<Item> getItemsByName(String name);

    void deleteItem(Long id);

    Page<Item> getItemsBySelectedParam(PageRequest pageable, int totalItems, Boolean addSection,
                                       Long selectedSectionId, Boolean addStatus, String selectedStatus);

    Page<Item> getAllItemsPaginated(PageRequest of, int totalPages);
}
