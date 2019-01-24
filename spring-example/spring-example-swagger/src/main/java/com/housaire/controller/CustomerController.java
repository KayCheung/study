package com.housaire.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.housaire.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("客户管理")
@RestController
@RequestMapping("/customers")
public class CustomerController
{

    @ApiOperation("新增客户并返回客户ID")
    @PostMapping
    public Long addCustomer(@RequestBody CustomerModel CustomerModel) {
        return 100L;
    }

    @ApiOperation("更新客户信息")
    @PutMapping
    public void updateCustomer(@RequestBody CustomerModel CustomerModel) {

    }

    @ApiOperation("删除客户")
    @DeleteMapping("/{id}")
    public void removeCustomer(@PathVariable("id") String id) {

    }

    @ApiOperation("获取客户详情")
    @GetMapping("/{id}")
    public CustomerModel getCustomer(@PathVariable("id") String id) {
        return new CustomerModel();
    }

    @ApiOperation("获取客户列表")
    @GetMapping(value = "/list", params = {"v=1.0"})
    public PageInfo<CustomerListModel> listCustomer1(Pagination<CustomerQueryModel> queryModel) {
        Page<CustomerListModel> page = new Page<>(1, 10);
        page.setTotal(20);
        return new PageInfo<>(page);
    }

    @ApiOperation("获取客户列表")
    @GetMapping(value = "/list", params = {"v=2.0"})
    public PageInfo<CustomerListModel> listCustomer2(Pagination<CustomerQueryModel> queryModel) {
        Page<CustomerListModel> page = new Page<>(1, 10);
        page.setTotal(20);
        return new PageInfo<>(page);
    }

    @ApiOperation("客户转移")
    @PostMapping("/transfer")
    public void transferCustomer(@RequestBody CustomerTransferModel transferModel) {

    }

    @ApiOperation("客户导入")
    @PostMapping(value = "/import", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public void importCustomer(@ApiParam(value = "客户信息", required = true) MultipartFile file) {

    }

    @ApiOperation("客户导出")
    @GetMapping("/export")
    public void exportCustomer(CustomerQueryModel queryModel) {

    }

    @ApiOperation("客户导入模板下载")
    @GetMapping("/template/download")
    public void downloadTemplate() {

    }

}
