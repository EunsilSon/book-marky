import { requestPasswordMail, getSecureQuestion, checkSecureQuestion, updatePassword } from '../services/authService.js';
import { getButtonElement, getInputValue, showError, showAlert } from '../utils/domUtils.js';


const pwMailButton = getButtonElement('pw-mail-submit');
const pwUpdate1Button = getButtonElement('pw-update-submit-1');
const pwUpdate2Button = getButtonElement('pw-update-submit-2');
const pwUpdate3Button = getButtonElement('pw-update-submit-3');

const usernameValue = getInputValue('username');


if (pwMailButton) {
    pwMailButton.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        const usernameValue = getInputValue('username');

        try {
            const response = await requestPasswordMail(usernameValue);
            console.log(response);

        } catch (error) {
            console.error('Request Password Mail Error:', error);
        }
    });
}


if (pwUpdate1Button) {
    pwUpdate1Button.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        try {
            const response = await getSecureQuestion(usernameValue);
            console.log(response);

        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}


if (pwUpdate2Button) {
    pwUpdate2Button.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        const answerValue = getInputValue('answer');

        try {
            const response = await checkSecureQuestion({
                username: usernameValue,
                answer: answerValue,
            });
            console.log(response);

        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}


if (pwUpdate3Button) {
    pwUpdate3Button.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        const passwordValue = getInputValue('password');
        const urlParams = new URLSearchParams(window.location.search);

        try {
            const response = await updatePassword({
                username: usernameValue,
                password: passwordValue,
                token: urlParams.get('token'),
            });
            console.log(response);

        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}