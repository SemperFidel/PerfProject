package org.example.perfproject.mock.service;

import org.example.perfproject.mock.dto.Currency;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Логика генерации инфы о балансе
 */
@Service
public class BalanceService {
    /**
     * Рассчитывает максимальный лимит баланса по ID
     * @param clientId - ID клиента. Должен приходить только как UUID, следить за этим!
     */
    public BigDecimal calculateMaxLimit(UUID clientId){
        char firstChar = clientId.toString().charAt(0);
        return switch (firstChar){ //Расчёт по первой цифре из ID
            case '8' -> BigDecimal.valueOf(2000);
            case '9' -> BigDecimal.valueOf(1000);
            default -> BigDecimal.valueOf(10000);
        };
    }

    /**
     * Генерирует рандомный баланс
     */
    public BigDecimal generateRandomBalance(){
        return BigDecimal.valueOf(100 + ThreadLocalRandom.current().nextInt(901));
    }

    /**
     * Выбирает рандомную валюту из Currency
     */
    public Currency getRandomCurrency() {
        Currency[] currencies = Currency.values();
        return currencies[ThreadLocalRandom.current().nextInt(currencies.length)];
    }
}
