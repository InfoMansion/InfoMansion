import Change from './change';
import Confirm from './confirm';
import { useRecoilState } from 'recoil';
import { pwConfirmState } from '../../../state/pwConfirm';

export default function PasswordChange() {
  const [pwConfirm, setPwConfirmState] = useRecoilState(pwConfirmState);

  let content;
  if (pwConfirm) {
    content = <Change></Change>;
  } else {
    content = <Confirm></Confirm>;
  }

  return <>{content}</>;
}
