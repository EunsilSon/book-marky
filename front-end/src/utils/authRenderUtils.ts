import { getElementById } from './domUtils.js';

export const renderSecureQuestion = (containerId: string, question: string) => {
    const container = getElementById('question-container');
    const p = document.createElement('p');
    p.textContent = question;
    container.appendChild(p);
}