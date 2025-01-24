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
            window.location.href = '/html/passage/detail.html?id=' + passage.id; // detail로 이동
        })
    });

    cancelButton.addEventListener('click', () => {
        window.location.href = '/html/passage/detail.html?id=' + passage.id; // detail로 이동
    });
} 

export const renderPassageCreateForm = () => {
    const container = getElementById('passage-creation-container');

    // 책 제목
    const bookTitleDiv = document.createElement('div');

    const bookTitleLabel = document.createElement('label');
    bookTitleLabel.innerText = '책 제목';

    const bookTitleInput = document.createElement('input');
    bookTitleInput.id = 'book-title';

    if (localStorage.getItem('title')) {
        bookTitleInput.id = localStorage.getItem('title');
    } else {
        bookTitleInput.innerText = '';
    }

    const selectedTitle = localStorage.getItem('title');
    if (selectedTitle) {
        bookTitleInput.value = selectedTitle;
    }

    bookTitleDiv.appendChild(bookTitleLabel);
    bookTitleDiv.appendChild(bookTitleInput);

    // 이미 저장된 책 버튼
    const getBookDiv = document.createElement('div');
    const getBookBtn = document.createElement('button');
    getBookBtn.innerText = '저장된 책 보기';
    getBookBtn.addEventListener('click', async () => {
        await renderOptions();
    });
    getBookDiv.appendChild(getBookBtn);

    // 찾기 버튼
    const searchBookDiv = document.createElement('div');
    const searchBookBtn = document.createElement('button');
    searchBookBtn.innerText = '찾기';
    searchBookBtn.addEventListener('click', () => {
        const title = bookTitleInput.value;
        if (title) {
            window.location.href = `../book/search.html?title=${bookTitleInput.value}`;
        } else {
            showAlert('검색할 책 제목을 입력하세요.');
        }
    });
    searchBookDiv.appendChild(searchBookBtn);

    // 구절 내용
    const contentDiv = document.createElement('div');
    const contentTextarea = document.createElement('textarea');
    contentTextarea.placeholder = '구절 내용을 입력하세요.';
    contentTextarea.rows = 4;
    contentTextarea.style.resize = 'none';
    contentDiv.appendChild(contentTextarea);

    // 쪽수
    const pageDiv = document.createElement('div');
    const pageNumInput = document.createElement('input');
    pageNumInput.placeholder = '쪽수를 입력하세요';
    pageDiv.appendChild(pageNumInput);

    // 저장 버튼
    const saveDiv = document.createElement('div');
    const saveButton = document.createElement('button');
    saveButton.innerText = '저장';
    saveButton.addEventListener('click', () => {
        const isbn = localStorage.getItem('isbn');
        const content = contentTextarea.value;
        const pageNum = pageNumInput.value;

        localStorage.removeItem('isbn');

        createPassageProcess(isbn, content, pageNum)
        .then(response => {
            if (response.status == 200) {
                showAlert('생성이 완료되었습니다. 메인 페이지로 이동합니다.');
                window.location.href = `../book/index.html`;
            } else {
                console.log(response);
            }
        })
        .catch((error) => {
            console.log('데이터를 가져오는 중 오류 발생:', error);
        })
    });

    saveDiv.appendChild(saveButton);
    container.appendChild(bookTitleDiv);
    container.appendChild(searchBookDiv);
    container.appendChild(getBookDiv);
    container.appendChild(contentDiv);
    container.appendChild(pageDiv);
    container.appendChild(saveDiv);

    // 옵션 렌더링 함수
    const renderOptions = async () => {
        try {
            const response = await getSavedBooks();
            const bookTitles = response.data;

            const existingSelect = getElementById('book-title-options');
            if (existingSelect) {
                existingSelect.remove();
            }

            const selectElement = document.createElement('select');
            selectElement.id = 'book-title-options';
            selectElement.innerHTML = '<option value="">책을 선택하세요</option>';

            bookTitles.forEach((item: { isbn: string; title: string; }) => {
                const option = document.createElement('option');
                option.value = item.isbn;
                option.innerText = item.title;
                selectElement.appendChild(option);
            });

            getBookDiv.appendChild(selectElement);

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
        itemDiv.className = 'passage-item';

        const bookDiv = document.createElement('div');
        bookDiv.className = 'book';
        const bookP = document.createElement('p');
        bookP.textContent = `${passage.title}`;
        bookDiv.appendChild(bookP);

        const contentDiv = document.createElement('div');
        contentDiv.className = 'content';
        const contentP = document.createElement('p');
        contentP.textContent = passage.content;
        contentDiv.appendChild(contentP);

        const restoreDiv = document.createElement('div');
        restoreDiv.className = 'restore';
        const restoreBtn = document.createElement('button');
        restoreBtn.innerText = '복구';
        restoreDiv.appendChild(restoreBtn);

        restoreBtn.addEventListener('click', () => {
            const confirm = window.confirm('복구하시겠습니까?');

            if (confirm) {
                restorePassageProcess(passage.id);
            }
        });

        itemDiv.appendChild(bookDiv);
        itemDiv.appendChild(contentDiv);
        itemDiv.appendChild(restoreDiv);
        container.appendChild(itemDiv);
    });
};
