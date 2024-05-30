package com.react.project2.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMember {

    // TODO : StudyMember Entity 수정

    private String title; // 제목

    private LocalDateTime createdDate; // 생성 날짜

    private boolean checked; // 확인 여부

    private String category; // 카테고리

    private String boardId; // 게시판 ID

    private String boardWriter; // 게시판 작성자

    private String boardWriterProfileImg; // 게시판 작성자 프로필 이미지
}