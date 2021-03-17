package harjoittelu_bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Harjoittelu_bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
		
		

	}

}
