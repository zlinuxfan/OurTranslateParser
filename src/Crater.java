import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static Utils.Utilities.getDocument;
import static Utils.Utilities.toTransliteration;

public class Crater {

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 5;
    private static final int COUNTER_PAGES_IN_FILE = 3;

    private static final ArrayList<URL> resources = readResource();
    private static final Logger log = LoggerFactory.getLogger(Crater.class);

    public static void main(String[] args) {
        try {
            createPages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createPages() throws IOException {
        ArrayList<Page> pages = new ArrayList<>();
        int counter = 1;
        int counterFiles = 1;

        for (URL url: resources) {

            Page page = createPage(url);
//            System.out.println("name: " + page.getNameOfElement());
//            System.out.println("text: " + page.getBriefDescriptionElement());
//            System.out.println("title: " + page.getElementTitle());
//            System.out.println("description: " + page.getElementDescription());
//            System.out.println("keyword: " + page.getElementKeywords());
//            System.out.println();

            pages.add(createPage(url));

            if (pages.size() == COUNTER_PAGES_IN_FILE || pages.size() == resources.size()) {
                String fileName = ((counterFiles*COUNTER_PAGES_IN_FILE)-COUNTER_PAGES_IN_FILE+1) +
                        "-" +
                        ((counterFiles*COUNTER_PAGES_IN_FILE)) +
                        ".csv";
                CsvFileWriter_Page.write("data/result/" + fileName, pages);
                pages.clear();
                counterFiles++;
                System.out.println("File name: " + fileName);
            }

            if (counter > 3) {
                break;
            }

            counter++;
        }
    }

    private static Page createPage(URL url) throws IOException {
        Document document = getDocument(url.toString());

        Elements head = document.select("head");
        Elements keywords = head.select("meta[name=keywords]");
        Elements description = head.select("meta[http-equiv=description]");
        Elements title = head.select("title");

        Elements ukPanelBox = document.select("div.uk-panel-box");
        Elements nameOfElement = ukPanelBox.select("h1");

        Elements div = ukPanelBox.select("div");
        Element div_1 = div.next().first();
        Element div_2 = div.next().next().first();
        Element div_3 = div.next().next().next().first();

        String textOfElement = ""; //String.valueOf(div_1) +
//                div_2 +
//                div_3 +
//                ukPanelBox.select("hr");

        ArrayList<OnceText> onceTexts = createOnceTexts();

        ArrayList<UrlInfo> urlInfos = createUrlInfos(ukPanelBox);

        return new Page.Builder(
                "",
                nameOfElement.html(),
                textOfElement,
                "",
                ((toTransliteration(nameOfElement.html())).replace(" ", "-")),
                onceTexts,
                urlInfos
        ).elementDescription(description.attr("content"))
                .elementTitle(title.text())
                .elementKeywords(keywords.attr("content"))
                .guidOfGroup("")
                .build();
    }

    private static ArrayList<OnceText> createOnceTexts() {
        ArrayList<OnceText> onceTexts = new ArrayList<>();

        for (int size = onceTexts.size(); size < NUMBER_TEXT_BOX; size++) {
            onceTexts.add(new OnceText("", false));
        }

        return onceTexts;
    }

    private static ArrayList<UrlInfo> createUrlInfos(Elements ukPanelBox) {
        ArrayList<UrlInfo> urlInfos = new ArrayList<>();

        Elements li_div_a = ukPanelBox.select("li div.uk-margin a");
        Elements li_div_p = ukPanelBox.select("li div.uk-margin p");

        for (int i =0; i < li_div_a.size(); i++) {
            UrlInfo urlInfo = new UrlInfo(
                    "OurSite",
                    li_div_a.get(i).attr("href"),
                    li_div_a.get(i).text(),
                    li_div_p.get(i).text()
            );
            urlInfos.add(urlInfo);
        }

        for (int size = urlInfos.size(); size < NUMBER_ELEMENT; size++) {
            urlInfos.add(new UrlInfo("OurSite", "", "", ""));
        }
        return urlInfos;
    }

    private static ArrayList<URL> readResource() {
        BufferedReader fileReader = null;
        ArrayList<URL> resource = new ArrayList<>();

        try {
            fileReader = new BufferedReader(new FileReader("data/resource.txt"));
            fileReader.readLine();
            String line;

            while ((line = fileReader.readLine()) != null) {

                if (!line.startsWith("#")) {
                    resource.add(new URL(line));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in resource file !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
        return resource;
    }
}
