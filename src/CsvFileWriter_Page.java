import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvFileWriter_Page {

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 5;

    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static String PAGE_HEADER = "";

    static {
        String[] headerLine = {"Название раздела", "GUID идентификатор группы элементов"
                , "GUID идентификатор родительской группы элементов", "Заголовок раздела(title)"
                , "Описание раздела(description)", "Ключевые слова раздела(keywords)"
                , "Описание раздела", "Путь для раздела", "Порядок сортировки раздела"
                , "GUID идентификатор элемента", "Название элемента", "Описание элемента", "Текст для элемента"
                , "Метки", "Активность элемента", "Порядок сортировки элемента", "Путь к элементу"
                , "Заголовок (title)", "Значение мета-тега description для страницы с элементом"
                , "Значение мета-тега keywords для страницы с элементом", "Флаг индексации", "Дата"
                , "Дата публикации", "Дата завершения публикации", "Файл изображения для элемента"
                , "Файл малого изображения для элемента", "Ярлыки", "Идентификатор пользователя сайта"};
        String[] headerTextBox = {"Форматированное текстовое поле", "Чек бокс"};
        String youtube = "Адрес (youtube)";
        String[] headerElements = {"Заголовок", "Ссылка", "Описание"};

        for (String str : headerLine) {
            PAGE_HEADER += "\"" + str +"\"" + DELIMITER;
        }

        for (int i = 1; i <= NUMBER_TEXT_BOX; i++) {
            PAGE_HEADER += "\"" + headerTextBox[0] + i + "-1"+ "\"" + DELIMITER +
                            "\"" + headerTextBox[1] + i + "-1" + "\"" + DELIMITER;
        }

        PAGE_HEADER += "\"" + youtube +"\"" + DELIMITER;

        for (int i = 1; i <= NUMBER_ELEMENT; i++) {
            PAGE_HEADER += "\"" + headerElements[0] + i + "-1\"" + DELIMITER +
                            "\"" + headerElements[1] + i + "-1\"" + DELIMITER +
                            "\"" + headerElements[2] + i + "-1\"" + DELIMITER;
        }
    }

    public static void write(String fileName, List<Page> pages) {
        FileWriter fileWriter = null;


        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(PAGE_HEADER);
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Page page : pages) {
                fileWriter.append(addQuotes(page.getNameParagraph()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getGuidOfGroup()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getGuidOfParentGroup()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getSectionTitle()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getSectionDescription()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getSectionKeywords()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getBriefDescriptionSection()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getPathSection()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getPartitionSortingSection()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getGuidOfElement()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getNameOfElement()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getBriefDescriptionElement()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getTextOfElement()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getTags()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getElementActiviti()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getSortingOrderOfElement()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getPathlElement()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getElementTitle()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getElementDescription()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getElementKeywords()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.isIndexing() ? "1": "0"));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getData()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getDataOfPublication()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getDataOfPublicationEnd()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getPathImage()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getPathImageSmall()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getShortcuts()));
                fileWriter.append(DELIMITER);
                fileWriter.append(addQuotes(page.getSiteUserID()));
                fileWriter.append(DELIMITER);
                fileWriter.append(textBoxesToCsv(page.getTextBoxes()));
                fileWriter.append(addQuotes(page.getIdYouTube()));
                fileWriter.append(DELIMITER);
                fileWriter.append(urlInfoListToCsv(page.getUrlInfoList()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter_Page !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }
        }

    }

    private static String addQuotes(String line) {
        return (line == null | Objects.equals(line, "")) ? "" : String.format("\"%s\"", line);
    }

    static String crateIframe(int width, int height, String src, int frameborder) {
       String str;

       if (src.equals("")) {
           str = "";
       } else {
           str = String.format(
                   "<iframe width=\"\"%d\"\" height=\"\"%d\"\" src=\"\"%s\"\" frameborder=\"\"%d\"\" allowfullscreen></iframe>",
                   width, height, src, frameborder
           );
       }

       return str;
    }

    static String urlInfoListToCsv(List<UrlInfo> urlInfoList) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (UrlInfo urlInfo: urlInfoList) {
            if (urlInfo.isYoutube()) {
                continue;
            }
            stringBuilder.append(
                    String.format("\"%s\"%s\"%s\"%s\"%s\"",
                            urlInfo.getHeading(), DELIMITER,
                            urlInfo.getLink(), DELIMITER,
                            urlInfo.getDescription()
                    ))
                    .append(DELIMITER);
            if (counter == NUMBER_ELEMENT) {
                break;
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    static String textBoxesToCsv(ArrayList<OnceText> textBoxes) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (OnceText onceText: textBoxes) {
            stringBuilder.append(
                    String.format("\"%s\"%s\"%s\"",
                            onceText.getTextBox(), DELIMITER, onceText.isCheckBox() ? '1' : '0'
                    ))
                    .append(DELIMITER);
            if (counter == NUMBER_TEXT_BOX) {
                break;
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    private static String arrayToCsvFormat(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        if (array != null && array.length > 0) {
            for (String str : array) {
                stringBuilder.append(str)
                        .append(" ");
            }
        } else {
            stringBuilder.append("");
        }
        return stringBuilder.toString();
    }
}
