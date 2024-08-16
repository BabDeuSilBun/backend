package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ALREADY_EXIST_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.STORE_IMAGE_FOLDER;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreDto.ImageUrl;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.CategoryRepository;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.service.StoreService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final EntrepreneurRepository entrepreneurRepository;
  private final StoreRepository storeRepository;
  private final StoreImageRepository imageRepository;
  private final SchoolRepository schoolRepository;
  private final CategoryRepository categoryRepository;
  private final ImageComponent imageComponent;

  @Override
  @Transactional
  public int createStore(Long entrepreneurId, List<MultipartFile> images, CreateRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNotNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    if (storeRepository.existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
        entrepreneur, request.getName(), request.getAddress().toEntity())
    ) {
      throw new CustomException(ALREADY_EXIST_STORE);
    }

    Store store = request.toEntity(entrepreneur);
    storeRepository.save(store);

    List<StoreImage> uploadImageList = new ArrayList<>();
    AtomicInteger sequence = new AtomicInteger();

    imageComponent.uploadImageList(images, STORE_IMAGE_FOLDER).forEach(url ->
      uploadImageList.add(
          ImageUrl.builder()
              .url(url)
              .isRepresentative(sequence.get() == 0)
              .sequence(sequence.getAndIncrement())
              .build()
              .toEntity(store)
      )
    );

    if (!uploadImageList.isEmpty()) {
      imageRepository.saveAll(uploadImageList);
    }

    return uploadImageList.size();
  }
}
