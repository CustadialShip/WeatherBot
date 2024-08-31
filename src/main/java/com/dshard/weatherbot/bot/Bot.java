package com.dshard.weatherbot.bot;

import com.dshard.weatherbot.bot.operation.MessageOperationRule;
import com.dshard.weatherbot.bot.operation.OperationRule;
import com.dshard.weatherbot.bot.operation.UpdateType;
import com.dshard.weatherbot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final List<OperationRule> operationRules;


    @Override
    public void onUpdateReceived(Update update) {
        UpdateType updateType = getUpdateType(update);
        Consumer<SendMessage> executeHandler = a -> {
            try {
                execute(a);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        };
        switch (updateType) {
            case MESSAGE -> executeMessages(update, executeHandler);
            default -> throw new IllegalStateException();
        }
    }

    private void executeMessages(Update update, Consumer<SendMessage> execute) {
        List<MessageOperationRule> executeOperationRules = operationRules.stream()
                .filter(r -> r.getUpdateType() == UpdateType.MESSAGE)
                .map(r -> (MessageOperationRule) r)
                .filter(r -> r.getCommand().equals(update.getMessage().getText()))
                .toList();

        for (MessageOperationRule r : executeOperationRules) {
            execute.accept(r.construct(update));
        }
    }

    private UpdateType getUpdateType(Update update) {
        if (update.hasMessage()) {
            return UpdateType.MESSAGE;
        } else if (update.hasInlineQuery()) {
            return UpdateType.INLINE_QUERY;
        } else if (update.hasChosenInlineQuery()) {
            return UpdateType.CHOSEN_INLINE_QUERY;
        } else if (update.hasCallbackQuery()) {
            return UpdateType.CALL_BACK_QUERY;
        } else if (update.hasEditedMessage()) {
            return UpdateType.EDITED_MESSAGE;
        } else if (update.hasChannelPost()) {
            return UpdateType.CHANNEL_POST;
        } else if (update.hasEditedChannelPost()) {
            return UpdateType.EDITED_CHANNEL_POST;
        } else if (update.hasShippingQuery()) {
            return UpdateType.SHIPPING_QUERY;
        } else if (update.hasPreCheckoutQuery()) {
            return UpdateType.PRE_CHECKOUT_QUERY;
        } else if (update.hasPoll()) {
            return UpdateType.POLL;
        } else if (update.hasPollAnswer()) {
            return UpdateType.POLL_ANSWER;
        } else if (update.hasMyChatMember()) {
            return UpdateType.MY_CHAT_MEMBER;
        } else if (update.hasChatMember()) {
            return UpdateType.CHAT_MEMBER;
        } else if (update.hasChatJoinRequest()) {
            return UpdateType.CHAT_JOIN_REQUEST;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
