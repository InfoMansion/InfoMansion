import { Button, styled } from "@mui/material";

export const ColorButton = styled(Button)(({ theme }) => ({
    color: '#fff',
    backgroundColor : '#fc7a71',
    '&:hover' : {
      backgroundColor : '#da6751',
    },
}));