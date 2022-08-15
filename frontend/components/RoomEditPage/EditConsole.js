import {Box, Button, Divider, Typography} from '@mui/material'
import { useState } from 'react';
import { useRecoilState } from 'recoil'
import { categoryState, editingState, editStuffState, fromState, positionState, rotationState } from '../../state/editRoomState'
import { ColorButton } from '../common/ColorButton';
import UpDownControl from './atoms/UpDownControl';

export default function EditConsole({addLocateStuff, addUnlocatedStuff, deleteUnlocatedStuff}) {
    const [editStuff, setEditStuff] = useRecoilState(editStuffState);
    const [editposition, setEditPosition] = useRecoilState(positionState);
    const [editrotation, setEditRotation] = useRecoilState(rotationState);

    const [editCategory, setEditCategory] = useRecoilState(categoryState);
    const [, setEditing] = useRecoilState(editingState);
    const [from] = useRecoilState(fromState);
    const [tags] = useState([
        {
            "name" : "position",
            "nameKor" : "위치", 
            "limit" : 4,
            "data" : [
                {
                    idf : '가로X',
                    idx : 0,
                },
                {
                    idf : '세로Z',
                    idx : 2,
                },
                {
                    idf : '높이Y',
                    idx : 1,  
                },
            ]
        },
        {
            "name" : "rotation",
            "nameKor" : "회전", 
            "limit" : 6.28,
            "data" : [
                {
                    idf : '돌리기Y',
                    idx : 1,
                },
                {
                    idf : '눕히기X',
                    idx : 0,
                },
                {
                    idf : '눕히기Z',
                    idx : 2,
                },
            ]
        },
    ])

    function saveChange(e) {
        // from과 관계 없이 room에 넣기. 새로운 좌표, 새로운 카테고리로.
        // from이 'unlocated'였으면 스터프 목록에서 지워야 함. 위쪽에서 처리.
        let data = {...editStuff};
        data.posX = editposition[0];
        data.posY = editposition[1];
        data.posZ = editposition[2];
        data.rotX = editrotation[0];
        data.rotY = editrotation[1];
        data.rotZ = editrotation[2];

        data.selectedCategory = editCategory;
        // located에 추가해주기.
        addLocateStuff(e, data);
        if(from == 'unlocated') deleteUnlocatedStuff(e, {...editStuff});
        resetEdit();
    }
    function cancelChange(e) {
        // from이 located면 located에 넣기.
        if(from == 'located') addLocateStuff(e, {...editStuff});
        resetEdit();
    }
    
    function putin(e) {
        addUnlocatedStuff(e, {...editStuff});
        resetEdit();
    }
    function resetEdit() {
        setEditStuff({});
        setEditing(false);
        setEditCategory('');
        setEditPosition([0, 0, 0]);
        setEditRotation([0, 0, 0]);
    }

    function changeCategory(category) {
        setEditCategory(category);
        console.log(category);
    }

    return (
        <Box
            sx={{minWidth : 300}}
        >
            <Typography variant="h6" sx={{mt : 1}}>
                카테고리 지정.
            </Typography>
            <Box
                style={{
                    display : 'flex',
                    flexFlow : 'wrap',
                    maxWidth : 300
                }}
            >
                {editStuff.categories.map(({category, categoryName}, index) => 
                    <Button 
                        sx={{ borderRadius : 5}}
                        onClick={() => changeCategory(category)}
                        key={index}
                    >
                        <Typography 
                            variant="body2"
                            style={{
                                backgroundColor : category == editCategory ? '#fc7a71' : '#f3f3f3',
                                color:  category == editCategory ? 'white' : '#fc7a71',
                            }}
                            sx={{
                                px : 2,
                                borderRadius: 4,
                            }} 
                        >
                            {categoryName}
                        </Typography>
                    </Button>
                )}
            </Box>

            <Divider />
            
            {tags.map( tag => (
                <Box sx={{ my : 2 }}>
                    <Typography>
                        {tag.nameKor}
                    </Typography>
                    <Divider />
                    <Box
                        style={{
                            display : 'flex',
                            flexDirection : 'column',
                            justifyContent : 'space-evenly'
                        }}
                        sx={{ px : 2}}
                    >
                        {tag.data.map( data => 
                            <UpDownControl 
                                part={tag.name} 
                                tag={data.idf} 
                                index={data.idx} 
                                limit={tag.limit}
                            />
                        )}
                    </Box>
                </Box>
            ))}

            <Box style={{display : 'flex', justifyContent : 'space-between'}}>
                { from == 'located' ?
                    <Button 
                        variant="outlined"
                        onClick={(e) => putin(e)}
                        size="small"
                        color="error"
                    >
                        집어넣기.
                    </Button>
                    : <Box sx={{width : 10, height : 10}} />
                }

                <Box>
                    <Button 
                        variant="outlined"
                        onClick={(e) => cancelChange(e)}
                        size="small"
                        color="error"
                        sx={{ mx : 2}}
                    >
                        취소
                    </Button>
                    <ColorButton 
                        variant="contained"
                        onClick={(e) => saveChange(e)}
                        size="small"
                    >
                        결정.
                    </ColorButton>
                </Box>
            </Box>
        </Box>
    )
}