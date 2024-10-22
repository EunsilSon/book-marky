import { getButtonElement } from '../dist/utils/domUtils.js';

const logout = (event: Event) => {
    event.preventDefault();
    localStorage.removeItem('username');
    window.history.replaceState(null, '', '/front-end/html/auth/index.html');
    window.location.href = '/front-end/html/auth/index.html';
}

const moveToLogin = (event: Event) => {
    event.preventDefault();
    window.location.href = '/front-end/html/auth/index.html';
}

if (getButtonElement('logout')) {
    const logoutElement = getButtonElement('logout');
    logoutElement.addEventListener('click', logout);
}

if (getButtonElement('move-to-login')) {
    const loginButton = getButtonElement('move-to-login');
    loginButton.addEventListener('click', moveToLogin);
}

if (getButtonElement('back')) {
    const backBtn = getButtonElement('back');
    backBtn.addEventListener('click', () => {
        window.history.back();
    })
}