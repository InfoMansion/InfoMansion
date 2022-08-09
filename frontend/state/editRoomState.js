import { atom } from 'recoil'

export const editingState = atom({
    key : 'editingState',
    default : false,
})

export const positionState = atom({
    key : 'positionState',
    default : [0, 0, 0],
})

export const rotationState = atom({
    key : 'rotationState', 
    default : [0, 0, 0],
})

export const editStuffState = atom({
    key : 'editStuffState',
    default : {},
})

export const fromState = atom({
    key : 'fromState',
    default : '',   
})

export const categoryState = atom({
    key : 'categoryState',
    default : 'NONE',
})