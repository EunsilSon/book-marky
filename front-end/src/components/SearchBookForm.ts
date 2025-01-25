import { searchBooksByTitle } from '../services/bookService.js';
import { renderSearchBooks } from '../utils/bookRenderUtils.js';

let page = 1;

document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const title = urlParams.get('title');
    renderSearchBooks(title, await searchBooksProcess(title));
})

export const searchBooksProcess = async (title: string) => {
    const booksResponse = await searchBooksByTitle(title, page++);
    return booksResponse.data;
};