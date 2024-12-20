package com.example.report.controller;

import com.example.report.constant.ResponseStringConstant;
import com.example.report.dto.request.ReportRequest;
import com.example.report.dto.response.JsonResponse;
import com.example.report.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @PostMapping
    public JsonResponse<String> createReport(@RequestBody ReportRequest report) {
        reportService.save(report);
        return JsonResponse.success(ResponseStringConstant.ADD_REPORT);
    }

    @PostMapping("/get-reports")
    public JsonResponse<Boolean> haveReports(@RequestBody ReportRequest report) {
        return JsonResponse.success(reportService.isReported(report.getPostId()));
    }

}
