package app;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();

        String id = UUID.randomUUID().toString();

        // Creating authors
        DocumentManager.Author author1 = DocumentManager.Author.builder().id("author1").name("John Doe").build();
        DocumentManager.Author author2 = DocumentManager.Author.builder().id("author2").name("Jane Smith").build();

        // Creating documents
        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .id(id)
                .title("Java Programming")
                .content("Learn Java in depth")
                .author(author1)
                .created(Instant.parse("2023-01-01T10:00:00Z"))
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Python Basics")
                .content("Introduction to Python")
                .author(author2)
                .created(Instant.parse("2023-06-01T12:00:00Z"))
                .build();

        DocumentManager.Document doc3 = DocumentManager.Document.builder()
                .title("Advanced Java")
                .content("Mastering Java")
                .author(author1)
                .created(Instant.parse("2023-03-15T14:00:00Z"))
                .build();

        // Saving documents
        documentManager.save(doc1);
        System.out.println("By saving" + documentManager.save(doc2));
        documentManager.save(doc3);

        // Creating a search request
        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Java", "Python"))
                .containsContents(Arrays.asList("Java", "Python"))
                .createdFrom(Instant.parse("2023-01-01T00:00:00Z"))
                .createdTo(Instant.parse("2023-12-31T23:59:59Z"))
                .build();

        // Performing search
        DocumentManager.Document search = documentManager.findById(id).get();
        System.out.println("By id: " + search.getTitle());
        List<DocumentManager.Document> searchResults = documentManager.search(searchRequest);

        // Printing search results
        System.out.println("Search Results:");
        searchResults.forEach(doc -> {
            System.out.println("Title: " + doc.getTitle());
            System.out.println("Content: " + doc.getContent());
            System.out.println("Author: " + doc.getAuthor().getName());
            System.out.println("Created: " + doc.getCreated());
            System.out.println();
        });
    }
}
