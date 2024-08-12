package com.zerobase.babdeusilbun.component;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageComponentTest {
  @Mock
  private AmazonS3 amazonS3;

  @InjectMocks
  private ImageComponent imageComponent;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName;

  private final String folder = "folder";
  private final String filename = "test-storeImageDto.png";

  @DisplayName("이미지 업로드 테스트")
  @Test
  void uploadImageList() throws IOException {
    //given
    FileInputStream fileInputStream = new FileInputStream("src/test/resources/img/symbol.png");
    MultipartFile multipartFile =
        new MockMultipartFile("file", filename, "image/png", fileInputStream);
    URL fakeUrl = URI.create(format("https://s3.amazonaws.com/%s/%s/%s", bucketName, folder, filename)).toURL();

    given(amazonS3.getUrl(eq(bucketName), anyString())).willReturn(fakeUrl);

    //when
    List<String> urlList = imageComponent.uploadImageList(List.of(multipartFile), folder);

    //then
    assertNotNull(urlList);
    assertEquals(1, urlList.size());
    assertEquals(fakeUrl.toString(), urlList.getFirst());
  }

  @DisplayName("이미지 삭제 테스트")
  @Test
  void deleteImage() throws IOException{
    //given
    String url = format("https://s3.amazonaws.com/%s/%s/%s", bucketName, folder, filename);
    String decodedPath = format("%s/%s/%s", bucketName, folder, filename);

    willDoNothing().given(amazonS3).deleteObject(any());

    //when
    imageComponent.deleteImageByUrl(url);
    ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);

    //then
    verify(amazonS3).deleteObject(captor.capture());

    DeleteObjectRequest capturedRequest = captor.getValue();
    assertThat(capturedRequest.getBucketName()).isEqualTo(bucketName);
    assertThat(capturedRequest.getKey()).isEqualTo(decodedPath);
  }
}
