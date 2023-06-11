package org.greenway.backend.model;


import org.springframework.data.domain.Sort;

public class PageSettings {

    private int page = 0;
    private int elementPerPage = 10;
    private String direction = "dsc";
    private String key;

    public Sort buildSort(){
        return switch (direction) {
            case "dsc" -> Sort.by(key).descending();
            case "asc" -> Sort.by(key).ascending();
            default -> Sort.by(key).descending();
        };
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getElementPerPage() {
        return elementPerPage;
    }

    public void setElementPerPage(int elementPerPage) {
        this.elementPerPage = elementPerPage;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
