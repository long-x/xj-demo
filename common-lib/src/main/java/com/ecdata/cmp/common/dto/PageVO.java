package com.ecdata.cmp.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> 数据对象
 * @author xuxinsheng
 * @since 2019-04-11
 */
@Data
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = -757140737715250587L;

    /** 数据列表 */
    @ApiModelProperty(value = "数据列表")
    private List<T> data;

    /** 总记录数 */
    @ApiModelProperty(value = "总记录数")
    private Long totalCount;

    /** 总页数 */
    @ApiModelProperty(value = "总页数")
    private Long totalPage;

    /** 每页大小 */
    @ApiModelProperty(value = "每页大小")
    private Long pageSize;

    /** 页数 */
    @ApiModelProperty(value = "页数")
    private Long pageNo;

    public PageVO() {

    }

    public PageVO(IPage<T> page) {
        this.data = page.getRecords();
        this.totalCount = page.getTotal();
        this.totalPage = page.getPages();
        this.pageSize = page.getSize();
        this.pageNo = page.getCurrent();
    }

    public PageVO(IPage page, List<T> data) {
        this.data = data;
        this.totalCount = page.getTotal();
        this.totalPage = page.getPages();
        this.pageSize = page.getSize();
        this.pageNo = page.getCurrent();
    }

    public PageVO(long pageNo, long pageSize, List<T> data) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        if (data != null && data.size() > 0) {
            this.totalCount = (long) data.size();
            this.totalPage = (totalCount - 1) / pageSize + 1;
            int size = data.size();
            if (size > pageSize) {
                int begin = (int) ((pageNo - 1) * pageSize);
                int end = (int) (begin + pageSize > size ? size : begin + pageSize);
                this.data = data.subList(begin, end);
            } else {
                this.data = data;
            }
        } else {
            this.data = new ArrayList<>();
            this.totalCount = 0L;
            this.totalPage = 0L;
        }
    }
}
