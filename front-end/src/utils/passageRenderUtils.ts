declare var swal: any;

import { getElementById, showAlert } from "./domUtils.js";
import { updatePassageProcess, deletePassageProcess, createPassageProcess, restorePassageProcess } from "../components/PassageForm.js";
import { getSavedBooks } from '../services/bookService.js';

export const renderPassages = (passages: Passage[]) => {
    const container = getElementById('passage-container');
    container.innerHTML = '';

    passages.forEach(passage => {
        const link = document.createElement('a');
        link.href = `../passage/detail.html?id=${passage.id}`;

        const passageDiv = document.createElement('div');
        passageDiv.classList.add('passage-item');

        const passageContent = document.createElement('p');
        const shortContent = passage.content.length > 50 ? passage.content.substring(0, 50) + '...' : passage.content;
        passageContent.innerText = shortContent;

        passageDiv.appendChild(passageContent);
        link.appendChild(passageDiv);
        container.appendChild(link);
    });
};

export const renderDetailForm = (passage: Passage) => {
    renderDetailMode(passage);
    setDetailButtonEventListeners(passage);
};

export const renderUpdateForm = (passage: Passage) => {
    renderUpdateMode(passage);
    setUpdateButtonEventListeners(passage);
}

const renderDetailMode = (passage: Passage) => {
    const passageDetail = getElementById('passage-detail');
    const pageDiv = document.createElement('div');
    const pageNumP = document.createElement('p');
    pageNumP.innerText = 'page. ' + passage.pageNum;
    pageNumP.id = 'detail-page-num';

    pageDiv.appendChild(pageNumP);

    const contentDiv = document.createElement('div');
    const contentP = document.createElement('p');
    contentP.innerText = passage.content;
    contentP.id = 'detail-content';

    contentDiv.appendChild(contentP);

    passageDetail.appendChild(pageNumP);
    passageDetail.appendChild(contentP);
};

const renderUpdateMode = (passage: Passage) => {
    const passageDetail = getElementById('passage-update');
    const pageDiv = document.createElement('div');
    const pageNum = document.createElement('textarea');
    const pageNumLabel = document.createElement('label');
    pageNumLabel.innerText = '쪽수';
    pageNum.innerText = passage.pageNum;
    pageNum.id = 'update-page-num';

    pageDiv.appendChild(pageNumLabel);
    pageDiv.appendChild(pageNum);

    const contentDiv = document.createElement('div');
    const content = document.createElement('textarea');
    const contentLabel = document.createElement('label');
    content.innerText = passage.content;
    content.id = 'update-content';
    contentLabel.innerText = '내용';

    contentDiv.appendChild(contentLabel);
    contentDiv.appendChild(content);

    passageDetail.appendChild(pageDiv);
    passageDetail.appendChild(contentDiv);

    // 글자 수만큼 스크롤 길이 지정
    const contentHeight = content.scrollHeight;
    content.style.height = contentHeight + 'px';
}

const setDetailButtonEventListeners = (passage: Passage) => {
    const updateButton = getElementById('update');
    const deleteButton = getElementById('delete');
    
    updateButton.addEventListener('click', () => {
        window.location.href = '/html/passage/update.html?id=' + passage.id;
    });

    deleteButton.addEventListener('click', () => {
        swal({
            title: "삭제하시겠습니까?",
            text: "30일간 보관되며, '최근 삭제한 하이라이트'에서 확인할 수 있습니다.",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
        .then((confirm) => {
        if (confirm) {
            deletePassageProcess(passage.id);
            swal({
                position: "top-end",
                icon: "success",
                title: "삭제 완료",
                timer: 1000
            })
            .then(() => {
                window.history.back();
            })
        }
        });
    });
};

const setUpdateButtonEventListeners = (passage: Passage) => {
    const saveButton = getElementById('save');
    const cancelButton = getElementById('cancel');

    saveButton.addEventListener('click', () => {
        const pageNumInput = getElementById('update-page-num') as HTMLTextAreaElement;
        const contentInput = getElementById('update-content') as HTMLTextAreaElement;

        const updatedPassage = {
            id: passage.id,
            content: contentInput.value,
            pageNum: pageNumInput.value,
        };

        updatePassageProcess(updatedPassage);

        swal({
            position: "top-end",
            icon: "success",
            title: "수정 완료",
            timer: 1000
        })
        .then(() => {
            window.location.href = '/html/passage/detail.html?id=' + passage.id;
        })
    });

    cancelButton.addEventListener('click', () => {
        window.location.href = '/html/passage/detail.html?id=' + passage.id;
    });
}

export const renderPassageCreationForm = () => {
    const container = getElementById('passage-creation-container');

    const bookDiv = getElementById('book'); 
    const bookTitleInput = document.createElement('input');
    bookTitleInput.id = 'book-title';
    bookTitleInput.placeholder = '책을 선택하세요';

    if (localStorage.getItem('title')) {
        bookTitleInput.id = localStorage.getItem('title');
    } else {
        bookTitleInput.innerText = '';
    }

    const selectedTitle = localStorage.getItem('title');
    if (selectedTitle) {
        bookTitleInput.value = selectedTitle;
    }

    const buttonBox = getElementById('button-box');
    const getBookButton = document.createElement('button');
    getBookButton.innerText = '저장된 책 보기';
    getBookButton.id = 'get-book';

    getBookButton.addEventListener('click', async () => {
        await renderBookOptions();
    });

    const searchBookButton = document.createElement('button');
    searchBookButton.innerText = '찾기';
    searchBookButton.id = 'search-book';

    searchBookButton.addEventListener('click', () => {
        const title = bookTitleInput.value;
        if (title) {
            window.location.href = `../book/search.html?title=${bookTitleInput.value}`;
        } else {
            showAlert('검색할 책 제목을 입력하세요.');
        }
    });

    buttonBox.appendChild(getBookButton);
    buttonBox.appendChild(searchBookButton);

    bookDiv.appendChild(bookTitleInput);
    bookDiv.appendChild(buttonBox);

    const passageDiv = getElementById('passage');
    const contentInput = document.createElement('input');
    contentInput.id = 'passage-content-input';
    contentInput.placeholder = '구절 내용을 입력하세요';
    const pageNumInput = document.createElement('input');
    pageNumInput.placeholder = '쪽수를 입력하세요';
    pageNumInput.id = 'page-num-input';

    passageDiv.appendChild(contentInput);
    passageDiv.appendChild(pageNumInput);

    const saveButton = document.createElement('button');
    saveButton.innerText = '완료';
    saveButton.id = 'save';

    saveButton.addEventListener('click', () => {
        const isbn = localStorage.getItem('isbn');
        const content = contentInput.value;
        const pageNum = pageNumInput.value;

        localStorage.removeItem('isbn');

        createPassageProcess(isbn, content, pageNum)
        .then(response => {
            console.log(response);
            if (response.status == 200) {
                swal({
                    position: "top-end",
                    icon: "success",
                    title: "작성 완료",
                    timer: 800
                })
                .then(() => {
                    window.location.href = `../book/index.html`;
                })
            }
        })
        .catch((error) => {
            console.log('데이터를 가져오는 중 오류 발생:', error);
        })
    });

    container.appendChild(bookDiv);
    container.appendChild(passageDiv);
    container.appendChild(saveButton);

    const renderBookOptions = async () => {
        try {
            const response = await getSavedBooks();
            const bookTitles = response.data;

            const existingSelect = document.getElementById('book-title-options');
            if (existingSelect) {
                existingSelect.remove();
            }

            const selectElement = document.createElement('select');
            selectElement.id = 'book-title-options';
            selectElement.innerHTML = '<option value="">선택</option>';

            bookTitles.forEach((item: { isbn: string; title: string; }) => {
                const option = document.createElement('option');
                option.value = item.isbn;
                option.innerText = item.title;
                selectElement.appendChild(option);
            });

            bookDiv.appendChild(selectElement);

            selectElement.addEventListener('change', (event) => {
                const target = event.target as HTMLSelectElement; // 타입 단언
                const selectedOption = target.options[target.selectedIndex];

                const selectedISBN = selectedOption.value;
                const selectedTitle = selectedOption.innerText;

                if (selectedTitle) {
                    bookTitleInput.value = selectedTitle;
                    selectElement.style.display = 'none';
                    localStorage.setItem('isbn', selectedISBN);
                }
            });
        } catch (error) {
            console.error('데이터를 가져오는 중 오류 발생:', error);
        }
    };
};
export const renderDeletedPassages = (passages: DeletedPassage[]) => {
    const container = getElementById('deleted-passage-container');

    passages.forEach((passage) => {
        const itemDiv = document.createElement('div');
        const passageDiv = document.createElement('div');
        itemDiv.className = 'deleted-passage-item';
        passageDiv.className = 'passage-div';

        const bookP = document.createElement('p');
        bookP.id = 'deleted-book-title';
        bookP.textContent = `${passage.title}`;

        const contentP = document.createElement('p');
        contentP.id = 'deleted-content';
        contentP.textContent = passage.content;

        const restoreBtn = document.createElement('button');
        restoreBtn.id = 'restore';
        restoreBtn.innerText = '복구';
        restoreBtn.addEventListener('click', () => {
            swal({
                title: "복구하시겠습니까?",
                icon: "info",
                buttons: true,
            })
            .then((confirm) => {
            if (confirm) {
                restorePassageProcess(passage.id);
                swal({
                    position: "top-end",
                    icon: "success",
                    title: "복구 완료",
                    timer: 1000
                })
                .then(() => {
                    window.location.reload();
                })
            }
            });
        });

        passageDiv.appendChild(bookP);
        passageDiv.appendChild(contentP);

        itemDiv.appendChild(passageDiv);
        itemDiv.appendChild(restoreBtn);

        container.appendChild(itemDiv);
    });
};
