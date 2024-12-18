package com.eunsil.bookmarky.service.openApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class NaverOpenApiSearch {

    @Value("${spring.props.client-id}")
    private String clientId;
    @Value("${spring.props.client-secret}")
    private String clientSecret;
    @Value("${spring.props.api-url}")
    private String apiUrl;


    /**
     * 제목으로 책 여러 권 검색
     */
    public String searchBooksByTitle(String title, int page) {

        String text = URLEncoder.encode(title, StandardCharsets.UTF_8); // 검색 키워드와 인코딩 형식
        String apiURL = apiUrl + "/book.json?display=10" + "&query=" + text + "&start=" + page;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        return get(apiURL, requestHeaders); // 결과 반환
    }


    /**
     * isbn 으로 책 1권 검색
     */
    public String searchBookByIsbn(String isbn) {

        String apiURL = apiUrl + "/book_adv.xml?" + "d_isbn=" + isbn;

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
        log.info("Naver Open API Connected");

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
