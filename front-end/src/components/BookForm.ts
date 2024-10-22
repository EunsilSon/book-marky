import { getAllBooks } from '../services/bookService.js';
import { getNickname } from '../services/authService.js';
import { renderNickname, renderBooks } from '../utils/bookRenderUtils.js';

document.addEventListener('DOMContentLoaded', async () => {
    const nicknameResponse = await getNickname();
    renderNickname('nickname', nicknameResponse.data);

    const booksResponse = await getAllBooks('id', 0);
    renderBooks(booksResponse);
})