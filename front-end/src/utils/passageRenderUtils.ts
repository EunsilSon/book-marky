import { getElementById, showAlert } from "./domUtils.js";
import { updatePassageProcess, deletePassageProcess, createPassageProcess, restorePassageProcess } from "../components/PassageForm.js";

export const renderPassages = (passages: Passage[]) => {
    const container = getElementById('passages-container');
    container.innerHTML = '';

    passages.forEach(passage => {
        const link = document.createElement('a');
        link.href = `../book/passage-detail.html?id=${passage.id}`;
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

    const pageNumLabel = document.createElement('label');
    pageNumLabel.innerText = 'page:';
    const pageNumTextarea = document.createElement('textarea');
    pageNumTextarea.placeholder = 'Write Here';
    pageNumTextarea.rows = 1;
    pageNumTextarea.style.resize = 'none';

    const contentLabel = document.createElement('label');
    contentLabel.innerText = 'content:';
    const contentTextarea = document.createElement('textarea');
    contentTextarea.placeholder = 'Write Here';
    contentTextarea.rows = 4;
    contentTextarea.style.resize = 'none';

    const editContainer = document.createElement('div');
    const saveButton = document.createElement('button');
    saveButton.innerText = '저장';

    saveButton.addEventListener('click', () => {
        const content = contentTextarea.value;
        const pageNum = pageNumTextarea.value;

        createPassageProcess(content, pageNum);
    });

    editContainer.appendChild(saveButton);
    container.appendChild(pageNumLabel);
    container.appendChild(pageNumTextarea);
    container.appendChild(contentLabel);
    container.appendChild(contentTextarea);
    container.appendChild(editContainer);
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

        const hr = document.createElement('hr');

        itemDiv.appendChild(bookDiv);
        itemDiv.appendChild(contentDiv);
        itemDiv.appendChild(restoreDiv);
        itemDiv.appendChild(hr);
        container.appendChild(itemDiv);
    });
};
