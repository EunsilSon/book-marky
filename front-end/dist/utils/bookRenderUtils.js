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
import { getElementById } from './domUtils.js';
import { searchBooksProcess } from '../components/SearchBookForm.js';
export var renderNickname = function (elementId, nickname) {
    var element = getElementById(elementId);
    element.textContent = nickname + '님의 Bookmarky';
};
export var renderBooks = function (books) {
    var container = getElementById('book-list-container');
    container.innerHTML = '';
    books.forEach(function (book) {
        var bookDiv = document.createElement('div');
        bookDiv.classList.add('book-item');
        var imgElement = document.createElement('img');
        imgElement.src = book.image;
        imgElement.alt = book.title;
        imgElement.style.width = '150px';
        imgElement.id = book.id;
        bookDiv.addEventListener('click', function () {
            window.location.href = "../passage/all.html?id=".concat(book.id);
        });
        bookDiv.appendChild(imgElement);
        container.appendChild(bookDiv);
    });
};
export var renderBookDetail = function (book) {
    var container = document.getElementById('book-detail-container');
    container.innerHTML = '';
    var bookDiv = document.createElement('div');
    bookDiv.id = book.id;
    bookDiv.className = 'book-item';
    var imgElement = document.createElement('img');
    imgElement.src = book.image;
    imgElement.alt = book.title;
    imgElement.style.width = '150px';
    imgElement.id = book.id;
    var titleElement = document.createElement('h3');
    titleElement.innerText = "".concat(book.title);
    // 설명 100자만 출력 + 더보기를 통해 전체 출력
    var descriptionElement = document.createElement('span');
    var shortContent = book.description.length > 100 ? book.description.substring(0, 100) + '...   ' : book.description;
    descriptionElement.innerText = shortContent;
    // 더보기
    var moreContent = document.createElement('a');
    moreContent.innerText = '더보기';
    moreContent.style.cursor = 'pointer';
    // 닫기
    var closeContent = document.createElement('a');
    closeContent.innerText = '닫기';
    closeContent.style.cursor = 'pointer';
    closeContent.style.display = 'none';
    moreContent.addEventListener('click', function () {
        descriptionElement.innerText = book.description + '   ';
        moreContent.style.display = 'none';
        closeContent.style.display = 'inline';
    });
    closeContent.addEventListener('click', function () {
        descriptionElement.innerText = shortContent;
        moreContent.style.display = 'inline';
        closeContent.style.display = 'none';
    });
    var authorElement = document.createElement('p');
    authorElement.innerText = "\u270D\uFE0F \uC800\uC790: ".concat(book.author);
    var publisherElement = document.createElement('p');
    publisherElement.innerText = "\uD83D\uDCDA \uCD9C\uD310\uC0AC: ".concat(book.publisher);
    var linkElement = document.createElement('a');
    linkElement.href = book.link;
    linkElement.innerText = '🔗 Connect to link';
    linkElement.target = '_blank';
    bookDiv.appendChild(imgElement);
    bookDiv.appendChild(titleElement);
    bookDiv.appendChild(descriptionElement);
    bookDiv.appendChild(moreContent);
    bookDiv.appendChild(closeContent);
    bookDiv.appendChild(authorElement);
    bookDiv.appendChild(publisherElement);
    bookDiv.appendChild(linkElement);
    container.appendChild(bookDiv);
};
export var renderSearchBooks = function (title, books) {
    var titleDiv = document.getElementById('search-title-div');
    if (titleDiv.children.length == 0) { // 페이지의 제목은 한 번만 출력
        var titleH = document.createElement('h1');
        titleH.innerText = title;
        var noticeMessage = document.createElement('p');
        noticeMessage.innerText = '연관도가 높은 순으로 조회되며, 선택한 책은 자동 등록됩니다.';
        titleDiv.appendChild(titleH);
        titleDiv.appendChild(noticeMessage);
    }
    var resultDiv = document.getElementById('search-result-div');
    books.forEach(function (book) {
        var bookDiv = document.createElement('div');
        bookDiv.className = 'book-item';
        var img = document.createElement('img');
        img.src = book.image;
        img.alt = book.title;
        img.style.cursor = 'pointer';
        img.addEventListener('click', function () {
            window.open(book.link, '_blank'); // 이미지 클릭 시 link로 이동
        });
        var title = document.createElement('p');
        title.innerText = book.title;
        var authorDiv = document.createElement('div');
        var authorLabel = document.createElement('label');
        authorLabel.innerText = '저자';
        var authorP = document.createElement('p');
        authorP.innerText = book.author;
        authorDiv.appendChild(authorLabel);
        authorDiv.appendChild(authorP);
        var publisherDiv = document.createElement('div');
        var publisherLabel = document.createElement('label');
        publisherLabel.innerText = '출판';
        var publisherP = document.createElement('p');
        publisherP.innerText = book.publisher;
        publisherDiv.appendChild(publisherLabel);
        publisherDiv.appendChild(publisherP);
        var checkboxLabel = document.createElement('label');
        checkboxLabel.innerText = 'Select';
        var checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkboxLabel.appendChild(checkbox);
        checkbox.addEventListener('change', function () {
            if (checkbox.checked) {
                var confirm_1 = window.confirm("책을 선택했습니다. 이전 페이지로 이동하시겠습니까?");
                if (confirm_1) {
                    localStorage.setItem('title', book.title);
                    localStorage.setItem('isbn', book.isbn);
                    window.history.back();
                }
                else {
                    localStorage.removeItem('title');
                    localStorage.removeItem('isbn');
                }
            }
        });
        bookDiv.appendChild(img);
        bookDiv.appendChild(title);
        bookDiv.appendChild(authorDiv);
        bookDiv.appendChild(publisherDiv);
        bookDiv.appendChild(checkboxLabel);
        resultDiv.appendChild(bookDiv);
    });
    // 더보기 버튼
    var pageBtn = document.getElementById('page-btn');
    if (!pageBtn) {
        pageBtn = document.createElement('button');
        pageBtn.id = 'page-btn';
        pageBtn.innerText = '더보기';
        pageBtn.addEventListener('click', function () { return __awaiter(void 0, void 0, void 0, function () {
            var _a, _b;
            return __generator(this, function (_c) {
                switch (_c.label) {
                    case 0:
                        _a = renderSearchBooks;
                        _b = [title];
                        return [4 /*yield*/, searchBooksProcess(title)];
                    case 1:
                        _a.apply(void 0, _b.concat([_c.sent()]));
                        return [2 /*return*/];
                }
            });
        }); });
    }
    resultDiv.appendChild(pageBtn);
};
//# sourceMappingURL=bookRenderUtils.js.map