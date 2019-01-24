package com.housaire.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.housaire.model.LinkmanListModel;
import com.housaire.model.LinkmanModel;
import com.housaire.model.LinkmanQueryModel;
import com.housaire.model.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("联系人管理")
@RestController
@RequestMapping("/linkman")
public class LinkmanController
{
    @ApiOperation("新增联系人并返回联系人ID")
    @PostMapping
    public Long addLinkman(@RequestBody LinkmanModel LinkmanModel)
    {
        return 20L;
    }

    @ApiOperation("更新联系人信息")
    @PutMapping
    public void updateLinkman(@RequestBody LinkmanModel LinkmanModel)
    {

    }

    @ApiOperation("删除联系人")
    @DeleteMapping("/{id}")
    public void removeLinkman(@PathVariable("id") String id)
    {

    }

    @ApiOperation("获取联系人详情")
    @GetMapping("/{id}")
    public LinkmanModel getLinkman(@PathVariable("id") String id)
    {
        return new LinkmanModel();
    }

    @ApiOperation("获取联系人列表")
    @GetMapping
    public PageInfo<LinkmanListModel> listLinkman(Pagination<LinkmanQueryModel> queryModel)
    {
        Page<LinkmanListModel> page = new Page<>(1, 10);
        page.setTotal(20);
        return new PageInfo<>(page);
    }

    @ApiOperation("联系人导入")
    @PostMapping(value = "/import", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public void importLinkman(@ApiParam(value = "联系人信息", required = true) MultipartFile file)
    {

    }

    @ApiOperation("联系人导出")
    @GetMapping("/export")
    public void exportLinkman(LinkmanQueryModel queryModel)
    {

    }

    @ApiOperation("联系人导入模板下载")
    @GetMapping("/template/download")
    public void downloadTemplate()
    {

    }

    public static void main(String[] args)
    {

        System.out.println(JSON.toJSONString(new LinkmanListModel(), SerializerFeature.WriteMapNullValue));
    }

}
