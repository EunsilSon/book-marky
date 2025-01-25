import { getPassages, getPassageDetail, getDeletedPassages, updatePassage, deletePassage, createPassage, restorePassage } from "../services/passageService.js";
import { getBookDetail } from "../services/bookService.js";
import { showError } from "../utils/domUtils.js";
import { renderBookDetail } from "../utils/bookRenderUtils.js";
import { renderPassages, renderDetailForm, renderUpdateForm, renderPassageCreationForm, renderDeletedPassages } from "../utils/passageRenderUtils.js";

document.addEventListener('DOMContentLoaded', async () => {
    const currentPath = window.location.pathname;

    if (currentPath.endsWith('all.html')) {
        const bookId = new URLSearchParams(window.location.search).get('id');

        const bookResponse = await getBookDetail(bookId);
        renderBookDetail(bookResponse.data);

        const passagesResponse = await getPassages(bookId, 0);
        renderPassages(passagesResponse.data);
    }

    if (currentPath.endsWith('detail.html')) {
        const passageId = new URLSearchParams(window.location.search).get('id');
        const passageResponse = await getPassageDetail(passageId);
        renderDetailForm(passageResponse.data);
    }

    if (currentPath.endsWith('update.html')) {
        const passageId = new URLSearchParams(window.location.search).get('id');
        const passageResponse = await getPassageDetail(passageId);
        renderUpdateForm(passageResponse.data);
    }

    if (currentPath.endsWith('create.html')) {
        renderPassageCreationForm();
    }

    if (currentPath.endsWith('deleted.html')) {
        const deletedPassages = await getDeletedPassages();
        renderDeletedPassages(deletedPassages.data);
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

export const createPassageProcess = async (isbn: string, content: string, pageNum: string) => {
    try {
        const newPassage = {
            isbn,
            content,
            pageNum,
        };
        return createPassage(newPassage);
    } catch (error) {
        return showError(error);
    }
}

export const deletedPassageProcess = async () => {
    try {
        getDeletedPassages();
    } catch (error) {
        return showError(error);
    }
}

export const restorePassageProcess = async (passageId: string) => {
    try {
        restorePassage(passageId);
    } catch (error) {
        return showError(error);
    }
}