package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.WishList;
import com.lec.spring.matzip.service.MyMatzipService;
import com.lec.spring.matzip.service.WishListService;
import com.lec.spring.member.domain.ProfileImg;
import com.lec.spring.member.service.ProfileImgService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@Controller // @RestController 대신 @Controller 사용
@RequestMapping("/matzips/wish-list/{memberId}")
public class WishListController {
    private final WishListService wishListService;
    private final ProfileImgService profileImgService;

    public WishListController(WishListService wishListService, ProfileImgService profileImgService) {
        this.wishListService = wishListService;
        this.profileImgService = profileImgService;
    }

    @GetMapping("")
    public String getWishList(@PathVariable Long memberId, Model model) {
        Map<String, Object> response = wishListService.findWishListByMemberId(memberId).getBody();
        ProfileImg profileImg = profileImgService.getMemberProfileImg(memberId);
        model.addAttribute("profileImg", profileImg);
        model.addAttribute("wishList", response.get("data")); // "data"를 모델에 추가
        model.addAttribute("memberId", memberId); // memberId를 모델에 추가
        return "matzip/wish-list"; // Thymeleaf 템플릿 이름
    }

    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> getWishListApi(@PathVariable Long memberId, Model model) {
        return wishListService.findWishListByMemberId(memberId);
    }




    @PostMapping("")
    public ResponseEntity<Map<String, String>> addWishList(@PathVariable Long memberId, @RequestBody WishList wishList) {
        wishList.setMemberId(memberId);
        return wishListService.add(wishList);
    }

    @DeleteMapping("/{matzipId}")
    public ResponseEntity<Map<String, String>> deleteWishList(@PathVariable Long memberId, @PathVariable Long matzipId) {
        WishList wishList = WishList.builder()
                .memberId(memberId)
                .matzipId(matzipId)
                .build();
        return wishListService.delete(wishList);
    }


}
