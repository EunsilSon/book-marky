import { requestPasswordMail, getSecureQuestion, checkSecureQuestion, updatePassword } from '../services/authService.js';
import { getButtonElement, getInputValue, showError, showAlert } from '../utils/domUtils.js';


const pwMailButton = getButtonElement('pw-mail-submit');
const usernameBtn = getButtonElement('update-pw-username');
const questionBtn = getButtonElement('update-pw-question');
const updateBtn = getButtonElement('update-pw');

if (pwMailButton) {
    pwMailButton.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        try {
            const response = await requestPasswordMail(getInputValue('username'));
            alert('메일함을 확인하세요.');

            if (response.status == 200) {
                console.log(response.data);
            } else {
                alert('존재하지 않는 이메일입니다. 다시 입력하세요.');
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
                console.log(response.data);
            } else {
                alert('존재하지 않는 이메일입니다. 다시 입력하세요.');
            }

        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}


if (questionBtn) {
    questionBtn.addEventListener('click', async (event: Event) => {
        event.preventDefault();

        const answerValue = getInputValue('answer');

        try {
            const response = await checkSecureQuestion({
                username: getInputValue('username'),
                answer: answerValue,
            });

            if (response.status == 200) {
                console.log(response.data);
            } else {
                alert('보안 질문의 답변이 일치하지 않습니다. 다시 입력하세요.');
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
                alert('비밀번호 변경이 완료되었습니다. 로그인 페이지로 이동합니다.');
                window.location.href = '/front-end/html/auth/index.html';
            } else {
                alert('토큰이 만료되었습니다.');
                console.log(response.data);
            }
            
        } catch (error) {
            console.error('Update Password Error:', error);
        }
    });
}