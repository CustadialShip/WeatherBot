package com.dshard.weatherbot.bot.operation.impl.message;

import com.dshard.weatherbot.bot.operation.MessageOperationRule;
import com.dshard.weatherbot.bot.operation.UpdateType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartMessageOperation implements MessageOperationRule {

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public UpdateType getUpdateType() {
        return UpdateType.MESSAGE;
    }

    @Override
    public SendMessage construct(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Привет, ты начал пользоваться ботом!")
                .build();
    }
}
