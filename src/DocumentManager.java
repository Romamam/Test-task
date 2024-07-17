import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {


    private final List<Document> documents = new ArrayList<>();

    /**
     * Upsert the document to the storage and generates a unique id if it doesn't exist.
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
        }
        documents.add(document);
        return document;
    }

    /**
     * Finds documents which match the search request.
     *
     * @param request - search request, each field could be null
     * @return list of matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documents.stream()
                .filter(doc -> matchesSearchCriteria(doc, request))
                .collect(Collectors.toList());
    }

    /**
     * Finds a document by its id.
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return documents.stream()
                .filter(doc -> doc.getId().equals(id))
                .findFirst();
    }

    private boolean matchesSearchCriteria(Document document, SearchRequest request) {
        if (request.getTitlePrefixes() != null && !request.getTitlePrefixes().isEmpty()) {
            if (request.getTitlePrefixes().stream()
                    .noneMatch(prefix -> document.getTitle().startsWith(prefix))) {
                return false;
            }
        }

        if (request.getContainsContents() != null && !request.getContainsContents().isEmpty()) {
            if (request.getContainsContents().stream()
                    .noneMatch(content -> document.getContent().contains(content))) {
                return false;
            }
        }

        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            if (!request.getAuthorIds().contains(document.getAuthor().getId())) {
                return false;
            }
        }

        if (request.getCreatedFrom() != null && document.getCreated().isBefore(request.getCreatedFrom())) {
            return false;
        }

        if (request.getCreatedTo() != null && document.getCreated().isAfter(request.getCreatedTo())) {
            return false;
        }

        return true;
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        @Builder.Default
        private String id = UUID.randomUUID().toString();
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        @Builder.Default
        private String id = UUID.randomUUID().toString();
        private String name;
    }
}