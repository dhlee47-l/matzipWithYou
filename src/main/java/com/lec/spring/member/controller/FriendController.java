package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members/{memberId}/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    // 친구 요청 보내기 / 비동기 팝업
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<String> sendFriendRequest(

            @RequestBody  Friend friend) {
        int result = friendService.sendFriendRequest(friend);
        if (result > 0) {
            return ResponseEntity.ok("친구 신청에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("친구 신청에 실패했습니다. 이미 친구거나 수락 대기중입니다.");
        }
    }

    // 친구 요청 목록 가져오기 / 비동기 팝업
    @ResponseBody
    @GetMapping("/pending")
    public ResponseEntity<List<Friend>> getPendingRequests(@PathVariable Long memberId) {
        List<Friend> pendingRequests = friendService.getPendingRequests(memberId);
        return ResponseEntity.ok(pendingRequests);
    }

    // 요청 수락/거절 (update) / 비동기 팝업
    @ResponseBody
    @PatchMapping("")
    public ResponseEntity<Integer> respondToRequest(
            @RequestBody Friend friend,
            @PathVariable Long memberId
    ) {
        friend.setReceiverId(memberId);
        int affectedRows = friendService.respondToRequest(friend);
        return ResponseEntity.ok(affectedRows);
    }

    // 친구 삭제
    @ResponseBody
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteFriend(
            @RequestBody Friend friend,
            @PathVariable Long memberId
    ){
        friend.setReceiverId(memberId);
        int affectedRows = friendService.deleteFriend(friend);
        return ResponseEntity.ok(affectedRows);
    }


    @GetMapping("")
    public String showFriendList(@PathVariable Long memberId, Model model) {
        model.addAttribute("memberId", memberId);
        return "member/friend/friend_list";
    }

    // 친구 목록 데이터를 반환하는 엔드포인트
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<List<FriendDetailsDTO>> getFriendsList(@PathVariable Long memberId) {
        try {
            List<FriendDetailsDTO> friends = friendService.getFriendsWithDetailsDTO(memberId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}