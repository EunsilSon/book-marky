import { getElementById, showAlert } from "./domUtils.js";
import { updatePassageProcess, deletePassageProcess } from "../components/PassageForm.js";

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
        const confirmDelete = window.confirm('삭제하시겠습니까?');

        if (confirmDelete) {
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