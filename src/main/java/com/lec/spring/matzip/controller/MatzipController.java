package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.DTO.DetailMapDataDTO;
import com.lec.spring.matzip.domain.DTO.DetailMatzipDTO;
import com.lec.spring.matzip.domain.DTO.MatzipDTO;
import com.lec.spring.matzip.domain.DTO.SeoulMapDataDTO;
import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.service.FoodKindService;
import com.lec.spring.matzip.service.MatzipService;
import com.lec.spring.matzip.service.MyMatzipService;
import com.lec.spring.matzip.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/matzips")
public class MatzipController {

    private final MatzipService matzipService;
    private final MyMatzipService myMatzipService;
    private final FoodKindService foodKindService;

    public MatzipController(MatzipService matzipService, MyMatzipService myMatzipService, FoodKindService foodKindService) {
        this.matzipService = matzipService;
        this.myMatzipService = myMatzipService;
        this.foodKindService = foodKindService;
    }

    @ResponseBody
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> saveMatZip(@RequestBody MatzipDTO matzipDTO) {
        return matzipService.saveMatzip((Matzip) matzipDTO);
    }

    @GetMapping("/{memberId}")
    public String seoulMapMatzip(@PathVariable("memberId") Long memberId, Model model) {
        SeoulMapDataDTO result = myMatzipService.findSeoulMapDataById(memberId);
        model.addAttribute("data", result);
        return "matzip/seoul-map";
    }

    @GetMapping("/homework/{id}")
    public String getHomework(@PathVariable Long id, Model model) {
        Matzip matzip = matzipService.getMatzipById(id);
        List<String> tagName = matzipService.listTagName(id);
        List<String> kindName = matzipService.listKindName(id);
        model.addAttribute("kindName", kindName);
        model.addAttribute("tagName", tagName);
        model.addAttribute("matzip", matzip);
        return "matzip/detail";
    }

    @GetMapping("/{memberId}/{gu}")
    public String getGuMapData(@PathVariable String gu, @PathVariable Long memberId, Model model) {
        DetailMapDataDTO result = myMatzipService.findGuMapDataById(memberId, gu);
        model.addAttribute("data", result);
        return "matzip/gu-detail-map";
    }

    @ResponseBody
    @GetMapping("/detail/{matzipId}")
    public ResponseEntity<DetailMatzipDTO> detail(@PathVariable Long matzipId, @RequestParam Long friendId) {
        return matzipService.getDetailMatzip(matzipId, friendId);
    }

    @ResponseBody
    @GetMapping("/food-kinds")
    public ResponseEntity<List<FoodKind>> getTags() {
        return ResponseEntity.ok(foodKindService.getAllFoodKinds());
    }

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<Matzip> getMatzip(@RequestParam Long matzipId) {
        return ResponseEntity.ok(matzipService.getMatzipById(matzipId));
    }
}
