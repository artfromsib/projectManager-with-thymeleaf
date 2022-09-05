package com.ym.projectManager.controller;


import com.ym.projectManager.enums.ItemStatus;
import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.ItemSection;
import com.ym.projectManager.model.templateWrap.item.ItemSelectTempl;
import com.ym.projectManager.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/main/items")
public class ItemController {

    private final ItemService itemService;
    private final List<ItemSection> itemSections;
    private final int pageSize = 15;

    @GetMapping(value = "")
    public String listPage(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("totalPages") Optional<Integer> totalPages) {

        Optional<Integer> amountPages = totalPages;
        int currentPage = page.orElse(1);
        Page<Item> itemPage;

        if (amountPages.isEmpty()) {
            itemPage = itemService.getAllItemsPaginated(PageRequest.of(currentPage - 1, pageSize), 0);
            amountPages = Optional.of((int) itemPage.getTotalElements());
        } else {
            itemPage = itemService.getAllItemsPaginated(PageRequest.of(currentPage - 1, pageSize), amountPages.get());
        }
        model.addAttribute("itemPage", itemPage);
        initItemsTempl(model, amountPages, itemPage.getTotalPages(), Optional.empty());
        return "items";
    }

    private void initItemsTempl(Model model, Optional<Integer> totalItems, int totalPages, Optional<ItemSelectTempl> selectTempl) {
        if (totalItems.isPresent()) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalItems", totalItems.get());
        model.addAttribute("status", ItemStatus.values());

        if (selectTempl.isEmpty()) {
            selectTempl = Optional.of(new ItemSelectTempl());
        }
        selectTempl.get().setSections(itemSections);

        model.addAttribute("itemSelectTempl", selectTempl.get());

    }

    @PostMapping("")
    public String listItemBySection(@RequestParam("page") Optional<Integer> page, @RequestParam("totalPages") Optional<Integer> totalItems,
                                    @ModelAttribute("itemSelectTempl") @RequestBody ItemSelectTempl selectTempl, Model model) {
        int currentPage = page.orElse(1);
        PageRequest request = PageRequest.of(page.orElse(1) - 1, pageSize);
        Page<Item> itemPage;

        if (totalItems.isEmpty()) {

            itemPage = itemService.getItemsBySelectedParam(request, 0, selectTempl.getAddSection(),
                    selectTempl.getSelectedSection().getItemSectionId(), selectTempl.getAddStatus(), selectTempl.getSelectedStatus());

            totalItems = Optional.of((int) itemPage.getTotalElements());
        } else {
            itemPage = itemService.getAllItemsPaginated(PageRequest.of(currentPage - 1, pageSize), totalItems.get());
        }

        model.addAttribute("itemPage", itemPage);
        initItemsTempl(model, totalItems, itemPage.getTotalPages(), Optional.ofNullable(selectTempl));

        return "items";
    }

    @GetMapping(value = "/delete/{item_id}")
    public String deleteItem(@PathVariable("item_id") Long itemId, Model model) {
        itemService.deleteItem(itemId);
        return "redirect:/main/items";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("item", new Item());
        return "item";
    }

    @GetMapping("/edit")
    public String createOrUpdateItem(@RequestParam(value = "id", required = false) Optional<Long> optionalItemId, Model model) {

        if (optionalItemId.isPresent()) {
            model.addAttribute("item", itemService.getItemById(optionalItemId.get()).get());
        } else {
            model.addAttribute("item", new Item());
        }

        model.addAttribute("status", ItemStatus.values());
        model.addAttribute("sectionSelect", itemSections);
        return "item";
    }

    @PostMapping("/edit")
    public String saveItem(Item item, Model model) {
        Item saved = itemService.createOrUpdateItem(item, Optional.ofNullable(item.getTempSection()));
        model.addAttribute(saved);
        return "redirect:/main/items";
    }

    @GetMapping("/add_section")
    public String addSection(@PathVariable("section_name") String sectionName, Model model) {
        itemService.saveItemSection(new ItemSection(sectionName));
        return "item";
    }

}
