package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.DetailMatzipDTO;
import com.lec.spring.matzip.domain.Matzip;

import java.util.List;

public interface MatzipRepository {
    boolean save(Matzip matzip);

    List<Matzip> findAll();

    Matzip findByName(String name);

    Matzip findById(Long id);

    int deleteById(Long id);

    // 지윤...
    List<String> listTagName(Long id);

    List<String> listKindName(Long id);

    DetailMatzipDTO findDetailMatzipByMatzipIdWithFriendId(Long matzipId, Long friendId);
}
