import { getAllBooks } from '../services/bookService.js';
import { logout, getNickname } from '../services/authService.js';
import { renderNickname, renderBooks } from '../utils/bookRenderUtils.js';
import { getElementById, getButtonElement, showAlert } from '../utils/domUtils.js';

let page = -1;

document.addEventListener('DOMContentLoaded', async () => {
    const nameElement = getElementById('nickname');
    if (nameElement) {
        const nicknameResponse = await getNickname();
        renderNickname(nicknameResponse.data);
        getNextAllBooksProcess();
    }

    const logoutElement = getButtonElement('logout');
    if (logoutElement) {
        logoutElement.addEventListener('click', logoutProcess);
    }

    const createPassageBtn = getButtonElement('create-passage');
    if (createPassageBtn) {
        createPassageBtn.addEventListener('click', moveToCreatePassage);
    }

    const deletedPassageBtn = getButtonElement('deleted-passage');
    if (deletedPassageBtn) {
        deletedPassageBtn.addEventListener('click', moveToDeletedPassage);
    }
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
        window.history.replaceState(null, '', '/html/auth/index.html');
        window.location.href = '/html/auth/index.html';
    }
}

export const getNextAllBooksProcess = async () => {
    const booksResponse = await getAllBooks('id', ++page);
    renderBooks(booksResponse);
}

export const getPrevAllBooksProcess = async () => {
    const booksResponse = await getAllBooks('id', --page);
    renderBooks(booksResponse);
}

export function getPageNumber(): number {
    return page;
}