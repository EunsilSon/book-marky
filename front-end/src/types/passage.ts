interface Passage {
    id: string;
    pageNum: string;
    content: string;
}

interface NewPassage {
    isbn: string;
    content: string;
    pageNum: string;
}

interface DeletedPassage {
    id: string;
    bookId: string;
    content: string;
    pageNum: string;
}