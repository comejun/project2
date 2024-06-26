package com.react.project2.controller;

import com.react.project2.domain.MemberStatus;
import com.react.project2.domain.NoticeType;
import com.react.project2.domain.StudyMember;
import com.react.project2.dto.PageRequestDTO;
import com.react.project2.dto.PageResponseDTO;
import com.react.project2.dto.StudyDTO;
import com.react.project2.service.MemberStatusService;
import com.react.project2.service.NoticeService;
import com.react.project2.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("api/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final MemberStatusService memberStatusService;
    private final NoticeService noticeService;

    // 생성한 스터디 조건으로 목록 조회
    @GetMapping("/list/{type}/{email}")
    public PageResponseDTO<StudyDTO> listMember(
            PageRequestDTO pageRequestDTO,
            @PathVariable("type") String type,
            @PathVariable("email") String memberEmail) {
        return studyService.getListMember(type, pageRequestDTO, memberEmail);
    }

    // 참가한 스터디 조건으로  목록 조회
    @GetMapping("/memberList/{type}/{email}")
    public PageResponseDTO<StudyDTO> getJoinStudy(
            @PathVariable("type") String type,
            PageRequestDTO pageRequestDTO,
            @PathVariable("email") String email) {
        return studyService.getJoinStudy(type, pageRequestDTO, email);
    }

    // 스터디 등록
    @PostMapping("/")
    public Map<String, String> add(StudyDTO studyDTO) {
        log.info("**** StudyController POST / add {} ****", studyDTO);
        studyService.add(studyDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    // 스터디 1개 조회
    @GetMapping("/{id}")
    public StudyDTO get(@PathVariable("id") Long id) {
        log.info("스터디 id 값은 : {}", id);
        return studyService.get(id);
    }

    // 스터디 수정
    @PutMapping("/modify")
    public Map<String, String> modify(StudyDTO studyDTO) {
        studyService.modifyStudy(studyDTO);

        // 상태가 HOLD인 멤버들 알람 생성
        memberStatusService.getMemberStatusByStatus(studyDTO.getId(), MemberStatus.HOLD).forEach(email -> {
            noticeService.createNotice(studyDTO.getId(), email, false, NoticeType.STUDY_MODIFY);
        });

        // 상태가 ACCEPT인 멤버들 알람 생성
        memberStatusService.getMemberStatusByStatus(studyDTO.getId(), MemberStatus.ACCEPT).forEach(email -> {
            noticeService.createNotice(studyDTO.getId(), email, false, NoticeType.STUDY_MODIFY);
        });

        return Map.of("result", "SUCCESS");

    }

    // 스터디 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        boolean result = studyService.delete(id);
        if (result) {

            // 상태가 ACCEPT인 멤버들 알람 생성
            memberStatusService.getMemberStatusByStatus(id, MemberStatus.ACCEPT).forEach(email -> {
                noticeService.createNotice(id, email, false, NoticeType.STUDY_DELETE);
            });
            // 상태가 HOLD인 멤버들 알람 생성
            memberStatusService.getMemberStatusByStatus(id, MemberStatus.HOLD).forEach(email -> {
                noticeService.createNotice(id, email, false, NoticeType.STUDY_DELETE);
            });

            // 전체 스터디 멤버 상태 DECLINE으로 변경
            memberStatusService.changeAllMemberStatus(id, MemberStatus.DECLINE);

            // 상태가 DECLINE인 멤버들 제거
            studyService.deleteDeclineMember(id);
            return ResponseEntity.ok().body(Map.of("message", "스터디가 성공적으로 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "삭제할 스터디를 찾을 수 없습니다."));
        }
    }

    // 스터디 참가신청
    @PostMapping("/{id}/participate")
    public ResponseEntity<?> participate(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");

        // 스터디 참가 로직 구현
        boolean result = studyService.participate(id, userEmail);

        // 알람 생성
        noticeService.createNotice(id, userEmail, true, NoticeType.STUDY_PARTICIPATION);

        return ResponseEntity.ok().body(Map.of("message", "스터디 참가신청이 완료되었습니다."));
    }

    // 스터디 출석체크
    @PostMapping("/{id}/arrive")
    public ResponseEntity<?> Arrive(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");

        // 스터디 참가 로직 구현
        memberStatusService.changeMemberStatus(id, userEmail, MemberStatus.ARRIVE);
        // 알람 생성
        noticeService.createNotice(id, userEmail, true, NoticeType.ATTENDANCE_COMPLETE);
        return ResponseEntity.ok().body(Map.of("message", "스터디 출석체크가 완료되었습니다."));
    }

    // 스터디 참가 취소
    @PostMapping("/{id}/cancelParticipation")
    public ResponseEntity<?> participationCancel(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");
        // 스터디 참가 취소 로직 구현
        memberStatusService.changeMemberStatus(id, userEmail, MemberStatus.WITHDRAW);
        // 알람 생성
        noticeService.createNotice(id, userEmail, true, NoticeType.STUDY_WITHDRAWAL);
        return ResponseEntity.ok().body(Map.of("message", "스터디 참가신청이 취소되었습니다."));
    }

    // 스터디 참가 수락
    @PostMapping("/{id}/acceptJoin")
    public ResponseEntity<?> acceptJoin(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");
        // 스터디 참가 수락 로직 구현
        memberStatusService.changeMemberStatus(id, userEmail, MemberStatus.ACCEPT);
        // 알람 생성
        noticeService.createNotice(id, userEmail, false, NoticeType.STUDY_APPROVAL);
        return ResponseEntity.ok().body(Map.of("message", "스터디 참가신청이 수락되었습니다."));
    }

    // 스터디 참가 거절
    @PostMapping("/{id}/declineJoin")
    public ResponseEntity<?> declineJoin(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");
        // 스터디 참가 거절 로직 구현
        memberStatusService.changeMemberStatus(id, userEmail, MemberStatus.DECLINE);
        // 알람 생성
        noticeService.createNotice(id, userEmail, false, NoticeType.STUDY_REJECTION);
        return ResponseEntity.ok().body(Map.of("message", "스터디 참가신청이 거절되었습니다."));
    }


    // 스터디 시작
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startStudy(@PathVariable("id") Long id) {
        boolean result = studyService.startStudy(id);
        // 스터디 시작 로직 구현
        if (result) {
            // 상태가  HOLD인 멤버들 알람 생성
            memberStatusService.getMemberStatusByStatus(id, MemberStatus.HOLD).forEach(email -> {
                noticeService.createNotice(id, email, false, NoticeType.STUDY_REJECTION);
                // DECLINE 상태로 변경
                memberStatusService.changeMemberStatus(id, email, MemberStatus.DECLINE);
            });

            // 상태가 WITHDRAW인 멤버 상태 변경
            memberStatusService.changeAllMemberStatusByStatus(id, MemberStatus.WITHDRAW, MemberStatus.DECLINE);

            // 상태가 DECLINE인 멤버들 제거
            studyService.deleteDeclineMember(id);

            // 상태가 ACCEPT인 멤버들 알람 생성
            memberStatusService.getMemberStatusByStatus(id, MemberStatus.ACCEPT).forEach(email -> {
                noticeService.createNotice(id, email, false, NoticeType.STUDY_START);
            });

            return ResponseEntity.ok().body(Map.of("message", "스터디가 성공적으로 시작되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "스터디 시작 처리 중 오류가 발생했습니다."));
        }
    }

    // 스터디 지각 처리
    @PostMapping("/{id}/arriveLate")
    public ResponseEntity<?> setLate(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");
        log.info("지각체크");

        memberStatusService.changeMemberStatus(id, userEmail, MemberStatus.LATE);
        // 알람 생성
        noticeService.createNotice(id, userEmail, false, NoticeType.TARDINESS);
        return ResponseEntity.ok().body(Map.of("message", "스터디에 지각 처리되었습니다."));
    }

    // 스터디 결석 처리
    @PostMapping("/{id}/setAbsence")
    public ResponseEntity<?> setAbsence(@PathVariable("id") Long id, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("email");

        memberStatusService.changeMemberStatus(id, userEmail, MemberStatus.ABSENCE);
        // 알람 생성
        noticeService.createNotice(id, userEmail, false, NoticeType.ABSENCE);
        return ResponseEntity.ok().body(Map.of("message", "스터디에 결석 처리되었습니다."));
    }

    // 스터디 완료
    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finishedStudy(@PathVariable("id") Long id) {
        boolean result = studyService.finishedStudy(id);
        if (result) {
            noticeService.createNotice(id, "ALL", false, NoticeType.STUDY_END);
            return ResponseEntity.ok().body(Map.of("message", "스터디가 성공적으로 종료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "스터디 종료 처리 중 오류가 발생했습니다."));
        }
    }

    // ----------- //

    // 마이페이지 스터디 개수 요청
    @GetMapping("/countmy")
    public ResponseEntity<?> countMyStudies(@RequestParam String email, @RequestParam String type) {
        log.info("testCount------");
        try {
            // 사용자 이메일로 생성한 스터디 개수 조회
            int count = studyService.countStudy(type, email);
            return ResponseEntity.ok().body(Map.of("count", count));
        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "스터디 개수를 조회하는 중 오류가 발생했습니다."));
        }
    }
}
