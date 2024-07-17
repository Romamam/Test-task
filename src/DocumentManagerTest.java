import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentManagerTest {

    private DocumentManager documentManager;

    @BeforeEach
    void setUp() {
        documentManager = new DocumentManager();

        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("Document 1")
                .content("Content of document 1.")
                .author(DocumentManager.Author.builder().name("Roman").build())
                .created(Instant.parse("2024-07-17T10:15:30Z"))
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Document 2")
                .content("Content of document 2.")
                .author(DocumentManager.Author.builder().name("Roman").build())
                .created(Instant.parse("2024-07-16T15:30:45Z"))
                .build();

        documentManager.save(doc1);
        documentManager.save(doc2);
    }

    @Test
    void testSaveDocument() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Document")
                .content("test document.")
                .author(DocumentManager.Author.builder().name("Roman").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertEquals(document.getTitle(), savedDocument.getTitle());
        assertEquals(document.getContent(), savedDocument.getContent());
        assertEquals(document.getAuthor(), savedDocument.getAuthor());
        assertEquals(document.getCreated(), savedDocument.getCreated());
        assertTrue(savedDocument.getId() != null && !savedDocument.getId().isEmpty());
    }

    @Test
    void testSearchDocuments() {

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Doc"))
                .build();

        List<DocumentManager.Document> searchResult = documentManager.search(searchRequest);

        assertEquals(2, searchResult.size());
    }

    @Test
    void testFindDocumentById() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Document")
                .content("test document.")
                .author(DocumentManager.Author.builder().name("Roman").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        Optional<DocumentManager.Document> foundDocument = documentManager.findById(savedDocument.getId());

        assertTrue(foundDocument.isPresent());
        assertEquals(savedDocument.getId(), foundDocument.get().getId());
        assertEquals(savedDocument.getTitle(), foundDocument.get().getTitle());
        assertEquals(savedDocument.getContent(), foundDocument.get().getContent());
        assertEquals(savedDocument.getAuthor(), foundDocument.get().getAuthor());
        assertEquals(savedDocument.getCreated(), foundDocument.get().getCreated());
    }

    @Test
    void testFindDocumentByIdNotFound() {
        String nonExistentId = "non-existent-id";

        Optional<DocumentManager.Document> foundDocument = documentManager.findById(nonExistentId);

        assertTrue(foundDocument.isEmpty());
    }

    @Test
    void testSearchDocumentsWithNullFields() {

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder().build();

        List<DocumentManager.Document> searchResult = documentManager.search(searchRequest);

        assertEquals(2, searchResult.size());
    }

    @Test
    void testSearchDocumentsWithContentContains() {
        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .containsContents(List.of("document"))
                .build();

        List<DocumentManager.Document> searchResult = documentManager.search(searchRequest);

        assertEquals(2, searchResult.size());
    }

    @Test
    void testSearchDocumentsWithCreatedFrom() {
        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .createdFrom(Instant.parse("2024-07-16T00:00:00Z"))
                .build();

        List<DocumentManager.Document> searchResult = documentManager.search(searchRequest);

        assertEquals(2, searchResult.size());
    }

}
