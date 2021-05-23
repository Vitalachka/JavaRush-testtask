package com.game.entity;

import com.game.controller.PlayerOrder;
import org.springframework.data.domain.Sort;

public class PlayerPage {
    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = PlayerOrder.ID.getFieldName();

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
