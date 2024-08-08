package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.entity.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ParsingService {

    /**
     * JSON 형식의 OPEN API 응답 값을 Book 객체 리스트로 변환
     * @param responseBody
     * @return List<Book>
     */
    public List<Book> jsonToBookList(String responseBody) {
        List<Book> bookList = new ArrayList<>();

        try {
            // JSON 을 Java 로 변환하고 원하는 속성의 값만 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(responseBody);
            JsonNode items = node.path("items");

            // 추출한 값을 순차적으로 접근
            Iterator<JsonNode> elements = items.elements();
            while (elements.hasNext()) {
                JsonNode bookNode = elements.next();
                Book book = objectMapper.treeToValue(bookNode, Book.class); // Book 객체로 변환
                bookList.add(book);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }


    /**
     * XML 형식의 OPEN API 응답 값을 Book 객체로 변환
     * @param responseBody
     * @return Book
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public Book xmlToBook(String responseBody) throws Exception {

        // xml 파싱 -> 함수로 빼기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(responseBody)));

        // 책 정보 추출
        List<String> bookInfo = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if (i == 4 || i == 6) {
                continue;
            }

            String info = document
                    .getElementsByTagName("item")
                    .item(0)
                    .getChildNodes()
                    .item(i) // 가져올 값
                    .getTextContent();

            bookInfo.add(info); // title 0, link 1, image 2, author 3, publisher 5, isbn 7, description 8;
        }

        // book 객체 반환
        return Book.builder()
                .title(bookInfo.get(0))
                .link(bookInfo.get(1))
                .image(bookInfo.get(2))
                .author(bookInfo.get(3))
                .publisher(bookInfo.get(4))
                .isbn(bookInfo.get(5))
                .description(bookInfo.get(6))
                .build();
    }
}
