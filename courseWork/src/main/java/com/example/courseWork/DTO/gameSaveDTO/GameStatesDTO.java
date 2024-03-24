package com.example.courseWork.DTO.gameSaveDTO;


import java.util.List;

public class GameStatesDTO {
    private List<GameStateDTO> items;
    private long totalCount;

    public GameStatesDTO(List<GameStateDTO> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public GameStatesDTO() {

    }

    public List<GameStateDTO> getItems() {
        return items;
    }

    public void setItems(List<GameStateDTO> items) {
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
