package com.kwonjh0406.finall.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwonjh0406.finall.model.Exchange;
import com.kwonjh0406.finall.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * 개발 순서
 * 1. 환율 정보 JSON 받아오기
 * 2. JSON 에서 원-달러 부분만 파싱
 * <p>
 * 개발 제약
 * 1. 환율 정보는 하루 호출 횟수가 제한되어 있으므로 5분 간격으로 1회만 최신화
 * 2. 환율 정보 조회 가능 시간대를 벗어난 경우 조회 X
 * 3.
 * <p>
 * 개발 메모
 * 1. DB에 환율을 저장하면 최신화 되기 전까지 매번 DB에서 새로 쿼리를 안날리고 캐시 데이터로 해결이 될까?
 */
@Service
public class ExchangeService {

    private static String authkey = "7BrtqTpIuY52NbLKJRc30ZXHLGx1cZLY";

    @Autowired
    ExchangeRepository exchangeRepository;

    // 메모리 저장소에 환율 정보 저장
    // DB 저장소에 환율 정보 저장
    // 1차적으로 메모리에서 환율을 가져와서 출력함 컨트롤러는
    // 근데 메모리에 환율이 깨진 경우 db에서 가져옴
    @Scheduled(fixedRate = 300000)
    public void getExchange() {
        try {
            URL url = new URL("https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + authkey + "&searchdate=20240730&data=AP01");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");


            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                String response = sb.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonArray = objectMapper.readTree(response);

                // "USD" 통화의 "ttb" 값 찾기
                Iterator<JsonNode> iterator = jsonArray.elements();
                while (iterator.hasNext()) {
                    JsonNode jsonNode = iterator.next();
                    if ("USD".equals(jsonNode.get("cur_unit").asText())) {

//                        exchangeRepository.deleteByTicker("USD");

                        Exchange exchange = new Exchange();

                        exchange.setTicker("USD");
                        exchange.setPrice(jsonNode.get("ttb").asText());

                        exchangeRepository.save(exchange);
                    }
                }

            } else {

            }


        } catch (Exception e) {
            System.out.println("ExchangeService 클래스, Exception on getExchange() method");
            e.printStackTrace();
        }

    }
}