import { searchBooksByTitle } from '../services/bookService.js';
import { renderSearchBooks } from '../utils/bookRenderUtils.js';

let page = 0;

document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const title = urlParams.get('title');
    renderSearchBooks(title, await searchBooksProcess(title));
})

export const searchBooksProcess = async (title: string) => {
    page += 1;
    const booksResponse = await searchBooksByTitle(title, page);
    return booksResponse.data;
};