declare var axios: any;

const instance = axios.create({
    baseURL: `http://127.0.0.1:8000/`,
    withCredentials: true,
})

export const getAllBooks = async (order: string, page: number) => {
    try {
        const response = await instance.get(`books/saved?order=${order}&page=${page}`);
        return response.data;
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