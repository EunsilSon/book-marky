import { login } from '../services/authService.js';
import { getButtonElement, getFormElement, getInputElement, getInputValue, showAlert, showError } from '../utils/domUtils.js';

const loginForm = getFormElement('login-form');
const joinButton = getButtonElement('move-to-join');
const pwButton = getButtonElement('move-to-pw');

const loginFormProcess = async (event: Event) => {
    event.preventDefault();

    const usernameValue = getInputValue('username');
    const passwordValue = getInputValue('password');

    try {
        const response = await login({
            username: usernameValue,
            password: passwordValue,
        });
        console.log('Login Result:', response);

        if (response.status == 200) {
            localStorage.setItem('username', response.data);
            showAlert('로그인에 성공했습니다. 메인 페이지로 이동합니다.');
            window.location.href = '/front-end/html/book/index.html';
        }

        if (response.status == 401) {
            getInputElement('username').value = '';
            getInputElement('password').value = '';
            showAlert('아이디 또는 비밀번호가 맞지 않습니다. 다시 입력하세요.');
        }

    } catch (error) {
        console.error('Login Error:', error);
    }
};

const moveToJoin = (event: Event) => {
    event.preventDefault();
    window.location.href = '/front-end/html/auth/join.html';
}

const moveToPw = (event: Event) => {
    event.preventDefault();
    window.location.href = '/front-end/html/auth/request-password.html';
}

loginForm.addEventListener('submit', loginFormProcess);
joinButton.addEventListener('click', moveToJoin);
pwButton.addEventListener('click', moveToPw);