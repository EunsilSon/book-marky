import { User } from '../types/user';

declare var axios: any;

const API_URL = 'http://127.0.0.1:8000/login';

export const login = async (user: User): Promise<any> => {
    console.log('authService.ts');
    try {
        const response = await axios({
            method: 'post',
            url: API_URL,
            data: {
                name: user.username,
                password: user.password
            }
        });

        return response.data;

    } catch (error) { // Axios 오류
        if (axios.isAxiosError(error) && error.response) {
            console.error('Axios Error:', error.response.data.message);
        } else {
            console.error('Error:', error);
        }
    }
}