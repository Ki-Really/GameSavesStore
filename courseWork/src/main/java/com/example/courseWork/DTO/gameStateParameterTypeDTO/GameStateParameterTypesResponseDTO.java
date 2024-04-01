package com.example.courseWork.DTO.gameStateParameterTypeDTO;

import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;

import java.util.List;

public class GameStateParameterTypesResponseDTO {
    private List<GameStateParameterTypeDTO> items;
    private long totalCount;

    public GameStateParameterTypesResponseDTO(List<GameStateParameterTypeDTO> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public GameStateParameterTypesResponseDTO() {
    }

    public List<GameStateParameterTypeDTO> getItems() {
        return items;
    }

    public void setItems(List<GameStateParameterTypeDTO> items) {
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
