package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InquiryImageRepository extends JpaRepository<InquiryImage, Long> {

  List<InquiryImage> findAllByInquiryOrderBySequence(Inquiry inquiry);

  @Query("select ii from InquiryImage ii "
        + "where ii.inquiry = :inquiry "
        + "and ii != :image "
        + "order by ii.sequence asc")
  List<InquiryImage> findAllExceptUpdatedImage(Inquiry inquiry, InquiryImage image);

  List<InquiryImage> findAllByInquiry(Inquiry inquiry);
}
