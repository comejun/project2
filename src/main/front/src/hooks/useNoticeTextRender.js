import React from "react";

const UseNoticeTextRender = () => {
  // *************** 실제 렌더링할 알람 텍스트 함수 ***************

  // 스터디 삭제 알람 텍스트
  const renderStudyDeleteNoticeText = (notice) => {
    return <h3>{notice.studyTitle}가 삭제 되었습니다.</h3>;
  };

  // 스터디 수정 알람 텍스트
  const renderStudyModifyNoticeText = (notice) => {
    return <h3>{notice.studyTitle}가 수정 되었습니다.</h3>;
  };

  // 스터디 참가신청 알람 텍스트
  const renderStudyParticipationNoticeText = (notice) => {
    return (
      <h3>
        {notice.memberNickname} 님이 {notice.studyTitle}에 참가 신청 하였습니다.
      </h3>
    );
  };

  // 스터디 탈퇴 알람 텍스트
  const renderStudyWithdrawalNoticeText = (notice) => {
    return (
      <h3>
        {notice.studyTitle}에서 {notice.memberNickname}님이 탈퇴 하였습니다.
      </h3>
    );
  };

  // 스터디 참가승인 알람 텍스트
  const renderStudyApprovalNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 참가 신청이 승인 되었습니다.</h3>;
  };

  // 스터디 참가거절 알람 텍스트
  const renderStudyRejectionNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 참가 신청이 거절 되었습니다.</h3>;
  };

  // 스터디 시작 알람 텍스트
  const renderStudyStartNoticeText = (notice) => {
    return <h3>{notice.studyTitle}가 시작 되었습니다.</h3>;
  };

  // 스터디 종료 알람 텍스트
  const renderStudyEndNoticeText = (notice) => {
    return <h3>{notice.studyTitle}가 종료 되었습니다.</h3>;
  };

  // 스터디 방치 알람 텍스트
  const renderStudyDeadNoticeText = (notice) => {
    return <h3>{notice.studyTitle}가 취소 되었습니다.</h3>;
  };

  // 참가일 1일전 알람 텍스트
  const renderPreParticipationDateNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 참가일이 1일 남았습니다.</h3>;
  };

  // 참가일 당일 알람 텍스트
  const renderParticipationDateNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 참가일이 오늘 입니다.</h3>;
  };

  // 출석 완료 알람 텍스트
  const renderAttendanceCompleteNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 출석이 완료 되었습니다.</h3>;
  };

  // 지각 알람 텍스트
  const renderTardinessNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 지각 하였습니다.</h3>;
  };

  // 결석 알람 텍스트
  const renderAbsenceNoticeText = (notice) => {
    return <h3>{notice.studyTitle} 결석 하였습니다.</h3>;
  };

  // 회원 정지 알람 텍스트
  const renderBlackUserNoticeText = (notice) => {
    return <h3>회원님의 활동이 3일 동안 정지 되었습니다.</h3>;
  };

  // 벌점 알람 텍스트
  const renderPenaltyNoticeText = (notice) => {
    return <h3>규칙 위반 으로 인해 벌점이 추가 되었습니다.</h3>;
  };

  // 벌점 삭제 알람 텍스트
  const renderPenaltyDeleteNoticeText = (notice) => {
    return <h3>회원님의 활동 정지가 해제되었습니다.</h3>;
  };

  return {
    renderStudyDeleteNoticeText,
    renderStudyModifyNoticeText,
    renderStudyParticipationNoticeText,
    renderStudyWithdrawalNoticeText,
    renderStudyApprovalNoticeText,
    renderStudyRejectionNoticeText,
    renderStudyStartNoticeText,
    renderStudyEndNoticeText,
    renderStudyDeadNoticeText,
    renderPreParticipationDateNoticeText,
    renderParticipationDateNoticeText,
    renderAttendanceCompleteNoticeText,
    renderTardinessNoticeText,
    renderAbsenceNoticeText,
    renderBlackUserNoticeText,
    renderPenaltyNoticeText,
    renderPenaltyDeleteNoticeText,
  };
};
export default UseNoticeTextRender;
