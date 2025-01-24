declare var swal: any;

import { getAllBooks, getBookCount } from '../services/bookService.js';
import { logout, getNickname } from '../services/authService.js';
import { renderUser } from '../utils/authRenderUtils.js';
import { renderBookCount, renderBooks } from '../utils/bookRenderUtils.js';
import { getElementById, getButtonElement } from '../utils/domUtils.js';

let page = 0;

document.addEventListener('DOMContentLoaded', async () => {
    if (getElementById('user-div')) {
        const nicknameResponse = await getNickname();
        renderUser(nicknameResponse.data);
        renderBooks(await getAllBooks('id', page));
    }

    if (getElementById('book-count')) {
        const bookCountRespose = await getBookCount();
        renderBookCount(bookCountRespose.data);
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
        swal("로그아웃 되었습니다.", "Have A Book Day!", "success")
        .then(() => {
            localStorage.removeItem('username');
            window.history.replaceState(null, '', '/html/auth/index.html');
            window.location.href = '/html/auth/index.html';
        });
    }
}

export const getNextBooksProcess = async () => {
    const booksResponse = await getAllBooks('id', ++page);
    renderBooks(booksResponse);
}

export const getPrevBooksProcess = async () => {
    const booksResponse = await getAllBooks('id', --page);
    renderBooks(booksResponse);
}

export function getPageNumber(): number {
    return page;
}