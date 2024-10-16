import { login } from '../services/authService.js';
import { getInputValue, showError } from '../utils/domUtils.js';

export const loginForm = () => {
    const formElement = document.getElementById('login-form') as HTMLFormElement;
    console.log('LoginForm.ts');

    formElement.addEventListener('submit', async (event) => {
        event.preventDefault();

        const username = getInputValue('username');
        const userPw = getInputValue('password');

        try {
            const response = await login({
                username: username,
                password: userPw
            });
            console.log('Login succesful:', response);

        } catch (error) {
            showError('Login failed.Please check your credentials.');
            console.error('Login Error:', error);
        }
    })
}
