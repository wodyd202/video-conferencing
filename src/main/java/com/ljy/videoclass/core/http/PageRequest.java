package com.ljy.videoclass.core.http;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PageRequest {
    @Min(value = 0, message = "page는 0이상 10이하로 입력해주세요.")
    @Max(value = 10, message = "page는 0이상 10이하로 입력해주세요.")
    private int page;

    @Min(value = 1, message = "size는 1이상 10이하로 입력해주세요.")
    @Max(value = 10, message = "size는 1이상 10이하로 입력해주세요.")
    private int size;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
