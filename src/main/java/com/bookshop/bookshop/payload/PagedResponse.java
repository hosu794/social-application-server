package com.bookshop.bookshop.payload;

import java.util.List;

public class PagedResponse<T> {

    private List<T> content;

    private int page;

    private int size;

    private long totalElement;

    private int totalPages;

    private boolean last;

    public PagedResponse() {}

    public PagedResponse(List<T> content, int page, int size, long totalElement, int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElement = totalElement;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(long totalElement) {
        this.totalElement = totalElement;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
