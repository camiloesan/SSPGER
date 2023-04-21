package mx.uv.fei.logic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class TelegramMessenger {
    public static void sendToTelegram(String message) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        String apiToken = "x";
        String chatId = "x";
        urlString = String.format(urlString, apiToken, chatId, message);

        try {
            URL url = new URL(urlString);
            url.openConnection();
            URLConnection conn = url.openConnection();
            new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
