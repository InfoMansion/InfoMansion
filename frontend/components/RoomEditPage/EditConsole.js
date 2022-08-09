import {Box, Button, Typography} from '@mui/material'
import { useRecoilState } from 'recoil'
import { categoryState, editingState, editStuffState, fromState, positionState, rotationState } from '../../state/editRoomState'
import UpDownControl from './atoms/UpDownControl';

export default function EditConsole({addLocateStuff, addUnlocatedStuff, deleteUnlocatedStuff}) {
    const [editStuff, setEditStuff] = useRecoilState(editStuffState);
    const [editposition, setEditPosition] = useRecoilState(positionState);
    const [editrotation, setEditRotation] = useRecoilState(rotationState);

    const [editCategory, setEditCategory] = useRecoilState(categoryState);
    const [, setEditing] = useRecoilState(editingState);
    const [from] = useRecoilState(fromState);

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

        data.selectedcategory = editCategory;
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
        <Box>
            <Typography variant="h6">
                카테고리 지정.
            </Typography>
            <Box
                style={{
                    display : 'flex',
                    flexFlow : 'wrap'
                }}
            >
                {/* 여기 나중에 카테고리로 수정, */}
                {editStuff.categories.map(({category, categoryName}, index) => 
                    <Button 
                        sx={{ p : 0, borderRadius : 5}}
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
                                px: 2,
                                m : 1,
                                borderRadius: 4,
                            }} 
                        >
                            {categoryName}
                        </Typography>
                    </Button>
                )}
            </Box>

            <Typography>
                이동
            </Typography>
            
            <Box
                style={{
                    display : 'flex',
                    justifyContent : 'space-evenly'
                }}
            >
                {['x', 'y', 'z'].map((tag, index) => 
                    <UpDownControl part={"position"} tag={tag} index={index} limit={4}/>
                )}
            </Box>
            
            <Typography>
                회전
            </Typography>
            <Box
                style={{
                    display : 'flex',
                    justifyContent : 'space-evenly' 
                }}
            >
                {['x', 'y', 'z'].map((tag, index) => 
                    <UpDownControl part={"rotation"} tag={tag} index={index} limit={6.28} />
                )}
            </Box>
            <Box style={{display : 'flex'}}>
                { from == 'located' ?
                    <Button 
                        variant="outlined"
                        onClick={(e) => putin(e)}
                    >
                        집어넣기.
                    </Button>
                    : <></>
                }

                <Button 
                    variant="outlined"
                    onClick={(e) => cancelChange(e)}
                >
                    취소
                </Button>
                <Button 
                    variant="contained"
                    onClick={(e) => saveChange(e)}
                >
                    결정.
                </Button>
            </Box>
        </Box>
    )
}