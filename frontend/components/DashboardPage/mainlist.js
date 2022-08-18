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
import { Fragment } from 'react';
import { Divider } from '@mui/material';



export default function MainListItems() {
  const [, setDashNum] = useRecoilState(dashNumState);
  const router = useRouter();

  function changeDashNumZero(event)  { setDashNum(0); }
  function changeDashNumOne(event)   { setDashNum(1); }
  function changeDashNumTwo(event)   { setDashNum(2); }
  function changeDashNumThree(event) { setDashNum(3); }
  function changePage(event) {
    Router.push("/" + router.query.userName);
  }

  return (
    <Fragment>
      <ListItemButton onClick={changeDashNumZero}>
        <ListItemIcon>
          <AccountCircleIcon color="primary"/>
        </ListItemIcon>
        <ListItemText primary="회원 정보 조회/수정" />
      </ListItemButton>
      <ListItemButton onClick={changeDashNumOne}>
        <ListItemIcon>
          <KeyIcon color="primary"/>
        </ListItemIcon>
        <ListItemText primary="비밀번호 수정" />
      </ListItemButton>
      <ListItemButton onClick={changeDashNumTwo}>
        <ListItemIcon>
          <AlarmIcon color="primary"/>
        </ListItemIcon>
        <ListItemText primary="알림 설정" />
      </ListItemButton>
      <ListItemButton onClick={changeDashNumThree}>
        <ListItemIcon>
          <LockIcon color="primary"/>
        </ListItemIcon>
        <ListItemText primary="개인 정보 및 보안" />
      </ListItemButton>

      <Divider color="white"/>

      <ListItemButton onClick={changePage}>
        <ListItemIcon>
          <ArrowBackIcon color="primary"/>
        </ListItemIcon>
        <ListItemText primary="돌아가기" />
      </ListItemButton>
    </Fragment>
  );
}
