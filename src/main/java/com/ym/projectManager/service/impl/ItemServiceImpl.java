package com.ym.projectManager.service.impl;

import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.ItemSection;
import com.ym.projectManager.repository.ItemRepository;
import com.ym.projectManager.repository.SectionRepository;
import com.ym.projectManager.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final SectionRepository sectionRepository;
    private final int pageSize = 15;

    public ItemServiceImpl(ItemRepository itemRepository, SectionRepository sectionRepository) {
        this.itemRepository = itemRepository;
        this.sectionRepository = sectionRepository;

    }

    @Override
    public Item createOrUpdateItem(Item item, Optional<String> newSection) {
        if (newSection.isPresent()) {
            ItemSection save = sectionRepository.saveAndFlush(new ItemSection(newSection.get()));
            item.setItemSection(save);
        }

        return itemRepository.saveAndFlush(item);
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Item with \"%s\" doesn't exist.", id))));
    }

    @Override
    public void deleteItem(Long id) {
        getItemOrThrowException(id);
        itemRepository.deleteById(id);
    }

    @Override
    public Page<Item> getItemsBySelectedParam(PageRequest pageable, int totalItems, Boolean addSection,
                                              Long selectedSectionId, Boolean addStatus, String selectedStatus) {
        int currentPage = pageable.getPageNumber();
        PageRequest request = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "itemId"));

        if (addSection && addStatus) {
            if (totalItems == 0) {
                totalItems = itemRepository.countItemByItemSection_ItemSectionIdAndStatus(selectedSectionId, selectedStatus);
                if(totalItems == 0)
                    return new PageImpl<Item>(Collections.emptyList(), PageRequest.of(currentPage, pageSize), totalItems);
            }
            final List<Item> items = itemRepository.findItemsByItemSection_ItemSectionIdAndStatus(selectedSectionId, selectedStatus, request).get();
            return new PageImpl<Item>(items, PageRequest.of(currentPage, pageSize), totalItems);
        }
        if (addSection) {
            if (totalItems == 0) {
                totalItems = itemRepository.countItemByItemSection_ItemSectionId(selectedSectionId);
            }
            final List<Item> items = itemRepository.findItemsByItemSection_ItemSectionId(selectedSectionId, request);
            return new PageImpl<Item>(items, PageRequest.of(currentPage, pageSize), totalItems);
        }
        if (addStatus) {
            if (totalItems == 0) {
                totalItems = itemRepository.countItemByStatus(selectedStatus);
            }
            final List<Item> items = itemRepository.findItemsByStatus(selectedStatus, request);
            return new PageImpl<Item>(items, PageRequest.of(currentPage, pageSize), totalItems);
        } else return getAllItems(totalItems, pageable.getPageNumber());
    }

    @Override
    public Page<Item> getAllItemsPaginated(PageRequest pageable, int totalItems) {
        return getAllItems(totalItems, pageable.getPageNumber());
    }

    private Page<Item> getAllItems(int totalItems, int currentPage) {
        if (totalItems == 0) {
            totalItems = (int) itemRepository.count();
        }
        PageRequest request = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "itemId"));
        final Page <Item> items = itemRepository.findAll(request);
        if (items == null) {
            return null;
        } else {
            return new PageImpl<Item>(items.toList(), PageRequest.of(currentPage, pageSize), totalItems);
        }
    }

    @Override
    public List<Item> getItemsBySection(Long sectionId) {
        return itemRepository.findItemsByItemSection_ItemSectionId(sectionId);
    }

    @Override
    public List<Item> getItemsByName(String name) {
        return itemRepository.getItemsByNameContainingIgnoreCase(name);
    }

    private void getItemOrThrowException(Long id) {
        itemRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Item with \"%s\" doesn't exist.", id)));
    }


}
