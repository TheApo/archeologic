package com.apogames.teavm;

import com.apogames.ArcheOLogic;
import com.apogames.Constants;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import org.teavm.jso.JSBody;

import java.util.Locale;

public class TeaVMLauncher {

    @JSBody(script = "return (navigator.languages && navigator.languages[0]) || navigator.language || navigator.userLanguage || 'en';")
    private static native String getBrowserLanguage();

    public static void main(String[] args) {
        Locale.setDefault(parseLocale(getBrowserLanguage()));
        Constants.IS_HTML = true;
        WebApplicationConfiguration config = new WebApplicationConfiguration();
        config.width = 0;
        config.height = 0;
        config.antialiasing = true;
        config.showDownloadLogs = true;
        config.preloadListener = assetLoader -> assetLoader.loadScript("freetype.js");
        new WebApplication(new ArcheOLogic(), config);
    }

    private static Locale parseLocale(String tag) {
        if (tag == null || tag.isEmpty()) {
            return Locale.ENGLISH;
        }
        String[] parts = tag.split("[-_]");
        if (parts.length >= 2) {
            return new Locale(parts[0], parts[1]);
        }
        return new Locale(parts[0]);
    }
}
