package com.example.courseWork.DTO.gamePathDTO;

import com.example.courseWork.DTO.gameSaveDTO.GameStateDTO;

import java.util.List;

public class GamePathsResponseDTO {
    private List<List<GamePathDTO>> items;
    private long totalCount;

    public GamePathsResponseDTO(List<List<GamePathDTO>> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public GamePathsResponseDTO() {
    }

    public List<List<GamePathDTO>> getItems() {
        return items;
    }

    public void setItems(List<List<GamePathDTO>> items) {
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
