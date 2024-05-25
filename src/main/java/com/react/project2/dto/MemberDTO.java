package com.react.project2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.react.project2.domain.Category;
import com.react.project2.domain.notice;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDTO extends User {

    private String email;
    private String password;
    private String nickname;
    private Long phone;
    private String profileImg;
    private String memberLink;
    private String introduction;
    private boolean disabled;
    private List<Category> favoriteList;
    private List<notice> noticeList;
    private int penalty;
    private LocalDateTime blockedDate;
    private LocalDateTime createdDate;
    private LocalDateTime disabledDate;
    private String role;

    // 생성자
    public MemberDTO(String email, String password, String nickname, Long phone, String profileImg, String memberLink,
                     String introduction, boolean disabled, List<Category> favoriteList, List<notice> noticeList, int penalty,
                     LocalDateTime blockedDate, LocalDateTime createdDate, LocalDateTime disabledDate, String role) {
        super(email, password, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImg = profileImg;
        this.memberLink = memberLink;
        this.introduction = introduction;
        this.disabled = disabled;
        this.favoriteList = favoriteList;
        this.noticeList = noticeList;
        this.penalty = penalty;
        this.role = role;
    }

    // 현재 사용자 정보를 Map 타입으로 리턴 : JWT 를 위한 메서드 -> 추후 JWT 문자열 생성시 사용
    // MemberDTO -> Map<String,Object> 타입으로 변환해서 리턴
    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password); // 비번은 나중에 전달 안하는 것으로 변경. 지금은 확인차 추가
        map.put("nickname", nickname);
        map.put("phone", phone);
        map.put("profileImg", profileImg);
        map.put("memberLink", memberLink);
        map.put("introduction", introduction);
        map.put("disabled", disabled);
        map.put("penalty", penalty);
        map.put("blockedDate", blockedDate);
        map.put("createdDate", createdDate);
        map.put("disabledDate", disabledDate);
        map.put("role", role);
        return map;
    }

}