package com.eunsil.bookmarky.service.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class NaverOpenApiSearchBook {

    @Value("${spring.props.client-id}")
    private String clientId;
    @Value("${spring.props.client-secret}")
    private String clientSecret;

    /**
     * 책 검색
     * @param title 제목
     * @param page 가져올 페이지 번호
     * @return JSON 형태의 검색 결과 10개
     */
    public String book(String title, int page) {

        String text = URLEncoder.encode(title, StandardCharsets.UTF_8); // 검색 키워드와 인코딩 형식
        String apiURL = "https://openapi.naver.com/v1/search/book.json?display=10"
                + "&query=" + text
                + "&start=" + page;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        return get(apiURL, requestHeaders); // 결과 반환
    }

    /**
     * 책 상세 정보 검색
     * @param isbn 고유 바코드 번호
     * @return JSON 형태의 책 정보
     */
    public String bookDetail(String isbn) {

        String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?"
                + "d_isbn=" + isbn;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        return get(apiURL, requestHeaders); // 결과 반환
    }


    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl); // 연결

        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode(); // 응답 결과 받기

            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("[API 요청과 응답 실패]", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        System.out.println("[네이버 Open 책 API 연결]");

        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("[API URL이 잘못되었습니다] : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("[연결이 실패했습니다] : " + apiUrl, e);
        }
    }


    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString(); // 결과 출력

        } catch (IOException e) {
            throw new RuntimeException("[API 응답을 읽는 데 실패했습니다]", e);
        }
    }
}
