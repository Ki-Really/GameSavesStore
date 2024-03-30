package com.example.courseWork.DTO.usersDTO;

import java.util.List;

public class PeopleDTO {
    private List<PersonDTO> items;
    private long totalCount;

    public PeopleDTO(List<PersonDTO> items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public PeopleDTO() {
    }

    public List<PersonDTO> getItems() {
        return items;
    }

    public void setItems(List<PersonDTO> items) {
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
