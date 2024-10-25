import { getElementById, showAlert } from "./domUtils.js";
import { updatePassageProcess, deletePassageProcess, createPassageProcess, restorePassageProcess } from "../components/PassageForm.js";
import { getSavedBooks } from '../services/bookService.js';

export const renderPassages = (passages: Passage[]) => {
    const container = getElementById('passages-container');
    container.innerHTML = '';

    passages.forEach(passage => {
        const link = document.createElement('a');
        link.href = `../passage/detail.html?id=${passage.id}`;
        link.style.textDecoration = 'none';
        link.style.cursor = 'pointer';
        link.style.color = 'inherit';

        const passageDiv = document.createElement('div');
        passageDiv.classList.add('passage-item');

        const passageElement = document.createElement('p');
        passageElement.innerText = `${passage.content}`;

        passageDiv.appendChild(passageElement);
        link.appendChild(passageDiv);
        container.appendChild(link);
    });
};

export const renderPassageDetail = (passage: Passage, isEditing: boolean) => {
    const container = getElementById('passage-detail-container');
    container.innerHTML = '';

    const contentDiv = document.createElement('div');
    const editDiv = document.createElement('div');

    // 수정 모드
    if (isEditing) {
        const pageNumInput = document.createElement('textarea');
        pageNumInput.value = passage.pageNum;
        contentDiv.appendChild(pageNumInput);

        const contentInput = document.createElement('textarea');
        contentInput.value = passage.content;
        contentDiv.appendChild(contentInput);

        const saveBtn = document.createElement('button');
        saveBtn.innerText = '저장';

        saveBtn.addEventListener('click', () => {
            // 수정된 내용 가져오기
            const updatedPageNum = pageNumInput.value;
            const updatedContent = contentInput.value;

            // API 호출을 위한 Passage 객체
            const updatedPassage = { id: passage.id, content: updatedContent, pageNum: updatedPageNum };
            updatePassageProcess(updatedPassage);

            showAlert('수정되었습니다.');
            window.location.reload();
        });

        contentDiv.appendChild(saveBtn); // 저장 버튼 추가
    } else {
        const pageNumText = document.createElement('p');
        pageNumText.innerText = passage.pageNum;
        contentDiv.appendChild(pageNumText);

        const contentText = document.createElement('p');
        contentText.innerText = passage.content;
        contentDiv.appendChild(contentText);
    }

    const updateBtn = document.createElement('button');
    updateBtn.innerText = '수정';

    const deleteBtn = document.createElement('button');
    deleteBtn.innerText = '삭제';

    updateBtn.addEventListener('click', () => {
        if (isEditing) {
            renderPassageDetail(passage, false);
        } else {
            renderPassageDetail(passage, true);
        }
    });

    deleteBtn.addEventListener('click', () => {
        const confirm = window.confirm('삭제하시겠습니까?');

        if (confirm) {
            deletePassageProcess(passage.id);
            showAlert('삭제되었습니다. 이전 페이지로 이동합니다.');
            window.history.back();
        }
    });

    // 수정 모드일 때는 버튼을 숨김
    if (isEditing) {
        updateBtn.style.display = 'none'; // 수정 버튼 숨기기
        deleteBtn.style.display = 'none'; // 삭제 버튼 숨기기
    }

    editDiv.appendChild(updateBtn);
    editDiv.appendChild(deleteBtn);
    container.appendChild(contentDiv);
    container.appendChild(editDiv);
}

export const renderPassageForm = () => {
    const container = document.getElementById('passage-creation-container');
    container.innerHTML = '';

    // 책 제목
    const bookTitleDiv = document.createElement('div');
    const bookTitleLabel = document.createElement('label');
    bookTitleLabel.innerText = '책 제목';
    const bookTitleInput = document.createElement('input');
    bookTitleInput.id = 'book-title';

    const selectedTitle = localStorage.getItem('title'); // 책 검색을 통해 선택한 책의 제목 출력
    if (selectedTitle) {
        console.log(selectedTitle);
        bookTitleInput.value = selectedTitle;
        //localStorage.removeItem('title');
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
            showAlert('검색 할 책 제목을 입력하세요.');
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

        createPassageProcess(isbn, content, pageNum);

        showAlert('생성이 완료되었습니다. 메인 페이지로 이동합니다.');
        window.location.href = `../book/index.html`;
    });
    saveDiv.appendChild(saveButton);

    container.appendChild(bookTitleDiv);
    container.appendChild(getBookDiv);
    container.appendChild(searchBookDiv);
    container.appendChild(contentDiv);
    container.appendChild(pageDiv);
    container.appendChild(saveDiv);

    // 옵션 렌더링 함수
    const renderOptions = async () => {
        try {
            const response = await getSavedBooks();
            console.log(response);
            const bookTitles = response.data;

            const existingSelect = document.getElementById('book-title-options');
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
    const container = document.getElementById('deleted-passage-container');

    passages.forEach((passage) => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'passage-item';

        const bookDiv = document.createElement('div');
        bookDiv.className = 'book';
        const bookP = document.createElement('p');
        bookP.textContent = `책 제목: ${passage.bookId}`;
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
