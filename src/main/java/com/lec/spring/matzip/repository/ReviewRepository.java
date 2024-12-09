package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.member.domain.Member;
import org.springframework.ui.Model;

import java.util.List;

public interface ReviewRepository {
    List<Review> findAll();

    Review findById(Long id);

    int save(ReviewDTO reviewDTO, Model model);

    int saveReviewTags(List<ReviewTag> reviewTags);

    List<Member> checkHiddenMatzip(Long matzipId, List<Member> memberIds);

    Review deleteById(Long id);
}
