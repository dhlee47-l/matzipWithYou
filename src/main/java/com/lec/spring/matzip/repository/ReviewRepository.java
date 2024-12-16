package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.member.domain.Member;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.util.List;

@Repository
public interface ReviewRepository {
    List<Review> findAll();

    Review findById(Long id);

    int save(ReviewDTO reviewDTO, Model model);

    int saveReviewTags(List<ReviewTagDTO> reviewTags);

    List<Long> checkHiddenMatzip(Long matzipId, Long memberId);

    int deleteById(Long id);
}
