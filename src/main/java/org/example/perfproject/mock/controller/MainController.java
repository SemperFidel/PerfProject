package org.example.perfproject.mock.controller;

import jakarta.validation.Valid;
import org.example.perfproject.mock.dto.Currency;
import org.example.perfproject.mock.dto.RequestDTO;
import org.example.perfproject.mock.dto.ResponseDTO;
import org.example.perfproject.mock.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Контроллер для отправки информации о счетах
 */
@RestController
@RequestMapping("/info")
public class MainController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final BalanceService service;
    private static final Logger log = LoggerFactory.getLogger(MainController.class); //Логирование

    /**
     * @param kafkaTemplate - Для отправки сообщений в Кафку
     * @param objectMapper - Для сериализации
     * @param service - Бизнес-логика
     */
    public MainController
            (KafkaTemplate<String, String> kafkaTemplate,
             ObjectMapper objectMapper,
             BalanceService service) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.service = service;
    }

    /**
     * Отдаёт информацию о балансе
     * @param request - DTO запроса. Должен быть валидным (нет null-полей).
     * @return ResponseEntity<ResponseDTO> - DTO ответа как ResponseEntity.
     * Принимает и отдаёт только application/json!
     */
    @PostMapping(
            value = "/postBalances",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseDTO> postBalances(@Valid @RequestBody RequestDTO request) {
        int delayMs = ThreadLocalRandom.current().nextInt(1000, 2001);
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //Вызовы логики из сервиса
        BigDecimal maxLimit = service.calculateMaxLimit(request.clientId());
        BigDecimal balance = service.generateRandomBalance();
        Currency currency = service.getRandomCurrency();

        //Здесь создаётся ответ
        ResponseDTO response = new ResponseDTO(
                request.rqUid(),
                request.clientId(),
                request.account(),
                currency,
                balance,
                maxLimit
        );

        /*
          В этом блоке ответ сериализуется и отправляется в Кафку как JSON
         */
        try {
            String json = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("balances-topic", request.clientId().toString(), json);
            log.info("Сообщение отправлено в Kafka: {}", json);
        } catch (JacksonException e) {
            log.error("Ошибка сериализации JSON для Kafka", e);
        }

        return ResponseEntity.ok(response);
    }
}
