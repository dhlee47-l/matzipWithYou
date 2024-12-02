package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    // 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        friendService.sendFriendRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    // 친구 요청 목록 가져오기
    @GetMapping("/pending/{receiverId}")
    public ResponseEntity<List<Friend>> getPendingRequests(@PathVariable Long receiverId) {
        List<Friend> pendingRequests = friendService.getPendingRequests(receiverId);
        return ResponseEntity.ok(pendingRequests);
    }

    @PostMapping("/response")
    public ResponseEntity<Void> respondToRequest(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam Boolean accept) {
        friendService.respondToRequest(senderId, receiverId, accept);
        return ResponseEntity.ok().build();
    }

    // 친구 목록 가져오기
    @GetMapping("/list/{id}")
    public ResponseEntity<List<Friend>> getFriends(@PathVariable Long id) {
        List<Friend> friends = friendService.getFriends(id);
        return ResponseEntity.ok(friends);
    }
}

// 친구요청 보내는 html 의 script 에 들어갈 JS
// // Query Parameter 방식
//async function sendFriendRequest(senderId, receiverId) {
//    const response = await fetch(/api/friend/request?senderId=${senderId}&receiverId=${receiverId}, {
//        method: 'POST',
//    });
//
//    if (response.ok) {
//        console.log('Friend request sent successfully');
//    } else {
//        console.error('Failed to send friend request');
//    }
//}