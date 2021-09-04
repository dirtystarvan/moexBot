package ru.sbrf.trade.data.PollingBot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sbrf.trade.data.bh.StockDataPipeline;

import java.io.IOException;
import java.sql.SQLException;

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
                    pipeline.rangePipeline(splitted[1]);

                    execute(new SendMessage(String.valueOf(chatId), "FUCK"));
                    break;
                case "r":
                    break;
            }

            execute(new SendMessage(String.valueOf(chatId), receivedMessage));
        } catch (TelegramApiException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}