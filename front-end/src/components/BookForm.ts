declare var swal: any;

import { getAllBooks } from '../services/bookService.js';
import { logout, getNickname } from '../services/authService.js';
import { renderNickname, renderBooks } from '../utils/bookRenderUtils.js';
import { getElementById, getButtonElement } from '../utils/domUtils.js';

let page = 0;

document.addEventListener('DOMContentLoaded', async () => {
    const nameElement = getElementById('nickname');
    if (nameElement) {
        const nicknameResponse = await getNickname();
        renderNickname(nicknameResponse.data);
        renderBooks(await getAllBooks('id', page));
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
        .then((value) => {
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