package com.example.courseWork.DTO.entityDTO;

import java.util.List;

public class EntitiesResponseDTO<T> {
    private List<T> items;

    private long totalCount;

    public EntitiesResponseDTO(List<T> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public EntitiesResponseDTO() {
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
