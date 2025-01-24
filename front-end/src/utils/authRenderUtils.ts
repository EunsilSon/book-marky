import { getElementById } from './domUtils.js';

export const renderUser = (nickname: string) => {
    const userDiv = getElementById('user-div');
    const icon = document.createElement('img');
    icon.src = '/img/account.png';
    const name = document.createElement('p');
    name.innerText = nickname;

    userDiv.appendChild(icon);
    userDiv.appendChild(name);
}

export const renderSecureQuestion = (question: string) => {
    const container = getElementById('question-container');
    const p = document.createElement('p');
    p.textContent = question;
    container.appendChild(p);
}