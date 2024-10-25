import { getAllBooks } from '../services/bookService.js';
import { getNickname } from '../services/authService.js';
import { renderNickname, renderBooks } from '../utils/bookRenderUtils.js';
import { getButtonElement } from '../utils/domUtils.js';

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

createPassageBtn.addEventListener('click', moveToCreatePassage);
deletedPassageBtn.addEventListener('click', moveToDeletedPassage);