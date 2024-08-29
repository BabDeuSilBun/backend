package com.zerobase.babdeusilbun.controller.lookup;

import static com.zerobase.babdeusilbun.util.NicknameUtil.createRandomNickname;

import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.MajorDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.UserDto;
import com.zerobase.babdeusilbun.service.MajorService;
import com.zerobase.babdeusilbun.service.SchoolService;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.lookup.LookupSwagger.GetAllCategoriesSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.lookup.LookupSwagger.GetRandomNicknameSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.lookup.LookupSwagger.SearchMajorSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.lookup.LookupSwagger.SearchSchoolAndCampusSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LookupController {
  private final StoreService storeService;
  private final SchoolService schoolService;
  private final MajorService majorService;

  @GetMapping("/schools")
  @SearchSchoolAndCampusSwagger
  public ResponseEntity<Page<SchoolDto.Information>> searchSchoolAndCampus(
      @RequestParam(name = "schoolName", required = false, defaultValue = "") String schoolName,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(schoolService.searchSchoolAndCampus(schoolName, page, size));
  }

  @GetMapping("/users/signup/majors")
  @SearchMajorSwagger
  public ResponseEntity<Page<MajorDto.Information>> searchMajor(
      @RequestParam(name = "majorName", required = false, defaultValue = "") String majorName,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(majorService.searchMajor(majorName, page, size));
  }

  @GetMapping("/stores/categories")
  @GetAllCategoriesSwagger
  public ResponseEntity<Page<Information>> getAllCategories(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllCategories(page, size));
  }

  @GetMapping("/random-nickname")
  @GetRandomNicknameSwagger
  public ResponseEntity<UserDto.NicknameResponse> getRandomNickname() {
    return ResponseEntity.ok(UserDto.NicknameResponse.builder()
            .nickname(createRandomNickname()).build());
  }
}
