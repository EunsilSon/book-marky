package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.service.api.NaverOpenApiSearchBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private final NaverOpenApiSearchBook naverOpenApiSearchBook;

    @Autowired
    public BookService(NaverOpenApiSearchBook naverOpenApiSearchBook) {
        this.naverOpenApiSearchBook = naverOpenApiSearchBook;
    }

    public List<Book> search(String title, int page) {
        String responseBody = naverOpenApiSearchBook.callApi(title, page); // 오픈 API 응답 결과
        List<Book> bookList = new ArrayList<>();

        // Book 객체로 변환 후 리스트에 담음
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(responseBody);
            JsonNode items = node.path("items");

            Iterator<JsonNode> elements = items.elements();
            while (elements.hasNext()) {
                JsonNode bookNode = elements.next();
                Book book = objectMapper.treeToValue(bookNode, Book.class);
                bookList.add(book);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

}
