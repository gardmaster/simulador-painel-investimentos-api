package master.gard.config;

import jakarta.enterprise.context.ApplicationScoped;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@ApplicationScoped
public class Messages {

    private final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public String get(String key) {
        return bundle.getString(key);
    }

    public String format(String key, Object... args) {
        return MessageFormat.format(get(key), args);
    }

}
