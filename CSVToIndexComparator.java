import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CSVToIndexComparator {

    private final HashSet<String> indexIds = new HashSet<>();
    private final HashSet<String> uniqueCsvIds = new HashSet<>();
    private final ArrayList<String> duplicateIds = new ArrayList<>();
    private int duplicateCount = 0;
    private final HashMap<Path, ArrayList<String>> csvIdWithFileName = new HashMap<>();
    private static final String index_path =
            "C:\\workspace\\projects\\poc\\chatbot\\compare-li\\csv_to_index\\index";
    private static final String csv_path =
            "C:\\workspace\\projects\\poc\\chatbot\\compare-li\\csv_to_index\\csv";
    private int totalCSVIdsCount = 0;
    private int mismatchCount = 0;

    public static void compareCSVToIndex() {
        CSVToIndexComparator indexComparator = new CSVToIndexComparator();
        indexComparator.loadIndex();
        indexComparator.loadAllCsv();
        indexComparator.checkCsvIdIsExistsOnLuceneIndex();
        indexComparator.printData();
    }

    private void checkCsvIdIsExistsOnLuceneIndex() {
        csvIdWithFileName.forEach((path, strings) -> {
            mismatchCount = 0;
            strings.forEach(documentId -> {
                if (!indexIds.contains(documentId)) {
                    mismatchCount++;
                }
            });
            System.out.println("CSV filename : " + path.getFileName()
                    + ", CSV Id Count : " + strings.size()
                    + ", Mismatch document count with lucene : " + mismatchCount);
        });
    }

    private void printData() {
        csvIdWithFileName.forEach((path, strings) -> {
            totalCSVIdsCount += strings.size();
        });
        Collections.sort(duplicateIds);
        System.out.println("Duplicate document list : " + duplicateIds.toString());
        System.out.println("Total processed csv files : " + csvIdWithFileName.size());
        System.out.println("Lucene document count (including duplicate) :" + (indexIds.size() + duplicateCount));
        System.out.println("Lucene duplicate document count : " + duplicateCount);
        System.out.println("Lucene unique document count :" + indexIds.size());
        System.out.println("CSV id's count (including duplicate) : " + totalCSVIdsCount);
        System.out.println("CSV Unique csv id's count: " + uniqueCsvIds.size());
        System.out.println("CSV duplicate document count : " + (totalCSVIdsCount - uniqueCsvIds.size()));
    }

    private void loadAllCsv() {
        try {
            Stream<Path> paths;
            paths = Files.walk(Paths.get(csv_path));
            paths.filter(Files::isRegularFile).forEach(filePath -> {
                //System.out.println("Reading file -> " + filePath);
                loadCsvIdIntoArrayList(filePath);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCsvIdIntoArrayList(final Path filePath) {
        try {
            ArrayList<String> csvIndexId = new ArrayList<>();
            Reader reader = new FileReader(filePath.toFile());
            CSVFormat format = CSVFormat.RFC4180.withHeader().withQuote(null).withDelimiter(',');
            CSVParser csvParser = new CSVParser(reader, format);
            for (CSVRecord csvRecord : csvParser) {
                csvIndexId.add(csvRecord.get(0));
            }
            uniqueCsvIds.addAll(csvIndexId);
            csvIdWithFileName.put(filePath, csvIndexId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadIndex() {
        try {
            File path = new File(index_path);
            Directory index = FSDirectory.open(path.toPath());
            IndexReader reader = DirectoryReader.open(index);
            for (int i = 0; i < reader.maxDoc(); i++) {
                Document doc = reader.document(i);
                if (!indexIds.contains(doc.get("uri_s")))
                    indexIds.add(doc.get("uri_s"));
                else {
                    duplicateCount++;
                    duplicateIds.add(doc.get("uri_s"));
                }
            }
            System.out.println("Added index into lucene data class ");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
