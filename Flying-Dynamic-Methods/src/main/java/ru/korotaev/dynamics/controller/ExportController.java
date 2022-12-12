package ru.korotaev.dynamics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.korotaev.dynamics.service.ExportService;

import java.io.IOException;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/eiler")
    public ResponseEntity<Resource> export(@RequestParam double dt, @RequestParam boolean alpha) throws IOException {
        final Resource resource = exportService.exportByEilerMethod(dt, alpha);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Dynamic_values")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
