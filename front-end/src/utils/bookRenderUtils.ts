declare var swal: any;

import { getElementById } from './domUtils.js';
import { getNextBooksProcess, getPrevBooksProcess, getPageNumber } from '../components/BookForm.js';
import { getBookCount } from '../services/bookService.js';

export const renderBookCount = (count: number) => {
    const bookCount = getElementById('book-count');
    bookCount.innerText = '현재 저장된 책: ' + count +'권';
}

const setupNavigationButtons = (currentPage: number) => {
    const prevButton = document.getElementById('prev') as HTMLButtonElement;
    const nextButton = document.getElementById('next') as HTMLButtonElement;

    if (!prevButton.hasAttribute('data-listener')) {
        prevButton.addEventListener('click', async () => {
            await getPrevBooksProcess();
        });
        prevButton.setAttribute('data-listener', 'true');
    }

    if (!nextButton.hasAttribute('data-listener')) {
        nextButton.addEventListener('click', async () => {
            await getNextBooksProcess();
        });
        nextButton.setAttribute('data-listener', 'true');
    }

    // 현재 페이지에 따라 버튼 활성화 상태 변경
    prevButton.disabled = currentPage <= 0;

    getBookCount()
    .then(bookCount => {
        const totalPages = Math.round(bookCount.data / 5);
        nextButton.disabled = currentPage >= totalPages;
    })
    .catch(error => {
        console.error('ERROR total book page:', error);
    });
};

const renderBookList = (container: HTMLElement, books: Book[]) => {
    container.innerHTML = '';

    if (books) {
        const div = document.createElement('div');
        div.classList.add('book-list');

        books.forEach(book => {
            const imgElement = document.createElement('img');
            imgElement.src = book.image;
            imgElement.alt = book.title;

            imgElement.addEventListener('click', () => {
                window.location.href = `../passage/all.html?id=${book.id}`;
            });

            div.appendChild(imgElement);
        });

        container.appendChild(div);
    } else {
        container.innerHTML = '<p>비어있음</p>';
    }
};

export const renderBooks = (books: Book[]) => {
    const container = document.getElementById('book-list-container');
    setupNavigationButtons(getPageNumber());
    renderBookList(container, books);
};

export const renderBookDetail = (book: Book) => {
    const container = document.getElementById('book-detail-container');
    container.innerHTML = '';

    const bookDiv = document.createElement('div');
    bookDiv.id = book.id;
    bookDiv.id = 'book-detail';

    // 표지
    const imgElement = document.createElement('img');
    imgElement.src = book.image;
    imgElement.alt = book.title;
    imgElement.id = book.id;

    imgElement.addEventListener('click', () => {
        window.open(book.link, '_blank');
    });

    // 제목
    const titleElement = document.createElement('h3');
    titleElement.innerText = `${book.title}`;


    // 저자
    const authorBox = document.createElement('div');
    authorBox.classList.add('detail-item');
    const authorLabel = document.createElement('label');
    authorLabel.innerText = '✍️ 저자';
    const authorElement = document.createElement('p');
    authorElement.innerText = `${book.author}`;

    authorBox.appendChild(authorLabel);
    authorBox.appendChild(authorElement);

    // 출판
    const publisherBox = document.createElement('div');
    publisherBox.classList.add('detail-item');
    const publisherLabel = document.createElement('label');
    publisherLabel.innerText = '📚 출판사';
    const publisherElement = document.createElement('p');
    publisherElement.innerText = `${book.publisher}`;

    publisherBox.appendChild(publisherLabel);
    publisherBox.appendChild(publisherElement);

    // 설명
    const descriptionBox = document.createElement('div');
    descriptionBox.id = 'description';
    const descriptionLabel = document.createElement('label');
    descriptionLabel.innerText = '📄 설명';
    
    const description = document.createElement('div');
    description.id ='description-content';

    const shortContent = book.description.length > 100 ? book.description.substring(0, 100) + '...   ' : book.description;
    description.innerText = shortContent;

    const moreContent = document.createElement('a');
    moreContent.innerText = '더보기';
    moreContent.id = 'more-btn';

    const closeContent = document.createElement('a');
    closeContent.innerText = '닫기';
    closeContent.id = 'close-btn';
    closeContent.style.display = 'none';

    moreContent.addEventListener('click', () => {
        description.innerText = book.description + '   ';
        moreContent.style.display = 'none';
        closeContent.style.display = 'inline';
    });

    closeContent.addEventListener('click', () => {
        description.innerText = shortContent;
        moreContent.style.display = 'inline';
        closeContent.style.display = 'none'; 
    });

    descriptionBox.appendChild(descriptionLabel);
    descriptionBox.appendChild(description);
    descriptionBox.appendChild(moreContent);
    descriptionBox.appendChild(closeContent);

    bookDiv.appendChild(imgElement);
    bookDiv.appendChild(titleElement);
    bookDiv.appendChild(authorBox);
    bookDiv.appendChild(publisherBox);
    bookDiv.appendChild(descriptionBox);
    container.appendChild(bookDiv);
}

export const renderSearchBooks = (title: string, books: Book[]) => {
    localStorage.setItem('search-title', title);
    renderSearchBookTitle(title); // 제목은 한 번만 출력
    renderSearchBookResult(books); // 검색 결과

    let pageBtn = document.getElementById('search-page-btn');
    pageBtn.addEventListener('click', async () => {
        const { searchBooksProcess } = await import('../components/SearchBookForm.js');
        renderSearchBookResult(await searchBooksProcess(localStorage.getItem('search-title')));
    });
}

const renderSearchBookTitle = (title: string) => {
    const bookTitle = getElementById('search-title');
    bookTitle.innerText = '제목: ' + title;
}

const renderSearchBookResult = (books: Book[]) => {
    const resultContainer = getElementById('search-container');

    books.forEach((book) => {
        const bookItem = document.createElement('div');
        bookItem.className = 'book-search-item';

        // cover: 책 표지 영역
        const cover = document.createElement('div');
        cover.className = 'search-cover';

        const img = document.createElement('img');
        img.src = book.image;
        img.alt = book.title;
        img.addEventListener('click', () => {
            window.open(book.link, '_blank');
        });

        cover.appendChild(img);

        // info: 책 정보 영역
        const info = document.createElement('div');
        info.className = 'search-info';

        const titleDiv = document.createElement('div');
        const titleLabel = document.createElement('label');
        titleLabel.innerText = book.title;
        titleLabel.id = 'book-title';

        titleDiv.appendChild(titleLabel);

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

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';

        checkbox.addEventListener('change', () => {
            if (checkbox.checked) {
                swal({
                    title: "책 선택 완료",
                    text: "이전 페이지로 이동하시겠습니까?",
                    icon: "success",
                    buttons: ["다시 선택하기", "OK"]
                  })
                  .then((willMove) => {
                    if (willMove) {
                        localStorage.setItem('title', book.title);
                        localStorage.setItem('isbn', book.isbn);
                        localStorage.removeItem('search-title');
                        window.location.href = '/html/passage/create.html';
                    } else {
                        localStorage.removeItem('title');
                        localStorage.removeItem('isbn');
                        checkbox.checked = false;
                    }
                  });
            }
        });

        info.appendChild(titleDiv);
        info.appendChild(authorDiv);
        info.appendChild(publisherDiv);

        bookItem.appendChild(checkbox);
        bookItem.appendChild(cover);
        bookItem.appendChild(info);
        resultContainer.appendChild(bookItem);
    });

};