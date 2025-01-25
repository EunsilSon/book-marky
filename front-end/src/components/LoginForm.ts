declare var swal: any;

import { login } from '../services/authService.js';
import { getButtonElement, getFormElement, getInputElement, getInputValue } from '../utils/domUtils.js';

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

        if (response.status == 200) {
            localStorage.setItem('username', response.data);
            window.location.href = '/html/book/index.html';
        }

        if (response.status == 401) {
            getInputElement('username').value = '';
            getInputElement('password').value = '';
            swal("로그인 실패", "아이디 또는 비밀번호가 맞지 않습니다.", "error");
        }

    } catch (error) {
        console.error('Login Error:', error);
    }
};

const moveToJoin = (event: Event) => {
    event.preventDefault();
    window.location.href = '/html/auth/join.html';
}

const moveToPw = (event: Event) => {
    event.preventDefault();
    window.location.href = '/html/auth/request-pw.html';
}

loginForm.addEventListener('submit', loginFormProcess);
joinButton.addEventListener('click', moveToJoin);
pwButton.addEventListener('click', moveToPw);