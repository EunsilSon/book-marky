import { User, UserPasswordAnswer, UserPasswordToken } from '../types/user';
import { handleError } from '../utils/domUtils.js';

declare var axios: any;

const instance = axios.create({
    baseURL: `http://127.0.0.1:8000`,
    withCredentials: true,
})


export const login = async (user: User): Promise<any> => {
    try {
        const formData = new FormData();
        formData.append('username', user.username);
        formData.append('password', user.password);

        const response = await instance.post(`/login`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;

    } catch (error) {
        handleError(error);
    }
}


export const join = async (user: User) => {
    try {
        const response = await instance.post(`/user`, user);
        return response.data;

    } catch (error) {
        handleError(error);
    }
}


export const checkDuplicateUsername = async (username: string) => {
    try {
        const response = await instance.post(`/registration/username/${username}`);
        return response.data;

    } catch (error) {
        handleError(error);
    }
}


export const checkDuplicateNickname = async (nickname: string) => {
    try {
        const response = await instance.post(`/registration/nickname/${nickname}`);
        return response.data;

    } catch (error) {
        handleError(error);
    }
}


export const checkDuplicateTel = async (telephone: string) => {
    try {
        const response = await instance.post(`/registration/telephone/${telephone}`);
        return response.data;

    } catch (error) {
        handleError(error);
    }
}


export const requestPasswordMail = async (username: string) => {
    try {
        const response = await instance.get(`/user/mail/${username}`);
        console.log(response);
    } catch (error) {
        handleError(error);
    }
}


export const getSecureQuestion = async (username: string) => {
    try {
        const response = await instance.get(`/user/question/${username}`);
        console.log(response);
    } catch (error) {
        handleError(error);
    }
}


export const checkSecureQuestion = async (userPasswordAnswer: UserPasswordAnswer) => {
    try {
        const response = await instance.post(`/user/question`, userPasswordAnswer);
        console.log(response);
    } catch (error) {
        handleError(error);
    }
}


export const updatePassword = async (userPasswordToken: UserPasswordToken) => {
    try {
        const response = await instance.put(`/user`, userPasswordToken);
        console.log(response);
    } catch (error) {
        handleError(error);
    }
}
