package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.*;
import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.repository.MyMatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyMatzipServiceImpl implements MyMatzipService {


    private final MyMatzipRepository myMatzipRepository;

    public MyMatzipServiceImpl(SqlSession sqlSession) {
        this.myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
    }


    @Override
    public FindingResultMyMatzipDTO findByMemberId(Long id) {
        return FindingResultMyMatzipDTO.builder()
                .cnt(myMatzipRepository.listCntByMemberId(id))
                .list(myMatzipRepository.findAll(id))
                .build();
    }

    @Override
    public ResponseEntity<Map<String, String>> updateMyMatzipVisibility(UpdateMyMatzipVisibility updateMyMatzipVisibility) {
        if(myMatzipRepository.updateMyMatzipVisibility(updateMyMatzipVisibility.getMyMatzipId(), updateMyMatzipVisibility.getVisibility())) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg","수정에 실패했습니다."
                    ));
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteMyMatzip(Long id) {
        if(myMatzipRepository.deleteMyMatzip(id)) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg","삭제에 실패했습니다."
            ));
        }
    }

    @Override
    public SeoulMapDataDTO findSeoulMapDataById(Long id) {
        List<SeoulMapSqlDataDTO> seoulMapDBDataDTOS = myMatzipRepository.findSeoulMapData(id);
        List<FriendDataDTO> friendDataDTOS = new ArrayList<>();
        Map<String, Integer> totalPublicGuMap = new HashMap<>();
        Map<String, Integer> totalHiddenGuMap = new HashMap<>();

        seoulMapDBDataDTOS.forEach(data -> {
            List<String> publicGu = data.getPublicGu();
            List<String> hiddenGu = data.getHiddenGu();
            publicGu.removeIf(Objects::isNull);
            hiddenGu.removeIf(Objects::isNull);

            Map<String, Integer> publicGuMap = new HashMap<>();
            Map<String, Integer> hiddenGuMap = new HashMap<>();

            publicGu.forEach(e -> {
                publicGuMap.put(e, publicGuMap.getOrDefault(e, 0) + 1);
                totalPublicGuMap.put(e, totalPublicGuMap.getOrDefault(e, 0) + 1);
            });
            hiddenGu.forEach(e -> {
                hiddenGuMap.put(e, hiddenGuMap.getOrDefault(e, 0) + 1);
                totalHiddenGuMap.put(e, totalHiddenGuMap.getOrDefault(e, 0) + 1);
            });

            friendDataDTOS.add(FriendDataDTO.builder()
                            .friendId(data.getFriendId())
                            .nickname(data.getNickname())
                            .profileImg(data.getProfileImg())
                            .publicGu(publicGuMap)
                            .hiddenGu(hiddenGuMap)
                            .build());
        });

        return SeoulMapDataDTO.builder()
                .friendData(friendDataDTOS)
                .toTalData(ToTalDataDTO.builder()
                        .publicGu(totalPublicGuMap)
                        .hiddenGu(totalHiddenGuMap)
                        .build())
                .build();
    }

    @Override
    public DetailMapDataDTO findGuMapDataById(Long id, String gu) {
        List<FriendDataWithMatzipDTO> sqlResult = myMatzipRepository.findGuMapData(id, gu);
        List<TotalMatzipListDataDTO> totalMatzipList = new ArrayList<>();
        sqlResult.forEach(data -> {
            data.getMatzipList().forEach(matzip -> {
                int index = totalMatzipList.indexOf(matzip);
                if(index == -1) {
                    totalMatzipList.add(new TotalMatzipListDataDTO(matzip));
                } else {
                    totalMatzipList.get(index).getMemberIds().add(matzip.getMemberId());
                }
            });
        });

        return DetailMapDataDTO.builder()
                .friendList(sqlResult)
                .totalMatzipList(totalMatzipList)
                .build();
    }

    @Override
    public boolean saveMyMatzip(MyMatzip myMatzip) {
        return myMatzipRepository.save(myMatzip);
    }
}