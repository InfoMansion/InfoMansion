import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Box, IconButton, Input, Typography } from '@mui/material';
import { useRecoilState } from 'recoil';
import { positionState, rotationState } from '../../../state/editRoomState';


export default function UpDownControl({part, tag, index, limit}) {
    const [data, setData] = useRecoilState(part == "position" ? positionState : rotationState);
    function onChange(e) {
        let copydata = [...data];
        const input  = e.target.value;

        if(input >= limit) copydata[index] = limit;
        else if (input < 0) copydata[index] = 0;
        else if(!input) copydata[index] = 0;
        else copydata[index] = Number(e.target.value);


        setData(copydata);
    }
    function up() {
        let copydata = [...data];
        copydata[index] = Number((Number(copydata[index]) + 0.1).toFixed(2));
        if(copydata[index] > limit) return;

        setData(copydata);
    }

    function down() {
        let copydata = [...data];
        copydata[index] = Number((Number(copydata[index]) - 0.1).toFixed(2));
        if(copydata[index] < 0) return;
        
        setData(copydata);
    }

    return (
        <Box style={{
            display : 'flex',
            flexDirection : 'column',
            width : '40px'

        }}>
            <IconButton 
                size="large"
                style={{ color : '#9e9e9e'}}
                onClick={up}
            >
                <ExpandLessIcon />
            </IconButton>
            <Box >
                <Input type='number'
                    onChange={e => onChange(e)} 
                    value={data[index]}
                />
                <Typography>
                    {tag}
                </Typography>
            </Box>
            <IconButton 
                size="large"
                style={{ color : '#9e9e9e'}}
                onClick={down}
            >
                <ExpandMoreIcon/>
            </IconButton>
        </Box>
    )
}