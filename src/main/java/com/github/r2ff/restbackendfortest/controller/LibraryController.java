package com.github.r2ff.restbackendfortest.controller;

import com.github.r2ff.restbackendfortest.domain.BookInfo;
import com.github.r2ff.restbackendfortest.domain.BookResult;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LibraryController {

    private HashMap<String, String> library = new HashMap<>();

    {
        library.put("House", "Tom");
        library.put("Spring", "Alex");
        library.put("Search", "Brin");
    }

    @PostMapping("book/add")
    public BookResult addBookToLibrary(@RequestBody BookInfo bookInfo) {
        String bookNameKey = bookInfo.getBookName();
        if (library.get(bookNameKey) == null) {
            library.put(bookInfo.getBookName(), bookInfo.getBookAuthor());
            return
                    BookResult.builder()
                            .bookName(bookInfo.getBookName() + " " + bookInfo.getBookAuthor())
                            .message("Successful added")
                            .build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exist");
        }
    }

    @GetMapping("books/getall")
    public List<BookInfo> getAllBooks() {
        List<BookInfo> result = new ArrayList<>();
        for (Map.Entry<String, String> e : library.entrySet()) {
            BookInfo bookInfo = new BookInfo(e.getKey(), e.getValue());
            result.add(bookInfo);
        }
        return result;
    }

    @PostMapping("books/byauthor")
    public List<BookInfo> getBookByAuthor(@RequestBody BookInfo bookInfo) {
        List<BookInfo> result = new ArrayList<>();

        if (library.containsValue(bookInfo.getBookAuthor())) {
            for (HashMap.Entry<String, String> e : library.entrySet()) {
                //BookInfo ListOfBookInfo = new BookInfo(e.getKey(), e.getValue());
                if (bookInfo.getBookAuthor().equals(e.getValue())) {
                    result.add(new BookInfo(e.getKey(), e.getValue()));
                }

            }
        }
        return result;
    }

}

