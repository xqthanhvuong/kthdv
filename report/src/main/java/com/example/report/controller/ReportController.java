package com.example.report.controller;

import com.example.report.constant.ResponseStringConstant;
import com.example.report.dto.request.ReportRequest;
import com.example.report.dto.response.JsonResponse;
import com.example.report.dto.response.ReportResponse;
import com.example.report.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/count/{postId}")
    public JsonResponse<Integer> getReportCount(@PathVariable Long postId) {
        return JsonResponse.success(reportService.getCountReportOf(postId));
    }

    @GetMapping("/{postId}")
    public JsonResponse<List<ReportResponse>> getReportsOfPost(@PathVariable long postId) {
        return JsonResponse.success(reportService.getReports(postId));
    }

}
