import { getElementById } from '../utils/domUtils.js';

export const renderSecureQuestion = (containerId: string, question: string) => {
    const container = getElementById(containerId);
    const div = document.createElement('div');
    div.textContent = question;
    container.appendChild(div); 
}