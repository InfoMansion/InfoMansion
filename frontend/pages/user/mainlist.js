import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import AlarmIcon from '@mui/icons-material/Alarm';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import KeyIcon from '@mui/icons-material/Key';
import FaceIcon from '@mui/icons-material/Face';
import LockIcon from '@mui/icons-material/Lock';
import { useRecoilState } from 'recoil';
import { dashNumState } from '../../state/dashNum';

export default function MainListItems() {
  const [dashNum, setDashNum] = useRecoilState(dashNumState);

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
  function changeDashNumFour(event) {
    setDashNum(4);
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
      <ListItemButton onClick={changeDashNumFour}>
        <ListItemIcon>
          <FaceIcon />
        </ListItemIcon>
        <ListItemText primary="자기소개" />
      </ListItemButton>
    </React.Fragment>
  );
}
