import { requestPasswordMail, getSecureQuestion, checkSecureQuestion, updatePassword } from '../services/authService.js';
import { getButtonElement, getInputValue, getInputElement, showAlert } from '../utils/domUtils.js';
import { renderSecureQuestion } from '../utils/renderUtils.js';


const pwMailButton = getButtonElement('pw-mail-submit');
const usernameBtn = getButtonElement('update-pw-username');
const answerBtn = getButtonElement('update-pw-answer');
const updateBtn = getButtonElement('update-pw');

if (pwMailButton) {
    pwMailButton.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        try {
            const response = await requestPasswordMail(getInputValue('username'));

            if (response.status == 200) {
                console.log(response.data);
                showAlert('메일함을 확인하세요.');
            } else {
                showAlert('존재하지 않는 이메일입니다. 다시 입력하세요.');
            }

        } catch (error) {
            console.error('Request Password Mail Error:', error);
        }
    });
}


if (usernameBtn) {
    usernameBtn.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        try {
            const response = await getSecureQuestion(getInputValue('username'));

            if (response.status == 200) {
                showAlert('보안 질문의 답변을 입력하세요.');
                renderSecureQuestion('question-container', response.data);
                
                getInputElement('answer').disabled = false;
                answerBtn.disabled = false;
            } else {
                showAlert('존재하지 않는 이메일입니다. 다시 입력하세요.');
            }

        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}


if (answerBtn) {
    answerBtn.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        const answerValue = getInputValue('answer');

        try {
            const response = await checkSecureQuestion({
                username: getInputValue('username'),
                answer: answerValue,
            });

            if (response.status == 200) {
                getInputElement('password').disabled = false;
                getButtonElement('update-pw').disabled = false;
                showAlert('새 비밀번호를 입력하세요.');
            } else {
                showAlert('보안 질문의 답변이 일치하지 않습니다. 다시 입력하세요.');
                console.log(response.data);
            }

        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}


if (updateBtn) {
    updateBtn.addEventListener('click', async (event: Event) => {
        event.preventDefault();
        const urlParams = new URLSearchParams(window.location.search);

        try {
            const response = await updatePassword({
                username: getInputValue('username'),
                password: getInputValue('password'),
                token: urlParams.get('token'),
            });

            if (response.status == 200) {
                showAlert('비밀번호 변경이 완료되었습니다. 로그인 페이지로 이동합니다.');
                window.location.href = '/front-end/html/auth/index.html';
            } else {
                showAlert('토큰이 만료되었습니다.');
                console.log(response.data);
            }
            
        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}