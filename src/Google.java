import Utils.Utilities;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

class Google implements Find {
    private static final int NUM_IN_REQUEST = 30;
    private static final String NAME = "google";

    @Override
    public ArrayList<UrlInfo> find(String requestName) throws Exception {
        String url = "https://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "&num=" + NUM_IN_REQUEST;
        Elements h3s;
        Elements h3Descriptions;
        Document doc = Utilities.getDocument(url); //connectUrl(url);  //getDocument(url);
        ArrayList<UrlInfo> urlInfoList = new ArrayList<>();

        h3s = doc.select("h3.r a");
        h3Descriptions = doc.select("span.st");

        for (int i = 0; i < h3s.size() && i < h3Descriptions.size(); i++) {
            urlInfoList.add(new UrlInfo(
                    NAME,
                    h3s.get(i).select("a").first().attr("abs:href"),
                    h3s.get(i).text(),
                    h3Descriptions.get(i).text()
            ));
        }
        System.out.println("google find ...");
        return urlInfoList;
    }
}
