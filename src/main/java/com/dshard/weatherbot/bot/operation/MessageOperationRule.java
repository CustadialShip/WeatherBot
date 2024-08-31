package com.dshard.weatherbot.bot.operation;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageOperationRule extends OperationRule {

    String getCommand();

    @Override
    SendMessage construct(Update update);
}
