import { getButtonElement } from '../dist/utils/domUtils.js';

const moveToLogin = (event: Event) => {
    event.preventDefault();
    window.location.href = '/html/auth/index.html';
}

if (getButtonElement('move-to-login')) {
    const loginButton = getButtonElement('move-to-login');
    loginButton.addEventListener('click', moveToLogin);
}

if (getButtonElement('back')) {
    const backBtn = getButtonElement('back');
    backBtn.addEventListener('click', () => {
        localStorage.removeItem('title');
        localStorage.removeItem('isbn');
        window.history.back();
    })
}

if (getButtonElement('yes-login-app-name')) {
    const appBtn = getButtonElement('yes-login-app-name');
    appBtn.addEventListener('click', () => {
        window.location.href = '/html/book/index.html';
    })
}