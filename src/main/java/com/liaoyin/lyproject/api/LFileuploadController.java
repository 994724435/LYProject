package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.service.LFileuploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

/**
 * 时间：2018/8/3 16:35
 * 描述：文件上传下载
 */
@Api(description = "文件上传、下载")
@RestController
@RequestMapping(value = "/file")
public class LFileuploadController {

    @Autowired
    private LFileuploadService fileuploadService;

    /**
     * 时间： 2018/8/3 16:49
     * 描述： 上传文件
     **/
    @ApiOperation(value = "上传文件", notes = "", response = String.class)
    @PostMapping(value = "fileupload")
    @FilterAnnotation
    @RepeatRequest
    public JsonRestResponse fileupload(
            @ApiParam(value = "文件", required = true) @RequestParam MultipartFile file,
            HttpServletRequest request) throws Exception {
        return fileuploadService.fileupload(file, request);
    }

    /**
     * 时间： 2018/8/3 17:17
     * 描述： 批量文件上传
     **/
//    @ApiOperation(value = "批量上传文件", notes = "", response = String.class)
//    @PostMapping(value = "batchFileupload")
//    public JsonRestResponse batchFileupload(
//            @ApiParam(value = "文件", required = true) @RequestParam MultipartFile[] fileAll,
//            HttpServletRequest request) throws Exception {
//        return fileuploadService.batchFileupload(fileAll, request);
//    }

    /**
     * 时间： 2018/8/3 17:28
     * 描述： 文件下载
     **/
//    @ApiOperation(value = "文件下载", notes = "", response = String.class)
//    @PostMapping(value = "fileDown")
//    public void fileDown(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @ApiParam(value = "文件名称(或全路径即可)", required = true) @RequestParam String name,
//            @ApiParam(value = "文件路径") @RequestParam(required = false) String path) throws Exception {
//        fileuploadService.fileDown(request, response, name, path);
//    }

}
