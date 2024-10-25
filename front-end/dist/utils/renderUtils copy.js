import { getElementById } from '../utils/domUtils.js';
export var renderSecureQuestion = function (containerId, question) {
    var container = getElementById(containerId);
    var p = document.createElement('p');
    p.textContent = question;
    container.appendChild(p);
};
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
            window.location.href = "../book/passage.html?id=".concat(book.id);
        });
        bookDiv.appendChild(imgElement);
        container.appendChild(bookDiv);
    });
};
//# sourceMappingURL=renderUtils%20copy.js.map