import { getElementById } from './domUtils.js';

export const renderNickname = (elementId: string, nickname: string) => {
    const element = getElementById(elementId);
    element.textContent = nickname + '님의 Bookmarky';
}

export const renderBooks = (books: Book[]) => {

    if (books == null) {
        console.log("없음");
    } else {
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
                window.location.href = `../passage/all.html?id=${book.id}`;
            });

            bookDiv.appendChild(imgElement);
            container.appendChild(bookDiv);
        })
    }
    
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

export const renderSearchBooks = (title: string, books: Book[]) => {
    const titleDiv = document.getElementById('search-title-div');
    
    if (titleDiv.children.length == 0) { // 페이지의 제목은 한 번만 출력
        const titleH = document.createElement('h1');
        titleH.innerText = title;
        const noticeMessage = document.createElement('p');
        noticeMessage.innerText = '연관도가 높은 순으로 조회되며, 선택한 책은 자동 등록됩니다.';

        titleDiv.appendChild(titleH);
        titleDiv.appendChild(noticeMessage);
    }

    const resultDiv = document.getElementById('search-result-div');

    books.forEach((book) => {
        const bookDiv = document.createElement('div');
        bookDiv.className = 'book-item';

        const img = document.createElement('img');
        img.src = book.image;
        img.alt = book.title;
        img.style.cursor = 'pointer';
        img.addEventListener('click', () => {
            window.open(book.link, '_blank'); // 이미지 클릭 시 link로 이동
        });

        const title = document.createElement('p');
        title.innerText = book.title;

        const authorDiv = document.createElement('div');
        const authorLabel = document.createElement('label');
        authorLabel.innerText = '저자';
        const authorP = document.createElement('p');
        authorP.innerText = book.author;

        authorDiv.appendChild(authorLabel);
        authorDiv.appendChild(authorP);

        const publisherDiv = document.createElement('div');
        const publisherLabel = document.createElement('label');
        publisherLabel.innerText = '출판';
        const publisherP = document.createElement('p');
        publisherP.innerText = book.publisher;

        publisherDiv.appendChild(publisherLabel);
        publisherDiv.appendChild(publisherP);

        const checkboxLabel = document.createElement('label');
        checkboxLabel.innerText = 'Select';
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkboxLabel.appendChild(checkbox);

        checkbox.addEventListener('change', () => {
            if (checkbox.checked) {
                const confirm = window.confirm("책을 선택했습니다. 이전 페이지로 이동하시겠습니까?");
                if (confirm) {
                    localStorage.setItem('title', book.title);
                    localStorage.setItem('isbn', book.isbn);
                    window.history.back();
                } else {
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
    let pageBtn = document.getElementById('page-btn');
    if (!pageBtn) {
        pageBtn = document.createElement('button');
        pageBtn.id = 'page-btn';
        pageBtn.innerText = '더보기';

        pageBtn.addEventListener('click', async () => {
            const { searchBooksProcess } = await import('../components/SearchBookForm.js');
            renderSearchBooks(title, await searchBooksProcess(title));
        });
    }

    resultDiv.appendChild(pageBtn);
};