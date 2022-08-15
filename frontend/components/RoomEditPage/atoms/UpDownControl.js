import { Box, Grid, Input, Slider } from '@mui/material';
import { useRecoilState } from 'recoil';
import { positionState, rotationState } from '../../../state/editRoomState';

export default function UpDownControl({part, tag, index, limit}) {
    const [data, setData] = useRecoilState(part == "position" ? positionState : rotationState);
    
    function setNewVal(val) {
        let copydata = [...data];
        copydata[index] = Number(val);
        if(copydata[index] > limit) copydata[index] = limit;
        else if(copydata[index] < 0) copydata[index] = 0;

        setData(copydata);
    }

    const handleInputChange = (event) => {
        setNewVal(event.target.value);       
    };
    const handleSliderChange = (event, newValue) => {
        setNewVal(newValue);
    };

    return (
        <Box style={{
            display : 'flex',
        }}>
            <Grid container spacing={2} alignItems="center">
                <Grid item xs={3} minWidth={80}>
                    {tag}
                </Grid>
                <Grid item xs>
                    <Slider
                        value={typeof data[index] === 'number' ? data[index] : 0}
                        onChange={handleSliderChange}
                        aria-labelledby="input-slider"
                        step={0.01}
                        min={0}
                        max={limit}
                    />
                    </Grid>
                <Grid item
                    sx={{width : 60}}
                >
                    <Input
                        value={data[index]}
                        size="small"
                        onChange={handleInputChange}
                        inputProps={{
                            step: 0.01,
                            min: 0,
                            max: limit,
                            type: 'number',
                            'aria-labelledby': 'input-slider',
                        }}
                    />
                </Grid>
            </Grid>
        </Box>
    )
}