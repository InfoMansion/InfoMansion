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

export const loginUserState = atom({
    key : 'loginUserState',
    default : false
})

export const followState = atom({
    key : 'followState',
    default : false
})