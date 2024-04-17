package com.example.courseWork.DTO.gameSaveDTO;

public class GameStatesRequestDTO {
    private String searchQuery;

    private int pageSize;

    private int pageNumber;
    private int searchGameId;

    public GameStatesRequestDTO() {}

    public GameStatesRequestDTO(String searchQuery, int pageSize, int pageNumber, int searchGameId) {
        this.searchQuery = searchQuery;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.searchGameId = searchGameId;

    }

    public GameStatesRequestDTO(String searchQuery, int pageSize, int pageNumber) {
        this.searchQuery = searchQuery;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getSearchGameId() {
        return searchGameId;
    }

    public void setSearchGameId(int searchGameId) {
        this.searchGameId = searchGameId;
    }
}
