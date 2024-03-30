package com.example.courseWork.DTO.gamePathDTO;

public class GamePathsRequestDTO {
    private String searchQuery;

    private int pageSize;

    private int pageNumber;

    public GamePathsRequestDTO(String searchQuery, int pageSize, int pageNumber) {
        this.searchQuery = searchQuery;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public GamePathsRequestDTO() {
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
}
