import { getElementById } from './domUtils.js';

export const renderNickname = (elementId: string, nickname: string) => {
    const element = getElementById(elementId);
    element.textContent = nickname + '님의 Bookmarky';
}

export const renderBooks = (books: Book[]) => {
    const container = getElementById('book-list-container');
    container.innerHTML = '';

    books.forEach(book => {
        const bookDiv = document.createElement('div');
        bookDiv.classList.add('book-item');

        const imgElement = document.createElement('img');
        imgElement.src = book.image;
        imgElement.alt = book.title;
        imgElement.style.width = '150px';
        imgElement.id = book.id;

        bookDiv.addEventListener('click', () => {
            window.location.href = `../book/passage-list.html?id=${book.id}`;
        });

        bookDiv.appendChild(imgElement);
        container.appendChild(bookDiv);
    })
}

export const renderBookDetail = (book: Book) => {
    const container = document.getElementById('book-detail-container');
    container.innerHTML = '';

    const bookDiv = document.createElement('div');
    bookDiv.id = book.id;
    bookDiv.className = 'book-item';

    const imgElement = document.createElement('img');
    imgElement.src = book.image;
    imgElement.alt = book.title;
    imgElement.style.width = '150px';
    imgElement.id = book.id;

    const titleElement = document.createElement('h3');
    titleElement.innerText = `${book.title}`;

    // 설명 100자만 출력 + 더보기를 통해 전체 출력
    const descriptionElement = document.createElement('span');
    const shortContent = book.description.length > 100 ? book.description.substring(0, 100) + '...   ' : book.description;
    descriptionElement.innerText = shortContent;

    // 더보기
    const moreContent = document.createElement('a');
    moreContent.innerText = '더보기';
    moreContent.style.cursor = 'pointer';

    // 닫기
    const closeContent = document.createElement('a');
    closeContent.innerText = '닫기';
    closeContent.style.cursor = 'pointer';
    closeContent.style.display = 'none';

    moreContent.addEventListener('click', () => {
        descriptionElement.innerText = book.description + '   ';
        moreContent.style.display = 'none';
        closeContent.style.display = 'inline';
    });

    closeContent.addEventListener('click', () => {
        descriptionElement.innerText = shortContent;
        moreContent.style.display = 'inline';
        closeContent.style.display = 'none';
    });

    const authorElement = document.createElement('p');
    authorElement.innerText = `✍️ 저자: ${book.author}`;

    const publisherElement = document.createElement('p');
    publisherElement.innerText = `📚 출판사: ${book.publisher}`;

    const linkElement = document.createElement('a');
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
}