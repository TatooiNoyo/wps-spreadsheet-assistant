package io.github.tatooinoyo.wpsassistant.spreadsheet.controller;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IExcelService;
import io.github.tatooinoyo.wpsassistant.spreadsheet.MultipartFileAdaptor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Tatooi Noyo
 */
@RestController
@Tag(name = "测试")
@RequiredArgsConstructor
public class TestController {
    private final IExcelService testExcelService;


    @GetMapping("/test/exportExcel")
    @Operation(summary = "导出excel表格")
    public void exportExcel(HttpServletResponse response, @RequestParam List<String> ids) {
        testExcelService.exportExcel(response, ids);
    }

    @PostMapping(value = "/test/importExcel", consumes = {"multipart/form-data"})
    @Operation(summary = "导入excel表格")
    public boolean importExcel(HttpServletResponse response, @Validated MultipartFile file) {
        MultipartFileAdaptor multipartFileAdaptor = new MultipartFileAdaptor(file);
        return testExcelService.importExcel(response, multipartFileAdaptor);
    }

    @PostMapping("/test/downloadTemplate")
    @Operation(summary = "下载模板")
    public void downloadTemplate(HttpServletResponse response) {
        testExcelService.downloadTemplate(response);
    }
}
