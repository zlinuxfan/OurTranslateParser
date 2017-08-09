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

//            Page page = createPage(url, guidOfElement.next());
//            System.out.println("name: " + page.getNameOfElement());
//            System.out.println("text: " + page.getBriefDescriptionElement());
//            System.out.println("title: " + page.getElementTitle());
//            System.out.println("descr: " + page.getElementDescription());
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

            if (counter > 9) {
                break;
            }

            counter++;
        }
    }

    private static Page createPage(URL url) throws IOException {
        Document document = getDocument(url.toString());
        Elements nameOfElement = document.select("h1.entry-title");
        Elements textOfElement = document.select("div.td-main-content");
        Elements title = document.select("head title");
        Elements description = (document.select("meta[name=description]"));

        ArrayList<OnceText> onceTexts = new ArrayList<>();

        for (int size = onceTexts.size(); size < NUMBER_TEXT_BOX; size++) {
            onceTexts.add(new OnceText("", false));
        }

        ArrayList<UrlInfo> urlInfos = new ArrayList<>();
        Google google = new Google();
        try {
            urlInfos = google.find(nameOfElement.html());
        } catch (Exception e) {
            log.error("    error: \"" + nameOfElement.html() + "\" is not processed. Check internet or capcha.");
        }

        for (int size = urlInfos.size(); size < NUMBER_ELEMENT; size++) {
            urlInfos.add(new UrlInfo("", "", "", ""));
        }

        return new Page.Builder(
                "",
                nameOfElement.html(),
                textOfElement.eachText().get(0),
                "",
                ((toTransliteration(nameOfElement.html())).replace(" ", "-")),
                onceTexts,
                urlInfos
        ).elementDescription(description.attr("content"))
                .elementTitle(title.text())
                .guidOfGroup("")
                .build();
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
