import { Box, Button, IconButton, Menu, MenuItem } from "@mui/material";
import Link from "next/link";
import { useState } from "react";
import MenuIcon from '@mui/icons-material/Menu';
import AddIcon from '@mui/icons-material/Add';
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
            <Link href="/Shop">
                <IconButton 
                    size="large"
                    style={{ color : '#9e9e9e'}}
                >
                    <ShoppingCartIcon />
                </IconButton>
            </Link>
            <IconButton
                id="basic-button"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-expanded={open ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
                style={{color : '#9e9e9e'}}
                size="large"
            >
                <MenuIcon />
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <MenuItem onClick={handleClose}>
                    <Link href={userID + "/RoomEdit"}>
                        방 편집
                    </Link>
                </MenuItem>
                <MenuItem onClick={handleClose}>Post 관리</MenuItem>
                <MenuItem onClick={handleClose}>방명록 관리</MenuItem>
            </Menu>
            <Link href="/post/CreatePost">
              <IconButton
                baseClassName="fas"
                className="fa-plus-circle"
                size="large"
                color="inherit"
                style={{ color: '#9e9e9e' }}
              >
                <AddIcon />
              </IconButton>
            </Link>
        </Box>
    )
}