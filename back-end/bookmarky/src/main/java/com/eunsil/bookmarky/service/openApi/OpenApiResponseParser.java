package com.eunsil.bookmarky.service.openApi;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.global.exception.CustomParsingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OpenApiResponseParser {

    /**
     * JSON -> Book 객체 리스트 변환
     * @param responseBody JSON 형식의 open api 응답 값
     */
    public List<BookDTO> jsonToBookList(String responseBody) {
        List<BookDTO> bookList = new ArrayList<>();

        try {
            // JSON 을 Java 로 변환하고 원하는 속성의 값만 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(responseBody);
            JsonNode items = node.path("items");

            // 추출한 값을 순차적으로 접근
            Iterator<JsonNode> elements = items.elements();
            while (elements.hasNext()) {
                JsonNode bookNode = elements.next();
                BookDTO bookDTO = objectMapper.treeToValue(bookNode, BookDTO.class); // Book 객체로 변환
                bookList.add(bookDTO);
            }
            return bookList;
        } catch (Exception e) {
            throw new CustomParsingException("JSON Parsing Failed.");
        }
    }


    /**
     * XML -> Book 객체 변환
     * @param responseBody XML 형식의 open api 응답 값
     */
    public BookDTO xmlToBook(String responseBody) {

        try {
            // xml 파싱 -> 함수로 빼기
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseBody)));

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

            return BookDTO.builder()
                    .title(bookInfo.get(0))
                    .link(bookInfo.get(1))
                    .image(bookInfo.get(2))
                    .author(bookInfo.get(3))
                    .publisher(bookInfo.get(4))
                    .isbn(bookInfo.get(5))
                    .description(bookInfo.get(6))
                    .build();
        } catch (Exception e) {
            throw new CustomParsingException("XML Parsing Failed.");
        }
    }
}
