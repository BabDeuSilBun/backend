package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto.MyPage;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto.UpdateRequest;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.service.EntrepreneurService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import io.micrometer.common.util.StringUtils;

import java.util.List;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.ENTREPRENEUR_IMAGE_FOLDER;

@Service
@AllArgsConstructor
public class EntrepreneurServiceImpl implements EntrepreneurService {

    private final EntrepreneurRepository entrepreneurRepository;

    private final ImageComponent imageComponent;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MyPage getMyPage(Long entrepreneurId) {
        return entrepreneurRepository.findMyPageByUserId(entrepreneurId)
                .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));
    }

    @Override
    @Transactional
    public UpdateRequest updateProfile(Long entrepreneurId, MultipartFile image, UpdateRequest request) {
        Entrepreneur entrepreneur = entrepreneurRepository.findByIdAndDeletedAtIsNull(entrepreneurId)
                .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

        if (image != null) {
            updateImage(entrepreneur, image, request);
        } else if (request.getImage() != null && request.getImage().isEmpty() && StringUtils.isNotBlank(entrepreneur.getImage())) {
            imageComponent.deleteImageByUrl(entrepreneur.getImage());
        }

        if (request.getPassword() != null) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        entrepreneur.update(request);
        return request;
    }

    private void updateImage(Entrepreneur entrepreneur, MultipartFile image, UpdateRequest request) {
        List<String> uploadUrlList = imageComponent.uploadImageList(List.of(image), ENTREPRENEUR_IMAGE_FOLDER);
        if (uploadUrlList.isEmpty()) {
            request.setImage(null);
            return;
        }

        if (StringUtils.isNotBlank(entrepreneur.getImage())) {
            imageComponent.deleteImageByUrl(entrepreneur.getImage());
        }

        request.setImage(uploadUrlList.getFirst());
    }
}
