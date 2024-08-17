package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MajorController {

   private final MajorService majorService;

    /**
     * 학과 검색
     */
    @GetMapping("/majors")
    public ResponseEntity<?> searchSchoolAndCampus(
            @RequestParam(name = "majorName", required = false, defaultValue = "") String majorName,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(majorService.searchMajor(majorName, page, size));
    }

}
