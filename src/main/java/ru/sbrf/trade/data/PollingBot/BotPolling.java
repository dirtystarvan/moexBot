package ru.sbrf.trade.data.PollingBot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sbrf.trade.data.bh.StockDataPipeline;
import ru.sbrf.trade.data.da.entity.ch.MoexDto;
import ru.sbrf.trade.data.da.entity.ch.MoexResultWrapper;
import ru.sbrf.trade.data.util.MsgBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
*
* Необходимо создать бота в telegram, в который будет отправляться дата в формате YYYY-MM-DD и получать ответ от сервиса в виде:
2021-03-20
ТИКЕР Open/High/Low/Close
ALRS 95.05/97.3/94.6/96.1
ROSN 438.05/433.1/444.6/440. 8

Лидер роста
SBER  +7%

Лидер падения
PLZL -10%

Реализовать два сервиса «Приемник» и «Мониторинг»:
1)  Приемник получает от телеграмм-бота дату и отдает результат в указанном виде.
Запрашивает от сервера статистики MOEX данные по торгам и отправляет их передатчик
2)  Передатчик получает данные от приемника и предоставляет список ТОП-5 лидеров роста за день
ТОП-5 лидеров падения за день

Если котировки акций изменились на 5% и более за день, то данную котировку нужно будет записать в файл
*
* */

@Component
public class BotPolling extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotPolling.class);

    private String botName;
    private String botToken;
    private StockDataPipeline pipeline;
    private RestTemplate consumerRest;

    public BotPolling(@Value("${telegram.bot.name}") String botName,
                      @Value("${telegram.bot.token}") String botToken,
                      @Qualifier("stockDataPipeline")StockDataPipeline stockDataPipeline,
                      @Qualifier("consumerRestTemplate")RestTemplate restTemplate) {
        this.botName = botName;
        this.botToken = botToken;
        this.pipeline = stockDataPipeline;
        this.consumerRest = restTemplate;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Long chatId = update.getMessage().getChatId();

            String receivedMessage = update.getMessage().getText();

            String[] splitted = receivedMessage.split("=");

            switch(splitted[0]) {
                case "t":
                    List<MoexDto> bounded = pipeline.rangePipeline(splitted[1]).stream()
                                    .limit(50).collect(Collectors.toList());
//                    execute(new SendMessage(String.valueOf(chatId), MsgBuilder.getMessage(bounded, splitted[1])));
//                    template.postForEntity("http://localhost:8080/shares-consumer", quotes, int.class);
                    ResponseEntity<MoexResultWrapper> restResult =
                            consumerRest.postForEntity("http://localhost:8080/shares-consumer", bounded, MoexResultWrapper.class);
                    execute(new SendMessage(String.valueOf(chatId), MsgBuilder.getDayAnalysisMessage(restResult.getBody())));
                    break;
                case "r":
                default:
                    break;
            }
        } catch (TelegramApiException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}