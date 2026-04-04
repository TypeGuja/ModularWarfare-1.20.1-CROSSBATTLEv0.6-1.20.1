package com.modularwarfare.common.protector;

import com.modularwarfare.ModularWarfare;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.ZipFile;

public class ModularProtector {
    public HashMap<String, String> passwords = new HashMap<>();

    public void requestPassword(String contentpackName) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://modularwarfare.com/api/pass_request.php"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "key=fjd3vkuw#KURefg&contentpack_name=" + contentpackName))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String pass = response.body();
            this.passwords.put(contentpackName, pass);
        } catch (IOException | InterruptedException e) {
            ModularWarfare.LOGGER.info("A critical error occured opening " + contentpackName + ", please verify your internet connection.");
            e.printStackTrace();
        }
    }

    public boolean passwordExists(String contentpackName) {
        return passwords.containsKey(contentpackName);
    }

    public String getPassword(String contentpackName) {
        if (passwordExists(contentpackName)) {
            return passwords.get(contentpackName);
        }
        return null;
    }

    public String getDecoded(String password) {
        return new String(Base64.getDecoder().decode(password));
    }

    public void applyPassword(ZipFile file, String contentpackName) {
        // java.util.zip.ZipFile не поддерживает пароли
        // Этот метод ничего не делает при использовании стандартного ZipFile
        ModularWarfare.LOGGER.info("Password protection not supported with standard ZipFile");
    }
}