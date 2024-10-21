import { handleError } from '../utils/domUtils.js';

declare var axios: any;

const instance = axios.create({
    baseURL: `http://127.0.0.1:8000/`,
    withCredentials: true,
})


export const getAllBooks = async (order: string, page: number) => {
    try {
        const response = await instance.get(`books/saved?order=${order}&page=${page}`);
        console.log(response);
    } catch (error) {
        handleError(error);
    }
}

// TODO: 책 상세정보 조회
export const getBookDetail = async (id: number) => {
    try {
        const response = await instance.get(`book/${id}`);
        console.log(response);
    } catch (error) {
        handleError(error);
    }
}