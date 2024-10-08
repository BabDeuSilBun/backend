package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.MenuRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.service.impl.MenuServiceImpl;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import com.zerobase.babdeusilbun.util.TestMenuUtility;
import com.zerobase.babdeusilbun.util.TestStoreUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static com.zerobase.babdeusilbun.util.ImageUtility.MENU_IMAGE_FOLDER;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ImageComponent imageComponent;

    @InjectMocks
    private MenuServiceImpl menuService;

    private final MenuDto.CreateRequest createRequest = MenuDto.CreateRequest.builder().
            name("가짜메뉴").description("가짜설명").image("url").price(200L).build();

    private final MenuDto.UpdateRequest updateRequest = MenuDto.UpdateRequest.builder().
            name("가짜메뉴2").description("가짜설명2").image("url2").price(300L).build();

    private final MultipartFile testMultipartFile = new MultipartFile() {
        @Override
        public String getName() {
            return "파일1";
        }

        @Override
        public String getOriginalFilename() {
            return "파일2";
        }

        @Override
        public String getContentType() {
            return "파일3";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 5;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return new byte[0];
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {

        }
    };

    @DisplayName("메뉴 생성 테스트 (이미지 업로드 X)")
    @Test
    void createMenu() {
        //given
        Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
        Store store = TestStoreUtility.getStore();
        createRequest.setImage(null);
        Menu expected = createRequest.toEntity(store);

        when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
                .thenReturn(Optional.of(entrepreneur));
        when(storeRepository.findByIdAndEntrepreneurAndDeletedAtIsNull(eq(store.getId()), eq(entrepreneur)))
                .thenReturn(Optional.of(store));
        when(menuRepository.save(eq(createRequest.toEntity(store)))).thenReturn(expected);

        //when
        MenuDto.CreateRequest result = menuService.createMenu(entrepreneur.getId(), store.getId(), null, createRequest);

        //then
        assertEquals(createRequest.getName(), result.getName());
        assertEquals(createRequest.getDescription(), result.getDescription());
        assertEquals(createRequest.getImage(), result.getImage());
        assertEquals(createRequest.getPrice(), result.getPrice());
    }

    @DisplayName("메뉴 생성 테스트 (이미지 업로드 성공)")
    @Test
    void createMenuCompleteWithImageUploadSuccess() {
        //given
        Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
        Store store = TestStoreUtility.getStore();
        Menu expected = createRequest.toEntity(store);

        when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
                .thenReturn(Optional.of(entrepreneur));
        when(storeRepository.findByIdAndEntrepreneurAndDeletedAtIsNull(eq(store.getId()), eq(entrepreneur)))
                .thenReturn(Optional.of(store));
        when(imageComponent.uploadImageList(eq(List.of(testMultipartFile)), eq(MENU_IMAGE_FOLDER)))
                .thenReturn(List.of(createRequest.getImage()));
        when(menuRepository.save(eq(createRequest.toEntity(store)))).thenReturn(expected);

        //when
       MenuDto.CreateRequest result = menuService.createMenu(entrepreneur.getId(), store.getId(), testMultipartFile, createRequest);

       //then
       assertEquals(createRequest.getName(), result.getName());
       assertEquals(createRequest.getDescription(), result.getDescription());
       assertEquals(createRequest.getImage(), result.getImage());
       assertEquals(createRequest.getPrice(), result.getPrice());

    }

    @DisplayName("메뉴 생성 테스트 (이미지 업로드 실패 시)")
    @Test
    void createMenuWithImageUploadFail() {

        //given
        Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
        Store store = TestStoreUtility.getStore();
        createRequest.setImage(null);
        Menu expected = createRequest.toEntity(store);

        //when
        when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
                .thenReturn(Optional.of(entrepreneur));
        when(storeRepository.findByIdAndEntrepreneurAndDeletedAtIsNull(eq(store.getId()), eq(entrepreneur)))
                .thenReturn(Optional.of(store));
        when(imageComponent.uploadImageList(eq(List.of(testMultipartFile)), eq(MENU_IMAGE_FOLDER)))
                .thenReturn(List.of());
        when(menuRepository.save(eq(createRequest.toEntity(store)))).thenReturn(expected);

        createRequest.setImage("url");
        MenuDto.CreateRequest result = menuService.createMenu(entrepreneur.getId(), store.getId(), testMultipartFile, createRequest);

        //then
        assertEquals(createRequest.getName(), result.getName());
        assertEquals(createRequest.getDescription(), result.getDescription());
        assertEquals(createRequest.getImage(), null);
        assertEquals(result.getImage(), null);
        assertEquals(createRequest.getPrice(), result.getPrice());
    }

    @DisplayName("메뉴 수정 테스트")
    @Test
    void updateMenu() {
        // given
        Menu menu = TestMenuUtility.getMenu();

        Store store = menu.getStore();
        Entrepreneur entrepreneur = store.getEntrepreneur();

        // when
        when(menuRepository.findByIdAndDeletedAtIsNull(menu.getId()))
                .thenReturn(Optional.of(menu));
        when(menuRepository.existsByStoreAndNameAndPriceAndDeletedAtIsNull(menu.getStore(), updateRequest.getName(), updateRequest.getPrice()))
                .thenReturn(false);

        MenuDto.UpdateRequest result = menuService.updateMenu(entrepreneur.getId(), menu.getId(), null, updateRequest);

        // then
        assertEquals(result.getName(), updateRequest.getName());
        assertEquals(result.getDescription(), updateRequest.getDescription());
        assertEquals(result.getPrice(), updateRequest.getPrice());
        assertEquals(result.getImage(), updateRequest.getImage());
    }

    @DisplayName("메뉴 수정 테스트 (이미지 업로드 성공)")
    @Test
    void updateMenuCompleteWithImageUploadSuccess() {
        // given
        Menu menu = TestMenuUtility.getMenu();

        Store store = menu.getStore();
        Entrepreneur entrepreneur = store.getEntrepreneur();

        // when
        when(menuRepository.findByIdAndDeletedAtIsNull(menu.getId()))
                .thenReturn(Optional.of(menu));
        when(menuRepository.existsByStoreAndNameAndPriceAndDeletedAtIsNull(menu.getStore(), updateRequest.getName(), updateRequest.getPrice()))
                .thenReturn(false);
        when(imageComponent.uploadImageList(eq(List.of(testMultipartFile)), eq(MENU_IMAGE_FOLDER)))
                .thenReturn(List.of(createRequest.getImage()));

        MenuDto.UpdateRequest result = menuService.updateMenu(entrepreneur.getId(), menu.getId(), testMultipartFile, updateRequest);

        // then
        assertEquals(result.getName(), updateRequest.getName());
        assertEquals(result.getDescription(), updateRequest.getDescription());
        assertEquals(result.getPrice(), updateRequest.getPrice());
        assertEquals(result.getImage(), updateRequest.getImage());
    }

    @DisplayName("메뉴 수정 테스트 (이미지 업로드 실패)")
    @Test
    void updateMenuCompleteWithImageUploadFail() {
        // given
        Menu menu = TestMenuUtility.getMenu();

        Store store = menu.getStore();
        Entrepreneur entrepreneur = store.getEntrepreneur();

        // when
        when(menuRepository.findByIdAndDeletedAtIsNull(menu.getId()))
                .thenReturn(Optional.of(menu));
        when(menuRepository.existsByStoreAndNameAndPriceAndDeletedAtIsNull(menu.getStore(), updateRequest.getName(), updateRequest.getPrice()))
                .thenReturn(false);
        when(imageComponent.uploadImageList(eq(List.of(testMultipartFile)), eq(MENU_IMAGE_FOLDER)))
                .thenReturn(List.of());

        MenuDto.UpdateRequest result = menuService.updateMenu(entrepreneur.getId(), menu.getId(), testMultipartFile, updateRequest);

        // then
        assertEquals(result.getName(), updateRequest.getName());
        assertEquals(result.getDescription(), updateRequest.getDescription());
        assertEquals(result.getPrice(), updateRequest.getPrice());
        assertEquals(updateRequest.getImage(), null);
        assertEquals(result.getImage(), null);
    }

    @DisplayName("메뉴 삭제 테스트")
    @Test
    void deleteMenuComplete() {
        // given
        Menu menu = TestMenuUtility.getMenu();

        Store store = menu.getStore();
        Entrepreneur entrepreneur = store.getEntrepreneur();

        // when
        when(menuRepository.findByIdAndDeletedAtIsNull(menu.getId()))
                .thenReturn(Optional.of(menu));

        Menu result = menuService.deleteMenu(entrepreneur.getId(), menu.getId());

        // then
        assertNotEquals(result.getDeletedAt(), null);
    }
}

