package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.swagger.annotation.MajorSwagger.*;

import com.zerobase.babdeusilbun.service.MajorService;
import com.zerobase.babdeusilbun.swagger.annotation.MajorSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
public class MajorController {

   private final MajorService majorService;

    /**
     * 학과 검색
     */
    @GetMapping
    @SearchSchoolAndCampusSwagger
    public ResponseEntity<?> searchSchoolAndCampus(
            @RequestParam(required = false, defaultValue = "") String majorName,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(majorService.searchMajor(majorName, page, size));
    }

}
