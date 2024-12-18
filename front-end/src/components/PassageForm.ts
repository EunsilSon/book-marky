import { getPassages, getPassageDetail, getDeletedPassages, updatePassage, deletePassage, createPassage, restorePassage } from "../services/passageService.js";
import { getBookDetail } from "../services/bookService.js";
import { showError } from "../utils/domUtils.js";
import { renderBookDetail } from "../utils/bookRenderUtils.js";
import { renderPassages, renderPassageDetail, renderPassageForm, renderDeletedPassages } from "../utils/passageRenderUtils.js";
import { showAlert } from "../utils/domUtils.js";

document.addEventListener('DOMContentLoaded', async () => {
    const currentPath = window.location.pathname;

    if (currentPath.endsWith('all.html')) {
        const bookId = new URLSearchParams(window.location.search).get('id');

        const bookResponse = await getBookDetail(bookId);
        renderBookDetail(bookResponse);

        const passagesResponse = await getPassages(bookId, 0);
        renderPassages(passagesResponse.data);
    }

    if (currentPath.endsWith('detail.html') || currentPath.endsWith('update.html')) {
        const passageId = new URLSearchParams(window.location.search).get('id');
        const passageResponse = await getPassageDetail(passageId);
        renderPassageDetail(passageResponse.data, false);
    }

    if (currentPath.endsWith('create.html')) {
        renderPassageForm();
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
        createPassage(newPassage);
    } catch (error) {
        return showError(error);
    }
}

export const deletedPassageProcess = async () => {
    try {
        const response = getDeletedPassages();
        showAlert('삭제되었습니다. 이전 페이지로 이동합니다.');
        window.history.back();
    } catch (error) {
        return showError(error);
    }
}

export const restorePassageProcess = async (passageId: string) => {
    try {
        const response = restorePassage(passageId);
        showAlert('복구되었습니다. 메인 페이지로 이동합니다.');
        window.location.href = `../../html/book/index.html`;
    } catch (error) {
        return showError(error);
    }
}