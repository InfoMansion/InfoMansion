import { atom } from "recoil";

export const clickedStuffState = atom({
    key : 'clickedStuffState',
    default : {}
})

export const clickedStuffCategoryState = atom({
    key : 'clickedStuffCategoryState',
    default : {
        category : 'NONE',
        categoryName : 'NONE'
    }
})