
package com.example.a305.nastamap.apifeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HalJson {

    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;
    @SerializedName("page_count")
    @Expose
    private Long pageCount;
    @SerializedName("page_size")
    @Expose
    private Long pageSize;
    @SerializedName("total_items")
    @Expose
    private Long totalItems;
    @SerializedName("page")
    @Expose
    private Long page;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

}
