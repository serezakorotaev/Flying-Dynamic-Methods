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
    public ResponseEntity<Resource> exportEiler(@RequestParam double dt, @RequestParam boolean alphaZero) throws IOException {
        final Resource resource = exportService.exportByEilerMethod(dt, alphaZero);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Dynamic_values_dt" + dt)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/eiler/modify")
    public ResponseEntity<Resource> exportEilerModify(@RequestParam double dt, @RequestParam boolean alphaZero) throws IOException {
        final Resource resource = exportService.exportByEilerModifyMethod(dt, alphaZero);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Dynamic_values_Eiler_Modify_dt" + dt)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/kutt")
    public ResponseEntity<Resource> exportKutt(@RequestParam double dt, @RequestParam boolean alphaZero) throws IOException {
        final Resource resource = exportService.exportByKuttMethod(dt, alphaZero);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Dynamic_values_Kutt_dt" + dt)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
