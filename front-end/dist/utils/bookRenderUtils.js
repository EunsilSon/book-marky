import { getElementById } from './domUtils.js';
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
            window.location.href = "../book/passage-list.html?id=".concat(book.id);
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
        descriptionElement.innerText = book.description + '...   ';
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
//# sourceMappingURL=bookRenderUtils.js.map