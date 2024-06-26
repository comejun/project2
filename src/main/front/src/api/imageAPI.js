import { API_SERVER_HOST } from "./memberAPI";
import jwtAxios from "../util/jwtUtil";

const host = `${API_SERVER_HOST}/api/image`;

// 이미지 업로드 요청
export const uploadImage = async (image) => {
  console.log("이미지 업로드API");
  // 헤더 추가
  const header = { headers: { "Content-Type": "multipart/form-data" } };
  const response = await jwtAxios.post(`${host}/upload`, image, header);
  return response.data;
};

export const deleteImage = async (image) => {
  console.log("이미지 삭제API");
  const response = await jwtAxios.delete(`${host}/delete/${image}`);
  return response.data;
};
