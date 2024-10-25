var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
import { getElementById, showAlert } from "./domUtils.js";
import { updatePassageProcess, deletePassageProcess, createPassageProcess, restorePassageProcess } from "../components/PassageForm.js";
import { getSavedBooks } from '../services/bookService.js';
export var renderPassages = function (passages) {
    var container = getElementById('passages-container');
    container.innerHTML = '';
    passages.forEach(function (passage) {
        var link = document.createElement('a');
        link.href = "../passage/detail.html?id=".concat(passage.id);
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
    // 책 제목
    var bookTitleDiv = document.createElement('div');
    var bookTitleLabel = document.createElement('label');
    bookTitleLabel.innerText = '책 제목';
    var bookTitleInput = document.createElement('input');
    bookTitleInput.id = 'book-title';
    var selectedTitle = localStorage.getItem('title'); // 책 검색을 통해 선택한 책의 제목 출력
    if (selectedTitle) {
        console.log(selectedTitle);
        bookTitleInput.value = selectedTitle;
        //localStorage.removeItem('title');
    }
    bookTitleDiv.appendChild(bookTitleLabel);
    bookTitleDiv.appendChild(bookTitleInput);
    // 이미 저장된 책 버튼
    var getBookDiv = document.createElement('div');
    var getBookBtn = document.createElement('button');
    getBookBtn.innerText = '저장된 책 보기';
    getBookBtn.addEventListener('click', function () { return __awaiter(void 0, void 0, void 0, function () {
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, renderOptions()];
                case 1:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    }); });
    getBookDiv.appendChild(getBookBtn);
    // 찾기 버튼
    var searchBookDiv = document.createElement('div');
    var searchBookBtn = document.createElement('button');
    searchBookBtn.innerText = '찾기';
    searchBookBtn.addEventListener('click', function () {
        var title = bookTitleInput.value;
        if (title) {
            window.location.href = "../book/search.html?title=".concat(bookTitleInput.value);
        }
        else {
            showAlert('검색 할 책 제목을 입력하세요.');
        }
    });
    searchBookDiv.appendChild(searchBookBtn);
    // 구절 내용
    var contentDiv = document.createElement('div');
    var contentTextarea = document.createElement('textarea');
    contentTextarea.placeholder = '구절 내용을 입력하세요.';
    contentTextarea.rows = 4;
    contentTextarea.style.resize = 'none';
    contentDiv.appendChild(contentTextarea);
    // 쪽수
    var pageDiv = document.createElement('div');
    var pageNumInput = document.createElement('input');
    pageNumInput.placeholder = '쪽수를 입력하세요';
    pageDiv.appendChild(pageNumInput);
    // 저장 버튼
    var saveDiv = document.createElement('div');
    var saveButton = document.createElement('button');
    saveButton.innerText = '저장';
    saveButton.addEventListener('click', function () {
        var isbn = localStorage.getItem('isbn');
        var content = contentTextarea.value;
        var pageNum = pageNumInput.value;
        localStorage.removeItem('isbn');
        createPassageProcess(isbn, content, pageNum);
        showAlert('생성이 완료되었습니다. 메인 페이지로 이동합니다.');
        window.location.href = "../book/index.html";
    });
    saveDiv.appendChild(saveButton);
    container.appendChild(bookTitleDiv);
    container.appendChild(getBookDiv);
    container.appendChild(searchBookDiv);
    container.appendChild(contentDiv);
    container.appendChild(pageDiv);
    container.appendChild(saveDiv);
    // 옵션 렌더링 함수
    var renderOptions = function () { return __awaiter(void 0, void 0, void 0, function () {
        var response, bookTitles, existingSelect, selectElement_1, error_1;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    _a.trys.push([0, 2, , 3]);
                    return [4 /*yield*/, getSavedBooks()];
                case 1:
                    response = _a.sent();
                    console.log(response);
                    bookTitles = response.data;
                    existingSelect = document.getElementById('book-title-options');
                    if (existingSelect) {
                        existingSelect.remove();
                    }
                    selectElement_1 = document.createElement('select');
                    selectElement_1.id = 'book-title-options';
                    selectElement_1.innerHTML = '<option value="">책을 선택하세요</option>';
                    bookTitles.forEach(function (item) {
                        var option = document.createElement('option');
                        option.value = item.isbn;
                        option.innerText = item.title;
                        selectElement_1.appendChild(option);
                    });
                    getBookDiv.appendChild(selectElement_1);
                    selectElement_1.addEventListener('change', function (event) {
                        var target = event.target; // 타입 단언
                        var selectedOption = target.options[target.selectedIndex];
                        var selectedISBN = selectedOption.value;
                        var selectedTitle = selectedOption.innerText;
                        if (selectedTitle) {
                            bookTitleInput.value = selectedTitle;
                            selectElement_1.style.display = 'none';
                            localStorage.setItem('isbn', selectedISBN);
                        }
                    });
                    return [3 /*break*/, 3];
                case 2:
                    error_1 = _a.sent();
                    console.error('데이터를 가져오는 중 오류 발생:', error_1);
                    return [3 /*break*/, 3];
                case 3: return [2 /*return*/];
            }
        });
    }); };
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
        itemDiv.appendChild(bookDiv);
        itemDiv.appendChild(contentDiv);
        itemDiv.appendChild(restoreDiv);
        container.appendChild(itemDiv);
    });
};
//# sourceMappingURL=passageRenderUtils.js.map