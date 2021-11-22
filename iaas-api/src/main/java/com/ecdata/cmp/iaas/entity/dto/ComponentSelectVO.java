package com.ecdata.cmp.iaas.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 19:50
 */
@Data
public class ComponentSelectVO {
    private List<Data> selectParm;

    public class Data {
        private String paramValue;
        private String displayValue;
        private String unit;
        private String sort;
        private String remark;

        public String getParamValue() {
            return paramValue;
        }

        public void setParamValue(String paramValue) {
            this.paramValue = paramValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }

        public void setDisplayValue(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
