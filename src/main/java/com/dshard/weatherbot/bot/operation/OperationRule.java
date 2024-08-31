package com.dshard.weatherbot.bot.operation;


import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface OperationRule {

    UpdateType getUpdateType();

    BotApiMethod<? extends BotApiObject> construct(Update update);
}
