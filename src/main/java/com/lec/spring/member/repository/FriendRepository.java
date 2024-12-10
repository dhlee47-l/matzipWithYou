package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository{

    // 친구 신청시 이미 보낸 신청은 없나 확인용
    boolean isAlreadyFriend(Friend friend);

    // 친구 목록 조회 (친구와 맛집 공개 상태 정보 포함)
    List<Friend> findFriendsWithDetailsDTO(Long memberId);

    // 대기 중인 요청 목록 조회
    List<Friend> findPendingRequests(Long memberId);

    // 친구 요청 수락
    int acceptFriendRequest(Friend friend);
//    int update(Friend friend);

    // 친구 요청 거절 & 친삭
    int rejectFriendRequest(Friend friend);


    // 친구 요청 보내기
    int sendFriendRequest(Friend friend);

    // 친구 요청 DB에 저장
    int save(Friend friend);

//
//    // 친구 삭제
//    int delete(Friend friend);
//    int deleteFriend(Friend friend);

//    // 친밀도 조회
//    int getIntimacy(Friend friend);
//
//    // 친밀도 업데이트
//    int updateIntimacy(Friend friend);

}