declare var axios: any;

const instance = axios.create({
    baseURL: `http://127.0.0.1:8000/`,
    withCredentials: true,
})

export const getPassages = async (bookId: string, page: number) => {
    try {
        const response = await instance.get(`passages?bookId=${bookId}&order=id&page=${page}`);
        return response;
    } catch (error) {
        return error.response;
    }
}

export const getPassageDetail = async (passageId: string) => {
    try {
        const response = await instance.get(`passage/${passageId}`);
        return response;
    } catch (error) {
        return error.response;
    }
}

export const updatePassage = async (passage: Passage) => {
    try {
        const response = await instance.patch(`passage`, {
            passageId: passage.id,
            content: passage.content,
            pageNum: passage.pageNum,
        });
        return response;
    } catch (error) {
        return error.response;
    }
}


export const deletePassage = async (passageId: string) => {
    try {
        const response = await instance.delete(`passage/${passageId}`);
        return response;
    } catch (error) {
        return error.response;
    }
}
