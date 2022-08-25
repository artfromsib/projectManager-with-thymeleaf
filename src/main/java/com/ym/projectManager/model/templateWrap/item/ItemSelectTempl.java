package com.ym.projectManager.model.templateWrap.item;

import com.ym.projectManager.model.ItemSection;

import java.util.List;
import java.util.stream.Stream;

public class ItemSelectTempl {
    private Boolean addSection = false;
    private List<ItemSection> sections;
    private ItemSection selectedSection = new ItemSection();
    private Boolean addStatus = false;
    private Stream<String> status;
    private String selectedStatus;


    public ItemSelectTempl(Boolean addSection, List<ItemSection> sections, ItemSection selectedSection, Boolean addStatus, Stream<String> status, String selectedStatus) {
        this.addSection = addSection;
        this.sections = sections;
        this.selectedSection = selectedSection;
        this.addStatus = addStatus;
        this.status = status;
        this.selectedStatus = selectedStatus;
    }

    public ItemSelectTempl() {
    }


    public Boolean getAddSection() {
        return addSection;
    }

    public void setAddSection(Boolean addSection) {
        this.addSection = addSection;
    }

    public List<ItemSection> getSections() {
        return sections;
    }

    public void setSections(List<ItemSection> sections) {
        this.sections = sections;
    }

    public ItemSection getSelectedSection() {
        return selectedSection;
    }

    public void setSelectedSection(ItemSection selectedSection) {
        this.selectedSection = selectedSection;
    }

    public Boolean getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(Boolean addStatus) {
        this.addStatus = addStatus;
    }

    public Stream<String> getStatus() {
        return status;
    }

    public void setStatus(Stream<String> status) {
        this.status = status;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }
}
