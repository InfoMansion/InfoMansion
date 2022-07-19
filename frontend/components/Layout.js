import HeaderNav from "./common/HeaderNav";
import { styled } from "@mui/material/styles";

const Root = styled("div")(({ theme }) => ({
  padding: theme.spacing(1),
  margin: "30px auto",

  [theme.breakpoints.down("lg")]: {
    width: "650px",
  },
  [theme.breakpoints.up("lg")]: {
    width: "1280px",
  },
}));

export default function Mainframe({ children }) {
  return (
    <>
      <HeaderNav />
      {/* 
                mainpage를 index.js에 작성.
                roompage를 (상대방의) 룸 페이지로 사용.
                아직 미제작이지만 Mypage를 나의 룸페이지로 사용.
            */}
      {/* 이 밑의 자식을 room, mainpage로 바꿔가면서 동작하기 */}
      <Root>{children}</Root>
    </>
  );
}
