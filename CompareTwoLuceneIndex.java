import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CompareTwoLuceneIndex {

    private static int count = 0;
    private static int sameCount = 0;
    private final ArrayList<String> uris = new ArrayList<>();
    private final ArrayList<String> ids = new ArrayList<>();
    private final HashMap<String, LuceneModel> luceneData = new HashMap<>();


    private final static String first_index_Path = "C:\\workspace\\projects\\poc\\chatbot\\compare-li\\test\\luxid\\index";
    private final static String second_index_path = "C:\\workspace\\projects\\poc\\chatbot\\compare-li\\test\\cfa\\index";

    public static void compareLucene() {
        System.out.println("Welcome to lucene comparison application");
        CompareTwoLuceneIndex firstIndex = new CompareTwoLuceneIndex();
        firstIndex.readIdFromDocument(first_index_Path);

        CompareTwoLuceneIndex secondIndex = new CompareTwoLuceneIndex();
        secondIndex.readIdFromDocument(second_index_path);

        compareIdData(firstIndex, secondIndex);

        CompareTwoLuceneIndex firstIndex1 = new CompareTwoLuceneIndex();
        firstIndex1.readUriFromDocument(first_index_Path);

        CompareTwoLuceneIndex secondIndex1 = new CompareTwoLuceneIndex();
        secondIndex1.readUriFromDocument(second_index_path);

        compareUriData(firstIndex1, secondIndex1);

        CompareTwoLuceneIndex firstIndex2 = new CompareTwoLuceneIndex();
        firstIndex2.readDocumentViaFile(first_index_Path);

        CompareTwoLuceneIndex secondIndex2 = new CompareTwoLuceneIndex();
        secondIndex2.readDocumentViaFile(second_index_path);

        compareLuceneData(firstIndex2, secondIndex2);
    }

    private static void compareIdData(CompareTwoLuceneIndex firstIndex, CompareTwoLuceneIndex secondIndex) {
        System.out.println("first id count "
                + firstIndex.ids.size()
                + " - second id count "
                + secondIndex.ids.size());
        count = 0;
        sameCount = 0;
        firstIndex.ids.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if (!secondIndex.ids.contains(s)) {
                    System.out.println(s);
                    count++;
                } else {
                    sameCount++;
                }
            }
        });

        System.out.println("Same count:" + sameCount);
        System.out.println("Different count:" + count);
    }

    private static void compareUriData(CompareTwoLuceneIndex firstIndex, CompareTwoLuceneIndex secondIndex) {
        System.out.println("first uri count "
                + firstIndex.uris.size()
                + " - second uri count "
                + secondIndex.uris.size());
        count = 0;
        sameCount = 0;
        firstIndex.uris.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if (!secondIndex.uris.contains(s)) {
                    System.out.println("This uri is not available:" + s);
                    count++;
                } else {
                    sameCount++;
                }
            }
        });

        System.out.println("Same count:" + sameCount);
        System.out.println("Different count:" + count);
    }

    private static void compareLuceneData(CompareTwoLuceneIndex firstIndex, CompareTwoLuceneIndex secondIndex) {
        System.out.println("first lucene data "
                + firstIndex.luceneData.size()
                + " - second lucene data "
                + secondIndex.luceneData.size());
        if (firstIndex.luceneData.size() != secondIndex.luceneData.size()) {
            System.out.println("Size different - Test case failed");
            return;
        }

        count = 0;
        sameCount = 0;
        System.out.println("First test case passed");

        System.out.println("Second test case started");
        firstIndex.luceneData.entrySet()
                .forEach(stringLuceneModelEntry -> {
                    LuceneModel secondModel = secondIndex.luceneData.get(stringLuceneModelEntry.getKey());

                    if (stringLuceneModelEntry.getValue().equals(secondModel)) {
                        count++;
                    }

                    secondIndex.luceneData.remove(stringLuceneModelEntry.getKey());
                });

        if (secondIndex.luceneData.size() != 0) {
            System.out.println("Map not empty - Test case failed " + secondIndex.luceneData.size());
            return;
        }

        System.out.println("Second test case passed");

        System.out.println("Third test case started");

        if (count != firstIndex.luceneData.size()) {
            System.out.println("Data is different - Test case failed");
            return;
        }

        System.out.println("Third test case passed " + count + " == " + firstIndex.luceneData.size());

    }

    public void readDocumentViaFile(String indexPathLocation) {
        try {
            File path = new File(indexPathLocation);

            Directory index = FSDirectory.open(path.toPath());
            IndexReader reader = DirectoryReader.open(index);
            for (int i = 0; i < reader.maxDoc(); i++) {
                Document doc = reader.document(i);
                LuceneModel luceneModel = new LuceneModel();
                luceneModel.setArticle_pl(doc.get("article_pl"));
                luceneModel.setId(doc.get("id"));
                luceneModel.setUri_s(doc.get("uri_s"));
                luceneModel.setTitle_pl(doc.get("title_pl"));
                luceneModel.setLast_modified_date_ss(doc.get("last_modified_date_ss"));
                luceneData.put(luceneModel.getId(), luceneModel);
            }
            System.out.println("Added index into lucene data class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readUriFromDocument(String indexPathLocation) {
        try {
            File path = new File(indexPathLocation);

            Directory index = FSDirectory.open(path.toPath());
            IndexReader reader = DirectoryReader.open(index);
            for (int i = 0; i < reader.maxDoc(); i++) {
                Document doc = reader.document(i);
                uris.add(doc.get("uri_s"));
            }
            System.out.println("Added index into lucene data class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readIdFromDocument(String indexPathLocation) {
        try {
            File path = new File(indexPathLocation);

            Directory index = FSDirectory.open(path.toPath());
            IndexReader reader = DirectoryReader.open(index);
            for (int i = 0; i < reader.maxDoc(); i++) {
                Document doc = reader.document(i);
                ids.add(doc.get("id"));
            }
            System.out.println("Added index into lucene data class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
