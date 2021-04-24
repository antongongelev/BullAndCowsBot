import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    private Game game;

    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (game != null && game.isActive() && Game.isGameNumber(message.getText())) {
                // here we go - lets give an answer;
                int current = game.getSteps();
                game.setSteps(++current);
                if (game.isAnswerValid(message.getText())) {
                    sendMessage(message, String.format("Ты победил, но тебе %s, неудачник", Game.getTryNumberMsg(game.getSteps())));
                    game.endGame();
                } else {
                    game.count(message.getText());
                    sendMessage(message, String.format("Неа, давай другое чило. Быки: %s. Коровы: %s", game.getBulls(), game.getCows()));
                }
            } else {
                switch (message.getText()) {
                    case "/play":
                        game = new Game();
                        sendMessage(message, String.format("Поехали. Я загад четыре цифры - %s. Угадаешь?", game.getNumber()));
                        break;
                    case "/help":
                        sendMessage(message, "Ждешь помощи? - Тут все просто. Мы с тобой сыграем в игру 'Быки и коровы'");
                        break;
                    default:
                        sendMessage(message, "Я тебя не понимаю");
                        break;
                }
            }
        }

    }

    public void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setHelpButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setHelpButtons(SendMessage message) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(markup);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("/play"));
        row.add(new KeyboardButton("/help"));
        rows.add(row);
        markup.setKeyboard(rows);

    }

    public String getBotUsername() {
        return Constants.BOT_NAME;
    }

    public String getBotToken() {
        return Constants.BOT_TOKEN;
    }
}
