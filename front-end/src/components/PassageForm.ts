import { getPassages, getPassageDetail, updatePassage, deletePassage } from "../services/passageService.js";
import { getBookDetail } from "../services/bookService.js";
import { getButtonElement, showError } from "../utils/domUtils.js";
import { renderBookDetail } from "../utils/bookRenderUtils.js";
import { renderPassages, renderPassageDetail } from "../utils/passageRenderUtils.js";

const backBtn = getButtonElement('back');

document.addEventListener('DOMContentLoaded', async () => {
    const currentPath = window.location.pathname;

    if (currentPath.endsWith('passage-list.html')) {
        console.log('잉?');
        const bookId = new URLSearchParams(window.location.search).get('id');

        const bookResponse = await getBookDetail(bookId);
        renderBookDetail(bookResponse);

        const passagesResponse = await getPassages(bookId, 0);
        renderPassages(passagesResponse.data);
    }

    if (currentPath.endsWith('passage-detail.html')) {
        const passageId = new URLSearchParams(window.location.search).get('id');
        const passageResponse = await getPassageDetail(passageId);
        renderPassageDetail(passageResponse.data, false);
    }

    if (currentPath.endsWith('update-passage.html')) {
        const passageId = new URLSearchParams(window.location.search).get('id');
        const passageResponse = await getPassageDetail(passageId);
        renderPassageDetail(passageResponse.data, false);
    }
})

export const updatePassageProcess = async (passage: Passage) => {
    try {
        updatePassage(passage);
    } catch (error) {
        return showError(error);
    }
}

export const deletePassageProcess = async (passageId: string) => {
    try {
        deletePassage(passageId);
    } catch (error) {
        return showError(error);
    }
}

const back = (event: Event) => {
    event.preventDefault();
    window.history.back();
}

backBtn.addEventListener('click', back);