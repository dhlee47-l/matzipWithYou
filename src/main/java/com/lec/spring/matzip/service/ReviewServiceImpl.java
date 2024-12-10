package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.repository.MatzipRepository;
import com.lec.spring.matzip.repository.ReviewRepository;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.MemberRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MatzipRepository matzipRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    public ReviewServiceImpl(SqlSession sqlSession, MatzipService matzipService, FriendRepository friendRepository) {
        this.reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        this.matzipRepository = sqlSession.getMapper(MatzipRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
        this.friendRepository = friendRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public int addReview(ReviewDTO reviewDTO, Model model) {
        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("음식점 정보를 찾을 수 없습니다");
        }

        Review review = reviewDTO;

        int saved = reviewRepository.save(reviewDTO, model);

        Member member = memberRepository.findById(reviewDTO.getMemberId());

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);
        int rewardReviewPoint = rewardReviewPoint(reviewDTO, 100, 10);
        int rewardReviewIntimacy = rewardReviewIntimacy(reviewDTO, 100, 10);

        model.addAttribute("result", !hiddenMatzipMemberIds.isEmpty()  ? "UNLOCK" : "saveOk");
        model.addAttribute("member", hiddenMatzipMemberIds);
        model.addAttribute("rewardReviewPoint", rewardReviewPoint);
        model.addAttribute("rewardReviewIntimacy", rewardReviewIntimacy);

        return saved;
    }

    @Override
    public List<ReviewTag> addReviewTag(Long id, List<Long> tagIds) {
        List<Tag> tags = tagRepository.findByIds(tagIds);

        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("태그 정보를 찾을 수 없습니다.");
        }

        List<ReviewTag> reviewTags = tags.stream()
                .map(tag -> ReviewTag.builder()
                        .tagId(tag.getId())
                        .reviewId(id)
                        .regdate(LocalDateTime.now())
                        .build())
                .toList();

        reviewRepository.saveReviewTags(reviewTags);

        return reviewTags;
    }

    @Override
    public List<Member> hiddenMatzipMemberIds(ReviewDTO reviewDTO) {
        Long matzipId = reviewDTO.getMatzipId();
        Long memberId = reviewDTO.getMemberId();

        List<Long> hiddenFriendIds = reviewRepository.checkHiddenMatzip(matzipId, memberId);

        List<Member> hiddenMatzipMembers = new ArrayList<>();
        if (hiddenFriendIds != null && !hiddenFriendIds.isEmpty()) {
            hiddenMatzipMembers = memberRepository.findByIds(hiddenFriendIds);
        }

        return hiddenMatzipMembers;
    }

    @Override
    public int rewardReviewPoint(ReviewDTO reviewDTO, int rewardHiddenPoint, int rewardPoint) {
        Member member = memberRepository.findById(reviewDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);

        int resultPoint = !hiddenMatzipMemberIds.isEmpty() ?  rewardHiddenPoint: rewardPoint;

        member.setPoint(member.getPoint() + resultPoint);
        memberRepository.updatePoint(member.getId(), member.getPoint());

        return member.getPoint();
    }

    @Override
    public int rewardReviewIntimacy(ReviewDTO reviewDTO, int rewardHiddenIntimacy, int rewardIntimacy) {
        Friend friend = friendRepository.findFriends(reviewDTO.getId());
        if (friend == null) {
            throw new IllegalArgumentException("Friend not found");
        }

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);

        int resultIntimacy = !hiddenMatzipMemberIds.isEmpty() ? rewardHiddenIntimacy : rewardIntimacy;

        friend.setIntimacy(friend.getIntimacy() + resultIntimacy);
        friendRepository.updateIntimacy(reviewDTO.getId(), friend.getIntimacy());

        return friend.getIntimacy();
    }


    @Override
    public Review deleteReview(Long id) {
        return reviewRepository.deleteById(id);
    }
}
