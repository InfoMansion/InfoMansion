import { Box, Button, Menu, MenuItem } from "@mui/material";
import Link from "next/link";
import { useState } from "react";
import MenuIcon from '@mui/icons-material/Menu';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useRouter } from "next/router";

export default function ManageButton({userID}) {    
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
      setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
      setAnchorEl(null);
    };
    // 이거 버튼 아니고 나중에는 오픈형 메뉴로 바뀌어야 합니다.
    return (
        <Box
            sx={{display : 'flex'}}
        >
            <Button sx={{color : '#111111'}}>
                <Link href="/Shop">
                    <ShoppingCartIcon />
                </Link>
            </Button>
            <Button
                id="basic-button"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-expanded={open ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
            >
                <MenuIcon />
            </Button>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                'aria-labelledby': 'basic-button',
                }}
                // 이거 왜 색깔 안바뀔까요?
                sx={{color : 'black'}}
            >
                <MenuItem onClick={handleClose}>
                    <Link href={userID + "/RoomEdit"}>
                        방 편집
                    </Link>
                </MenuItem>
                <MenuItem onClick={handleClose}>Post 관리</MenuItem>
                <MenuItem onClick={handleClose}>방명록 관리</MenuItem>
            </Menu>
        </Box>
    )
}