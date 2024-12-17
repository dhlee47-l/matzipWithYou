package com.lec.spring.matzip.domain;

import com.lec.spring.member.domain.Member;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMatzipTagStatus {
        private Long myMatzipId;// 맛집 태그 일련 번호(가게)
        private Long memberId; // 회원 일련번호
        private Long tagId; // 태그 일련번호
        // Tag 에서 가져오기
        private String tagName;
        //kind id
        private String kindName;

        //wholeHiddenList
        private Long matzipId;
        private String visibility;
        private Long id;

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof UserMatzipTagStatus)) return false;
                UserMatzipTagStatus that = (UserMatzipTagStatus) o;
                return Objects.equals(myMatzipId, that.myMatzipId) &&// id를 기준으로 비교
                Objects.equals(id, that.id)&&
                        Objects.equals(tagId, that.tagId)
                        ;
        }

        @Override
        public int hashCode() {
                return Objects.hash(myMatzipId, id, tagId); // id를 해시 코드로 사용
        }


}// end hintPurchase


