import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import AlarmIcon from '@mui/icons-material/Alarm';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import KeyIcon from '@mui/icons-material/Key';
import LockIcon from '@mui/icons-material/Lock';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useRecoilState } from 'recoil';
import { dashNumState } from '../../state/dashNum';
import Router, { useRouter } from 'next/router';



export default function MainListItems() {
  const [dashNum, setDashNum] = useRecoilState(dashNumState);
  const router = useRouter();

  function changeDashNumZero(event) {
    setDashNum(0);
  }
  function changeDashNumOne(event) {
    setDashNum(1);
  }
  function changeDashNumTwo(event) {
    setDashNum(2);
  }
  function changeDashNumThree(event) {
    setDashNum(3);
  }
  function changePage(event) {
    console.log(router.query.userName);
    Router.push("/" + router.query.userName);
  }

  return (
    <React.Fragment>
      <ListItemButton onClick={changeDashNumZero}>
        <ListItemIcon>
          <AccountCircleIcon />
        </ListItemIcon>
        <ListItemText primary="회원 정보 조회/수정" />
      </ListItemButton>
      <ListItemButton onClick={changeDashNumOne}>
        <ListItemIcon>
          <KeyIcon />
        </ListItemIcon>
        <ListItemText primary="비밀번호 수정" />
      </ListItemButton>
      <ListItemButton onClick={changeDashNumTwo}>
        <ListItemIcon>
          <AlarmIcon />
        </ListItemIcon>
        <ListItemText primary="알림 설정" />
      </ListItemButton>
      <ListItemButton onClick={changeDashNumThree}>
        <ListItemIcon>
          <LockIcon />
        </ListItemIcon>
        <ListItemText primary="개인 정보 및 보안" />
      </ListItemButton>
      <ListItemButton onClick={changePage}>
        <ListItemIcon>
          <ArrowBackIcon />
        </ListItemIcon>
        <ListItemText primary="돌아가기" />
      </ListItemButton>
    </React.Fragment>
  );
}
