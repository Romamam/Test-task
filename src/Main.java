import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("Document 1")
                .content("Content of document 1.")
                .author(DocumentManager.Author.builder().name("Roman").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Document 2")
                .content("Content of document 2.")
                .author(DocumentManager.Author.builder().name("Taras").build())
                .created(Instant.now())
                .build();

        documentManager.save(doc1);
        documentManager.save(doc2);

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Document"))
                .createdFrom(Instant.parse("2024-07-16T00:00:00Z"))
                .createdTo(Instant.parse("2024-07-18T00:00:00Z"))
                .build();

        List<DocumentManager.Document> searchResult = documentManager.search(searchRequest);
        System.out.println("Search result:");
        searchResult.forEach(doc -> System.out.println(doc.getTitle()));

        String idToFind = doc1.getId();
        Optional<DocumentManager.Document> foundDocument = documentManager.findById(idToFind);
        foundDocument.ifPresent(doc -> System.out.println("Found document by id: " + doc.getTitle()));
    }
}
