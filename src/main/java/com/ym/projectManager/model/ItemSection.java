package com.ym.projectManager.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "section")
public class ItemSection {

    @Id
    @Column(name = "section_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long itemSectionId;
    private String name;

    @OneToMany(mappedBy = "itemSection", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    private Set<Item> items;


    public ItemSection(Long ItemSectionId, String name, Set<Item> items) {
        this.itemSectionId = ItemSectionId;
        this.name = name;
        this.items = items;
    }

    public ItemSection() {

    }

    public ItemSection(String name) {
        this.name = name;
    }

    public ItemSection(Long ItemSectionId, String name) {
        this.itemSectionId = ItemSectionId;
        this.name = name;
    }


    public Long getItemSectionId() {
        return itemSectionId;
    }

    public void setItemSectionId(Long id) {
        this.itemSectionId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSection section = (ItemSection) o;
        return Objects.equals(itemSectionId, section.itemSectionId) && Objects.equals(name, section.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemSectionId, name);
    }
}
