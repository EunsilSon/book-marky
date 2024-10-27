import { join, checkDuplicateUsername, checkDuplicateNickname, checkDuplicateTel } from '../services/authService.js';
import { getFormElement, getInputElement, getInputValue, showError, showAlert } from '../utils/domUtils.js';


const usernameInput = getInputElement('username');
const nicknameInput = getInputElement('nickname');
const telephoneInput = getInputElement('telephone');
const joinForm = getFormElement('join-form');


const checkUsername = async (event: Event) => {
    event.preventDefault();

    try {
        const response = await checkDuplicateUsername(usernameInput.value);

        if (response) {
            showAlert('중복되는 아이디입니다. 다시 입력하세요.')
            usernameInput.value = '';
        }

    } catch (error) {
        showError('Username is duplicated.');
        console.error('Username Error:', error);
    }
};


const checkNickname = async (event: Event) => {
    event.preventDefault();

    try {
        const response = await checkDuplicateNickname(nicknameInput.value);

        if (response) {
            showAlert('중복되는 닉네임입니다. 다시 입력하세요.')
            usernameInput.value = '';
        }

    } catch (error) {
        showError('Nickname is duplicated.');
        console.error('Nickname Error:', error);
    }
};


const checkTelephone = async (event: Event) => {
    event.preventDefault();

    try {
        const response = await checkDuplicateTel(telephoneInput.value);

        if (response) {
            showAlert('중복되는 연락처입니다. 다시 입력하세요.')
            usernameInput.value = '';
        }

    } catch (error) {
        showError('Telephone is duplicated.');
        console.error('Telephone Error:', error);
    }
};


const joinFormProcess = async (event: Event) => {
    event.preventDefault();

    const usernameValue = usernameInput.value;
    const nicknameValue = nicknameInput.value;
    const telephoneValue = telephoneInput.value;
    const passwordValue = getInputValue('password');
    const secureQuestionIdValue = getInputValue('secure-question-id');
    const answerContentValue = getInputValue('answer-content');

    try {
        const response = await join({
            username: usernameValue,
            password: passwordValue,
            nickname: nicknameValue,
            telephone: telephoneValue,
            secureQuestionId: secureQuestionIdValue,
            answerContent: answerContentValue,
        });
        console.log('Join Result:', response);

    } catch (error) {
        showError('Join failed. Please check your credentials.');
        console.error('Join Error:', error);
    }
};


usernameInput.addEventListener('change', checkUsername)
nicknameInput.addEventListener('change', checkNickname)
telephoneInput.addEventListener('change', checkTelephone)
joinForm.addEventListener('submit', joinFormProcess);