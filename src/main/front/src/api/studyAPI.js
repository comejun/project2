import axios from "axios";
import jwtAxios from "../util/jwtUtil";
import { API_SERVER_HOST } from "./memberAPI";

const host = `${API_SERVER_HOST}/api/study`;

// API 등록 요청
export const postAdd = async (study) => {
  const header = { headers: { "Content-Type": "multipart/form-data" } };
  const response = await jwtAxios.post(`${host}/`, study, header);
  return response.data;
};

// API 스터디 조회 요청
export const getOne = async (id) => {
  console.log(`${host}/${id}`);
  const response = await axios.get(`${host}/${id}`);
  console.log(response.data);
  return response.data;
};

// API 스터디 수정 요청
export const modifyStudy = async (study) => {
  console.log(`${host}/modify`);
  const header = { headers: { "Content-Type": "multipart/form-data" } };
  const response = await jwtAxios.put(`${host}/modify`, study, header);
  return response.data;
};

// API 생성한 스터디 개수 요청
export const fetchMyStudyCount = async (userEmail, type) => {
  try {
    const response = await jwtAxios.get(`${host}/countmy`, {
      params: {
        type: type,
        email: userEmail,
      },
    });
    console.log("스터디 개수", response.data.count);
    return response.data.count;
  } catch (error) {
    console.error("스터디 개수를 가져오는데 실패했습니다.", error);
    return 0;
  }
};

// 주최스터디 목록조회 요청
export const getCreatedStudyList = async (type, pageParam, email) => {
  console.log("getCreatedStudyList", type, pageParam, email);
  const { page, size } = pageParam;
  const response = await jwtAxios.get(`${host}/list/${type}/${email}`, {
    params: { page, size }, // 여기서 page와 size를 동적으로 설정
  });
  console.log("getCreatedStudyList", response.data);
  return response.data;
};

// API 참가스터디 목록조회 요청
export const getJoinStudyList = async (type, pageParam, email) => {
  const { page, size } = pageParam;
  const response = await jwtAxios.get(`${host}/memberList/${type}/${email}`, {
    params: { page, size }, // 여기서 page와 size를 동적으로 설정
  });
  return response.data;
};
