package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.member.domain.Member;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews();

    void addReview(Review review, List<Tag> tags, FoodKind foodKind);

    void unlockHiddenMatizipAndNotify(Review review, Member member);

    int deleteReview(Long id);
}
