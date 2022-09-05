package com.ym.projectManager.controller;


import com.ym.projectManager.model.templateWrap.order.OrderTempl;
import com.ym.projectManager.model.templateWrap.order.SetOrderStatusModal;
import com.ym.projectManager.repository.CustomerRepository;
import com.ym.projectManager.repository.ParcelRepository;
import com.ym.projectManager.repository.SectionRepository;
import com.ym.projectManager.service.ItemService;
import com.ym.projectManager.service.OrderService;
import com.ym.projectManager.service.TrackerService;
import com.ym.projectManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/main")
public class OrderController {


    private final OrderService orderService;
    private final ItemService itemService;
    private final CustomerRepository customerRepository;
    private final ParcelRepository parcelRepository;
    private final List<ItemSection> sections;
    private final TrackerService trackerService;

    enum OrderStatus {
        NEW,
        IN_PROGRESS,
        COMPLETE;

        public static Stream<ItemController.ItemStatus> stream() {
            return Stream.of(ItemController.ItemStatus.values());
        }
    }

    @Autowired
    public OrderController(OrderService orderService, ItemService itemService, ParcelRepository parcelRepository, SectionRepository sectionRepository,
                           CustomerRepository customerRepository, TrackerService trackerService) {
        this.orderService = orderService;
        this.itemService = itemService;
        this.parcelRepository = parcelRepository;
        this.customerRepository = customerRepository;
        this.sections = sectionRepository.findAll();
        this.trackerService = trackerService;
    }

    @GetMapping(value = "/new_orders_tab")
    public String listNewOrder(Model model) {
        final List<Order> orders = orderService.getOrdersByStatus(OrderStatus.NEW.toString());
        initModelTabs(model, orders);
        return "main/new_orders_tab";
    }

    @GetMapping(value = "/in_progress_orders_tab")
    public String listInProgressOrder(Model model) {
        final List<Order> orders = orderService.getOrdersByStatus(OrderStatus.IN_PROGRESS.toString());
        initModelTabs(model, orders);
        return "main/in_progress_orders_tab";
    }

    @GetMapping(value = "/complete_orders_tab")
    public String listCompleteOrder(Model model) {
        final List<Order> orders = orderService.getOrdersByStatus(OrderStatus.COMPLETE.toString());
        initModelTabs(model, orders);
        return "main/complete_orders_tab";
    }

    private void initModelTabs(Model model, List<Order> orders) {
        model.addAttribute("orders", orders);
        model.addAttribute("modal_status", new SetOrderStatusModal());
        model.addAttribute("status", OrderStatus.values());
    }


    @GetMapping("/order")
    public String newPreOrder(@RequestParam(value = "id", required = false) Optional<Long> optionalOrderId, Model model) {
        OrderTempl orderTempl;
        if (optionalOrderId.isPresent()) {
            Optional<Order> order = orderService.getOrderById(optionalOrderId.get());
            orderTempl = new OrderTempl(order.get(), order.get().getItems(), new Item(), order.get().getCustomer());
        } else {
            orderTempl = new OrderTempl(new Order(), new ArrayList<>(), new Item(), new Customer());
        }
        initModelOrder(model, orderTempl);
        return "order";
    }

    @PostMapping(value = "/order", params = {"removeItem"})
    public String removeItem(OrderTempl orderTempl, BindingResult bindingResult, HttpServletRequest request, Model model) {
        orderTempl.getItemsOfOrder().remove(Integer.parseInt(request.getParameter("removeItem")));
        initModelOrder(model, orderTempl);
        return "order";
    }

    @PostMapping(value = "/order", params = {"findItems"})
    public String findItems(OrderTempl orderTempl, BindingResult bindingResult, Model model) {
        orderTempl.setItemsFromDBService(itemService.getItemsByName(orderTempl.getFindByItemName()));
        if (orderTempl.getItemsFromDB().isEmpty()) {
            orderTempl.setSearchAswerItem("Not found");
        }
        initModelOrder(model, orderTempl);
        return "order";
    }

    @PostMapping(value = "/order", params = {"addItemsFromDB"})
    public String addItemsFromDB(OrderTempl orderTempl, BindingResult bindingResult, Model model) {
        List<Item> items = new ArrayList<>();
        if (orderTempl.getItemsOfOrder() != null) {
            items.addAll(orderTempl.getItemsOfOrder());
        }
        items.addAll(orderTempl.getSelectedItemsFromDB());
        orderTempl.setItemsOfOrder(items);
        initModelOrder(model, new OrderTempl(orderTempl.getOrder(), orderTempl.getItemsOfOrder(), new Item(), orderTempl.getCustomer()));
        return "order";
    }

    @PostMapping(value = "/order", params = {"addNewItem"})
    public String newItemInPreOrder(OrderTempl orderTempl, Model model) {
        List<Item> items = new ArrayList<>();
        if (orderTempl.getItemsOfOrder() == null) {
            items.add(orderTempl.getNewItem());
        } else {
            items.addAll(orderTempl.getItemsOfOrder());
            items.add(orderTempl.getNewItem());
        }
        orderTempl.setItemsOfOrder(items);
        initModelOrder(model, new OrderTempl(orderTempl.getOrder(), orderTempl.getItemsOfOrder(), new Item(), orderTempl.getCustomer()));
        return "order";
    }

    @PostMapping(value = "/order", params = {"save"})
    public String saveNewOrder(@Valid OrderTempl orderTempl, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            initModelOrder(model,orderTempl);
            model.addAttribute("err",bindingResult.getFieldError().getDefaultMessage());
            return "order";
        }
        orderService.saveOrderWithItemsCustomerAndParcel(orderTempl.getOrder(), orderTempl.getItemsOfOrder(), orderTempl.getCustomer());
        return "redirect:/main";
    }

    @PostMapping(value = "/order", params = {"findCustomer"})
    public String findCustomer(OrderTempl orderTempl, BindingResult bindingResult, Model model) {
        orderTempl.setCustomersFromDB(customerRepository.getCustomerByFullNameIsLike(orderTempl.getFindByCustomerName()));
        if (orderTempl.getCustomersFromDB().isEmpty()) {
            orderTempl.setSearchAnswerCustomer("Not found");
        }
        initModelOrder(model, orderTempl);
        return "order";
    }

    @PostMapping(value = "/order", params = {"addCustomer"})
    public String addCustomerFromDB(OrderTempl orderTempl, BindingResult bindingResult, HttpServletRequest request, Model model) {
        orderTempl.setCustomer(customerRepository.getById(Long.parseLong(request.getParameter("addCustomer"))));
        initModelOrder(model, orderTempl);
        return "order";
    }

    private void initModelOrder(Model model, OrderTempl orderTempl) {
        model.addAttribute("order", orderTempl);
        model.addAttribute("status", OrderStatus.values());
        model.addAttribute("sections", sections);
    }

    @PostMapping(value = "/order/set_status")
    public String setOrderStatus(SetOrderStatusModal statusModal, Model model) {
        orderService.setOrderStatus(statusModal.getOrderId(), statusModal.getStatus());

        if (statusModal.getStatus().equals(OrderStatus.COMPLETE.toString())) {
            trackerService.registerParcelInTracker(statusModal.getTrackNum());
            orderService.addTrackNumberToOrderAndSaveParcel(statusModal.getOrderId(), statusModal.getTrackNum());
        }

        return "main/main";
    }

    @GetMapping(value = "/order/tracking")
    public String orderTracking(@RequestParam(value = "parcel_id", required = false) Long parcelId, Model model) {
        Parcel parcel = parcelRepository.getParcelByParcelId(parcelId);
        model.addAttribute("parcel", parcel);
        return "order_tracking";
    }

    @PostMapping(value = "/order/tracking/delivered")
    public String orderSetDelivered(Parcel parcel, Model model) {
        parcel = orderService.setOrderDelivered(parcel.getParcelId());
        model.addAttribute("parcel", parcel);
        return "order_tracking";
    }

}
