declare var axios: any;

const instance = axios.create({
    baseURL: `http://127.0.0.1:8000/`,
    withCredentials: true,
})

export const getAllBooks = async (order: string, page: number) => {
    try {
        const response = await instance.get(`books/saved?order=${order}&page=${page}`);
        return response.data.data;
    } catch (error) {
        return error.response;
    }
}

export const getBookDetail = async (id: string) => {
    try {
        const response = await instance.get(`book/${id}`);
        return response.data;
    } catch (error) {
        return error.response;
    }
}

export const getSavedBooks = async () => {
    try {
        const response = await instance.get(`books/titles`);
        return response.data;
    } catch (error) {
        return error.response;
    }
}

export const searchBooksByTitle = async (title: string, page: number) => {
    try {
        const response = await instance.get(`books?title=${title}&page=${page}`);
        return response.data;
    } catch (error) {
        error.response;
    }
}


export const getBookCount = async () => {
    try {
        const response = await instance.get(`book/count`);
        return response.data;
    } catch (error) {
        error.response;
    }
}