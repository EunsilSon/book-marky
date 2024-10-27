import { getAllBooks } from '../services/bookService.js';
import { logout, getNickname } from '../services/authService.js';
import { renderNickname, renderBooks } from '../utils/bookRenderUtils.js';
import { getButtonElement, showAlert } from '../utils/domUtils.js';

const logoutElement = getButtonElement('logout');
const createPassageBtn = getButtonElement('create-passage');
const deletedPassageBtn = getButtonElement('deleted-passage');

document.addEventListener('DOMContentLoaded', async () => {
    const nicknameResponse = await getNickname();
    renderNickname('nickname', nicknameResponse.data);

    const booksResponse = await getAllBooks('id', 0);
    renderBooks(booksResponse);
})

const moveToCreatePassage = (event: Event) => {
    event.preventDefault();
    window.location.href = '../passage/create.html';
}

const moveToDeletedPassage = (event: Event) => {
    event.preventDefault();
    window.location.href = '../passage/deleted.html';
}

const logoutProcess = async (event: Event) => {
    event.preventDefault();

    const response = await logout();

    if (response.status == 200) {
        showAlert('로그아웃 되었습니다.');

        localStorage.removeItem('username');
        window.history.replaceState(null, '', '/front-end/html/auth/index.html');
        window.location.href = '/front-end/html/auth/index.html';
    }
}

logoutElement.addEventListener('click', logoutProcess);
createPassageBtn.addEventListener('click', moveToCreatePassage);
deletedPassageBtn.addEventListener('click', moveToDeletedPassage);