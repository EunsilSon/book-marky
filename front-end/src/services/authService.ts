import { User } from '../types/user';

declare var axios: any;

const instance = axios.create({
    baseURL: 'http://127.0.0.1:8000',
    withCredentials: true,
})

export const login = async (user: User): Promise<any> => {
    console.log('authService.ts');
    try {
        const formData = new FormData();
        formData.append('username', user.username);
        formData.append('password', user.password);

        const response = await instance.post('/login', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });

        return response.data;

    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error('Axios Error:', error.response.data.message);
        } else {
            console.error('Error:', error);
        }
    }
}