import { getElementById, showAlert } from "./domUtils.js";
import { updatePassageProcess, deletePassageProcess, createPassageProcess, restorePassageProcess } from "../components/PassageForm.js";
export var renderPassages = function (passages) {
    var container = getElementById('passages-container');
    container.innerHTML = '';
    passages.forEach(function (passage) {
        var link = document.createElement('a');
        link.href = "../book/passage-detail.html?id=".concat(passage.id);
        link.style.textDecoration = 'none';
        link.style.cursor = 'pointer';
        link.style.color = 'inherit';
        var passageDiv = document.createElement('div');
        passageDiv.classList.add('passage-item');
        var passageElement = document.createElement('p');
        passageElement.innerText = "".concat(passage.content);
        passageDiv.appendChild(passageElement);
        link.appendChild(passageDiv);
        container.appendChild(link);
    });
};
export var renderPassageDetail = function (passage, isEditing) {
    var container = getElementById('passage-detail-container');
    container.innerHTML = '';
    var contentDiv = document.createElement('div');
    var editDiv = document.createElement('div');
    // 수정 모드
    if (isEditing) {
        var pageNumInput_1 = document.createElement('textarea');
        pageNumInput_1.value = passage.pageNum;
        contentDiv.appendChild(pageNumInput_1);
        var contentInput_1 = document.createElement('textarea');
        contentInput_1.value = passage.content;
        contentDiv.appendChild(contentInput_1);
        var saveBtn = document.createElement('button');
        saveBtn.innerText = '저장';
        saveBtn.addEventListener('click', function () {
            // 수정된 내용 가져오기
            var updatedPageNum = pageNumInput_1.value;
            var updatedContent = contentInput_1.value;
            // API 호출을 위한 Passage 객체
            var updatedPassage = { id: passage.id, content: updatedContent, pageNum: updatedPageNum };
            updatePassageProcess(updatedPassage);
            showAlert('수정되었습니다.');
            window.location.reload();
        });
        contentDiv.appendChild(saveBtn); // 저장 버튼 추가
    }
    else {
        var pageNumText = document.createElement('p');
        pageNumText.innerText = passage.pageNum;
        contentDiv.appendChild(pageNumText);
        var contentText = document.createElement('p');
        contentText.innerText = passage.content;
        contentDiv.appendChild(contentText);
    }
    var updateBtn = document.createElement('button');
    updateBtn.innerText = '수정';
    var deleteBtn = document.createElement('button');
    deleteBtn.innerText = '삭제';
    updateBtn.addEventListener('click', function () {
        if (isEditing) {
            renderPassageDetail(passage, false);
        }
        else {
            renderPassageDetail(passage, true);
        }
    });
    deleteBtn.addEventListener('click', function () {
        var confirm = window.confirm('삭제하시겠습니까?');
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
};
export var renderPassageForm = function () {
    var container = document.getElementById('passage-creation-container');
    container.innerHTML = '';
    var pageNumLabel = document.createElement('label');
    pageNumLabel.innerText = 'page:';
    var pageNumTextarea = document.createElement('textarea');
    pageNumTextarea.placeholder = 'Write Here';
    pageNumTextarea.rows = 1;
    pageNumTextarea.style.resize = 'none';
    var contentLabel = document.createElement('label');
    contentLabel.innerText = 'content:';
    var contentTextarea = document.createElement('textarea');
    contentTextarea.placeholder = 'Write Here';
    contentTextarea.rows = 4;
    contentTextarea.style.resize = 'none';
    var editContainer = document.createElement('div');
    var saveButton = document.createElement('button');
    saveButton.innerText = '저장';
    saveButton.addEventListener('click', function () {
        var content = contentTextarea.value;
        var pageNum = pageNumTextarea.value;
        createPassageProcess(content, pageNum);
    });
    editContainer.appendChild(saveButton);
    container.appendChild(pageNumLabel);
    container.appendChild(pageNumTextarea);
    container.appendChild(contentLabel);
    container.appendChild(contentTextarea);
    container.appendChild(editContainer);
};
export var renderDeletedPassages = function (passages) {
    var container = document.getElementById('deleted-passage-container');
    passages.forEach(function (passage) {
        var itemDiv = document.createElement('div');
        itemDiv.className = 'passage-item';
        var bookDiv = document.createElement('div');
        bookDiv.className = 'book';
        var bookP = document.createElement('p');
        bookP.textContent = "\uCC45 \uC81C\uBAA9: ".concat(passage.bookId);
        bookDiv.appendChild(bookP);
        var contentDiv = document.createElement('div');
        contentDiv.className = 'content';
        var contentP = document.createElement('p');
        contentP.textContent = passage.content;
        contentDiv.appendChild(contentP);
        var restoreDiv = document.createElement('div');
        restoreDiv.className = 'restore';
        var restoreBtn = document.createElement('button');
        restoreBtn.innerText = '복구';
        restoreDiv.appendChild(restoreBtn);
        restoreBtn.addEventListener('click', function () {
            var confirm = window.confirm('복구하시겠습니까?');
            if (confirm) {
                restorePassageProcess(passage.id);
            }
        });
        var hr = document.createElement('hr');
        itemDiv.appendChild(bookDiv);
        itemDiv.appendChild(contentDiv);
        itemDiv.appendChild(restoreDiv);
        itemDiv.appendChild(hr);
        container.appendChild(itemDiv);
    });
};
//# sourceMappingURL=passageRenderUtils.js.map