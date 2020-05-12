package com.alahr.spring.boot.projects.elasticsearch.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ResultResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pageNo;
    private Integer pageSize;
    private Boolean isSuccess;
    private String message;
    private List<Map<String, Object>> list;

    public ResultResponse() {
        super();
    }

    public ResultResponse(Integer pageNo, Integer pageSize, Boolean isSuccess, List<Map<String, Object>> list, String message) {
        this();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.isSuccess = isSuccess;
        this.list = list;
        this.message = message;
    }


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
