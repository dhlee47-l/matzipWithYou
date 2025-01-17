SET SESSION FOREIGN_KEY_CHECKS = 0;

/* Drop Tables */
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS food_kind;
DROP TABLE IF EXISTS friend;
DROP TABLE IF EXISTS matzip;
DROP TABLE IF EXISTS matzip_tag;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_authorities;
DROP TABLE IF EXISTS my_matzip;
DROP TABLE IF EXISTS my_review;
DROP TABLE IF EXISTS profile_img;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS user_matzip_tag_status;
DROP TABLE IF EXISTS wish_list;
DROP TABLE IF EXISTS review_tag;

CREATE TABLE authority
(
    id   INT         NOT NULL AUTO_INCREMENT COMMENT '권한 일련번호',
    name VARCHAR(15) NOT NULL COMMENT '권한명',
    PRIMARY KEY (id)
) COMMENT '권한';

ALTER TABLE authority
    ADD CONSTRAINT uq_name UNIQUE (name);

CREATE TABLE food_kind
(
    id       INT         NOT NULL AUTO_INCREMENT COMMENT '음식 종류 일련번호',
    kindname VARCHAR(30) NOT NULL COMMENT '종류',
    regdate  DATETIME    NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (id)
) COMMENT '음식 종류';

ALTER TABLE food_kind
    ADD CONSTRAINT uq_kindname UNIQUE (kindname);

CREATE TABLE friend
(
    sender_id   INT      NOT NULL COMMENT '요청 보낸 회원 일련번호',
    receiver_id INT      NOT NULL COMMENT '요청 받은 회원 일련번호',
    intimacy    INT      NULL DEFAULT 0 COMMENT '친밀도',
    is_accept   BOOLEAN  NULL DEFAULT FALSE COMMENT '수락여부',
    regdate     DATETIME NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (sender_id, receiver_id)
) COMMENT '친구';

CREATE TABLE matzip
(
    id            INT         NOT NULL AUTO_INCREMENT COMMENT '맛집 일련번호',
    kind_id       INT         NULL COMMENT '음식 종류 일련번호',
    address       TEXT        NOT NULL COMMENT '주소',
    lat           DOUBLE      NOT NULL COMMENT '위도',
    lng           DOUBLE      NOT NULL COMMENT '경도',
    img_url       TEXT        NOT NULL COMMENT '가게 이미지 url',
    gu            VARCHAR(5)  NOT NULL COMMENT '지역구',
    kakao_map_url TEXT        NOT NULL COMMENT '카카오맵 상세페이지 url',
    name          VARCHAR(50) NOT NULL COMMENT '가게 이름',
    regdate       DATETIME    NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (id)
) COMMENT '맛집';

CREATE TABLE matzip_tag
(
    tag_id       INT      NOT NULL COMMENT '태그 일련번호',
    my_matzip_id INT      NOT NULL COMMENT '나의 맛집 일련번호',
    regdate      DATETIME NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (tag_id, my_matzip_id)
) COMMENT '나의 맛집태그';

CREATE TABLE member
(
    id       INT          NOT NULL AUTO_INCREMENT COMMENT '회원 일련번호',
    username VARCHAR(100) NOT NULL COMMENT '아이디 (8~20)',
    password VARCHAR(100) NOT NULL COMMENT '비밀번호(8~16)',
    email    VARCHAR(100) NOT NULL COMMENT '이메일',
    point    INT          NULL DEFAULT 0 COMMENT '포인트',
    nickname VARCHAR(100) NOT NULL COMMENT '닉네임',
    name     VARCHAR(20)  NOT NULL COMMENT '이름',
    PRIMARY KEY (id)
) COMMENT '회원';

ALTER TABLE member
    ADD CONSTRAINT uq_username UNIQUE (username);

ALTER TABLE member
    ADD CONSTRAINT uq_nickname UNIQUE (nickname);

CREATE TABLE member_authorities
(
    authorities_id INT NOT NULL COMMENT '권한 일련번호',
    member_id      INT NOT NULL COMMENT '회원 일련번호',
    PRIMARY KEY (authorities_id, member_id)
) COMMENT '권한 리스트';

CREATE TABLE my_matzip
(
    id          INT                                NOT NULL AUTO_INCREMENT COMMENT '나의 맛집 일련번호',
    matzip_id   INT                                NOT NULL COMMENT '맛집 일련번호',
    member_id   INT                                NOT NULL COMMENT '회원 일련번호',
    regdate     DATETIME                           NULL DEFAULT NOW() COMMENT '등록일',
    visibility  ENUM ('PRIVATE','PUBLIC','HIDDEN') NULL DEFAULT 'PUBLIC' COMMENT '공개여부',
    content     TEXT                               NOT NULL COMMENT '리뷰',
    star_rating INT                                NULL DEFAULT 1 COMMENT '리뷰 점수',
    PRIMARY KEY (id)
) COMMENT '나의 맛집';

CREATE TABLE my_review
(
    id          INT      NOT NULL AUTO_INCREMENT COMMENT '나의 리뷰 일련번호',
    matzip_id   INT      NOT NULL COMMENT '맛집 일련번호',
    member_id   INT      NOT NULL COMMENT '회원 일련번호',
    content     TEXT     NOT NULL COMMENT '리뷰',
    regdate     DATETIME NULL DEFAULT NOW() COMMENT '등록일',
    star_rating INT      NULL DEFAULT 1 COMMENT '리뷰 점수',
    PRIMARY KEY (id)
) COMMENT '나의 리뷰';

CREATE TABLE profile_img
(
    id         INT  NOT NULL AUTO_INCREMENT COMMENT '프로필 사진 일련번호',
    member_id  INT  NOT NULL COMMENT '회원 일련번호',
    sourcename TEXT NOT NULL COMMENT '원본 이름',
    filename   TEXT NOT NULL COMMENT '저장 이름',
    PRIMARY KEY (id)
) COMMENT '프로필 이미지';

CREATE TABLE review_tag
(
    tag_id       INT      NOT NULL COMMENT '태그 일련번호',
    my_review_id INT      NOT NULL COMMENT '나의 리뷰 일련번호',
    regdate      DATETIME NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (tag_id, my_review_id)
) COMMENT '나의 리뷰 태크';

CREATE TABLE tag
(
    id      INT         NOT NULL AUTO_INCREMENT COMMENT '태그 일련번호',
    tagname VARCHAR(30) NOT NULL COMMENT '태그',
    regdate DATETIME    NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (id)
) COMMENT '태그';

ALTER TABLE tag
    ADD CONSTRAINT uq_tagname UNIQUE (tagname);

CREATE TABLE user_matzip_tag_status
(
    member_id    INT      NOT NULL COMMENT '회원 일련번호',
    my_matzip_id INT      NOT NULL COMMENT '나의 맛집 일련번호',
    tag_id       INT      NOT NULL COMMENT '태그 일련번호',
    regdate      DATETIME NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (member_id, my_matzip_id, tag_id)
) COMMENT '맛집 태그와 회원 관계';

CREATE TABLE wish_list
(
    member_id INT      NOT NULL COMMENT '회원 일련번호',
    matzip_id INT      NOT NULL COMMENT '맛집 일련번호',
    regdate   DATETIME NULL DEFAULT NOW() COMMENT '등록일',
    PRIMARY KEY (member_id, matzip_id)
) COMMENT '위시리스트';

ALTER TABLE friend
    ADD CONSTRAINT fk_member_to_friend
        FOREIGN KEY (sender_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE friend
    ADD CONSTRAINT fk_member_to_friend1
        FOREIGN KEY (receiver_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE matzip
    ADD CONSTRAINT fk_food_kind_to_matzip
        FOREIGN KEY (kind_id)
            REFERENCES food_kind (id);

ALTER TABLE member_authorities
    ADD CONSTRAINT fk_authority_to_member_authorities
        FOREIGN KEY (authorities_id)
            REFERENCES authority (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE member_authorities
    ADD CONSTRAINT fk_member_to_member_authorities
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE my_matzip
    ADD CONSTRAINT fk_matzip_to_my_matzip
        FOREIGN KEY (matzip_id)
            REFERENCES matzip (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE my_matzip
    ADD CONSTRAINT fk_member_to_my_matzip
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE my_review
    ADD CONSTRAINT fk_matzip_to_my_review
        FOREIGN KEY (matzip_id)
            REFERENCES matzip (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE my_review
    ADD CONSTRAINT fk_member_to_my_review
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE profile_img
    ADD CONSTRAINT fk_member_to_profile_img
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE user_matzip_tag_status
    ADD CONSTRAINT fk_member_to_user_matzip_tag_status
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE wish_list
    ADD CONSTRAINT fk_member_to_wish_list
        FOREIGN KEY (member_id)
            REFERENCES member (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE wish_list
    ADD CONSTRAINT fk_matzip_to_wish_list
        FOREIGN KEY (matzip_id)
            REFERENCES matzip (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;



ALTER TABLE user_matzip_tag_status
    ADD CONSTRAINT fk_matzip_tag_to_user_matzip_tag_status
        FOREIGN KEY (tag_id, my_matzip_id)
            REFERENCES matzip_tag (tag_id, my_matzip_id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE review_tag
    ADD CONSTRAINT fk_tag_to_review_tag
        FOREIGN KEY (tag_id)
            REFERENCES tag (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE review_tag
    ADD CONSTRAINT fk_my_review_to_review_tag
        FOREIGN KEY (my_review_id)
            REFERENCES my_review (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE matzip_tag
    ADD CONSTRAINT fk_tag_to_matzip_tag
        FOREIGN KEY (tag_id)
            REFERENCES tag (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE matzip_tag
    ADD CONSTRAINT fk_my_matzip_to_matzip_tag
        FOREIGN KEY (my_matzip_id)
            REFERENCES my_matzip (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE matzip
    DROP FOREIGN KEY FK_food_kind_TO_matzip;

ALTER TABLE matzip
    ADD CONSTRAINT FK_food_kind_TO_matzip
        FOREIGN KEY (kind_id)
            REFERENCES food_kind (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

alter table member
    add column provider varchar(40);

alter table member
    add column providerId varchar(200);