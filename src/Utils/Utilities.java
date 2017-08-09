package Utils;

import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

public class Utilities {
    static {
        String log4jConfPath = "conf/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    private static Logger log = LoggerFactory.getLogger(Utilities.class);

    public static String toTransliteration(String string) {
        final char[] ru = {' ', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ч', 'Ц', 'Ш', 'Щ', 'Э', 'Ю', 'Я', 'Ы', 'Ъ', 'Ь', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ч', 'ц', 'ш', 'щ', 'э', 'ю', 'я', 'ы', 'ъ', 'ь'};
        final String[] en = {" ", "A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "", "", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "", ""};
        final StringBuilder response = new StringBuilder(string.length());
        final HashMap<Character, String> table = new HashMap<>();

        int i = 0;
        for (char ch : ru) {
            table.put(ch, en[i++]);
        }

        for (i = 0; i < string.length(); i++) {
            String str = table.get(string.charAt(i));
            response.append(str == null ? "" : str);
        }

        return response.toString();
    }

    public static Document getDocument(String url) throws IOException {
        Document document = null;

            document = Jsoup
                    .connect(url)
                    .get();
        return document;
    }

    public static Document connectUrl(String stringURL) throws Exception {
        URL url = new URL(stringURL);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("212.237.36.234", 3128)); // or whatever your proxy is
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);

        uc.connect();

        String line;
        StringBuffer tmp = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }

        return Jsoup.parse(String.valueOf(tmp));
    }

    public static void iPChange(String proxy, String port) {
        Properties systemProperties = System.getProperties();
        systemProperties.put("proxySet", "true");
        systemProperties.setProperty("http.proxyHost", proxy);
        systemProperties.setProperty("http.proxyPort", port);
    }
}
