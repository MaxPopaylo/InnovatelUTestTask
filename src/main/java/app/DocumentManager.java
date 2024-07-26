package app;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;


public class DocumentManager {

    private final Map<String, Document> documentStorage = new HashMap<>();

    public Document save(Document document) {
        if (document.getId() == null) {
            document.setId(generateId());
        }

        documentStorage.put(document.getId(), document);
        return document;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }


    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documentStorage.get(id));
    }


    public List<Document> search(SearchRequest request) {
        return documentStorage.values()
                .stream()
                .filter(document -> suitsBySearchRequest(document, request))
                .toList();
    }

    private Boolean suitsBySearchRequest(Document document, SearchRequest request) {
        if (request.titlePrefixes != null && !startsWithAny(document.title, request.titlePrefixes)) {
            return false;
        }
        if (request.containsContents != null && !containsAnyContent(document.content, request.containsContents)) {
            return false;
        }
        if (request.authorIds != null && !containsAnyIds(document.author.getId(), request.authorIds)) {
            return false;
        }
        if (request.createdFrom != null && request.createdTo != null &&
                !isWithinInstantRange(document.created, request.createdFrom, request.createdTo)) {
            return false;
        }

        return true;
    }

    private boolean startsWithAny(String value, List<String> prefixes) {
        return prefixes.stream().anyMatch(value::startsWith);
    }

    private boolean containsAnyContent(String value, List<String> valuesList) {
        return valuesList.stream().anyMatch(value::contains);
    }

    private boolean containsAnyIds(String value, List<String> valuesList) {
        return valuesList.contains(value);
    }

    private boolean isWithinInstantRange(Instant value, Instant from, Instant to) {
        return (value.isAfter(from) || value.equals(from)) && (value.isBefore(to) || value.equals(to));
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
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}