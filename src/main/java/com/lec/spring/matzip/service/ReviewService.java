package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.member.domain.Member;
import org.springframework.ui.Model;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews();

    Review findById(Long id);

    int addReview(ReviewDTO reviewDTO, Model model);

    String addContent(Long id, String content);

    FoodKind addFoodKind(String kindName);

    List<ReviewTag> addReviewTags(Long id, List<Long> tagIds);

    List<Member> hiddenMatzipMemberIds(ReviewDTO reviewDTO);

    int rewardReviewPoint(ReviewDTO reviewDTO, int rewardHiddenPoint, int rewardPoint);

    int rewardReviewIntimacy(ReviewDTO reviewDTO, int rewardHiddenIntimacy ,int rewardIntimacy);

    int deleteReview(Long id);
}