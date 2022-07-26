import { Button } from "@mui/material";

export default function ManageButton() {
    
    function Click() {
        console.log("버튼 클릭");
    }
// 이거 버튼 아니고 나중에는 오픈형 메뉴로 바뀌어야 합니다.
    return (
        <Button onClick={Click}>
            편집.
        </Button>
    )
}