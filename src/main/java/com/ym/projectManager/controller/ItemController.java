package com.ym.projectManager.controller;


import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.ItemSection;
import com.ym.projectManager.model.templateWrap.item.ItemSelectTempl;
import com.ym.projectManager.repository.SectionRepository;
import com.ym.projectManager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Controller
@RequestMapping(value = "/main/items")
public class ItemController {

    public static final String DELETE_ITEM = "/delete/{item_id}";
    public static final String CREATE_OR_UPDATE_ITEM = "/edit";
    public static final String SAVE_ITEM = "/edit";
    private final ItemService itemService;
    private final SectionRepository sectionRepository;
    private final int pageSize = 15;

    enum ItemStatus {
        NONE, NEW, IN_PROGRESS, READY, SOLD;

        public static Stream<ItemStatus> stream() {
            return Stream.of(ItemStatus.values());
        }
    }

    @Autowired
    public ItemController(ItemService itemService, SectionRepository sectionRepository) {
        this.itemService = itemService;
        this.sectionRepository = sectionRepository;

    }

    @GetMapping(value = "")
    public String listPage(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("totalPages") Optional<Integer> totalItems) {
        int currentPage = page.orElse(1);
        Page<Item> itemPage;

        if (totalItems.isEmpty()) {
            itemPage = itemService.getAllItemsPaginated(PageRequest.of(currentPage - 1, pageSize), 0);
            totalItems = Optional.of((int) itemPage.getTotalElements());
        } else {
            itemPage = itemService.getAllItemsPaginated(PageRequest.of(currentPage - 1, pageSize), totalItems.get());
        }
        model.addAttribute("itemPage", itemPage);
        initItemsTempl(model, totalItems, itemPage.getTotalPages(), Optional.empty());
        return "items";
    }

    private void initItemsTempl(Model model, Optional<Integer> totalItems, int totalPages, Optional<ItemSelectTempl> selectTempl) {
        if (totalItems.isPresent()) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalItems", totalItems.get());
        model.addAttribute("status", ItemStatus.values());

        if(selectTempl.isEmpty()){
            selectTempl = Optional.of(new ItemSelectTempl());
        }
        selectTempl.get().setSections(sectionRepository.findAll());

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

        initItemsTempl(model, totalItems,itemPage.getTotalPages(), Optional.ofNullable(selectTempl));

        return "items";
    }

    @GetMapping(value = DELETE_ITEM)
    public String deleteItem(@PathVariable("item_id") Long itemId, Model model) {
        itemService.deleteItem(itemId);
        return "redirect:/main/items";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("item", new Item());
        return "edit";
    }

    @GetMapping(CREATE_OR_UPDATE_ITEM)
    public String createOrUpdateItem(@RequestParam(value = "id", required = false) Optional<Long> optionalItemId, Model model) {

        if (!optionalItemId.isEmpty()) {
            model.addAttribute("item", itemService.getItemById(optionalItemId.get()).get());

        } else {
            model.addAttribute("item", new Item());

        }
        model.addAttribute("status", ItemStatus.values());
        model.addAttribute("sectionSelect", sectionRepository.findAll());
        return "edit";
    }

    @PostMapping(SAVE_ITEM)
    public String saveItem(Item item, Model model) {
        Item saved = itemService.createOrUpdateItem(item, Optional.ofNullable(item.getTempSection()));
        model.addAttribute(saved);
        return "redirect:/main/items";
    }

    @GetMapping("/add_section")
    public String addSection(@PathVariable("section_name") String sectionName, Model model) {
        sectionRepository.saveAndFlush(new ItemSection(sectionName));
        return "edit";
    }

    @GetMapping("/add_section/{section_name}")
    public String saveNewSection(@PathVariable("section_name") String section) {

        ItemSection saved = sectionRepository.saveAndFlush(new ItemSection(section));

        return "edit";
    }


}
