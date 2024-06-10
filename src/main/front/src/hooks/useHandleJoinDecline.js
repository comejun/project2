import { useSelector } from "react-redux";
import jwtAxios from "../util/jwtUtil";
import { API_SERVER_HOST } from "../api/studyAPI";

const useHandleJoinDecline = () => {
  const loginState = useSelector((state) => state.loginSlice);
  const userEmail = loginState.email;
  const host = API_SERVER_HOST;

  const handleJoinDecline = async (studyId, memberEmail) => {
    if (userEmail) {
      const confirmCancel = window.confirm(`"${memberEmail}"님의 참가를 거절하시겠습니까?`);
      if (confirmCancel) {
        try {
          const response = await jwtAxios.post(`${host}/api/study/${studyId}/declineJoin`, {
            email: memberEmail,
          });
          console.log(response.data);
        } catch (error) {
          console.error(error);
        }
      }
    }
  };

  return { handleJoinDecline };
};

export default useHandleJoinDecline;