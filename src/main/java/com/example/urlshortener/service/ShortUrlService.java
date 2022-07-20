package com.example.urlshortener.service;

import com.example.urlshortener.exception.CodeAlreadyExistException;
import com.example.urlshortener.exception.ShortUrlNotFoundException;
import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.utils.RandomCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShortUrlService {
    private final ShortUrlRepository repository;
    private final RandomCodeGenerator stringGenerator;

    public ShortUrlService(ShortUrlRepository repository, RandomCodeGenerator stringGenerator){
        this.repository = repository;
        this.stringGenerator = stringGenerator;
    }

    public List<ShortUrl> getAllShortUrls() {
        return repository.findAll();
    }

    public ShortUrl getUrlByCode(String code) {
        return repository.findAllByCode(code).orElseThrow(() -> new ShortUrlNotFoundException("Url not found!"));
    }

    public ShortUrl create(ShortUrl shortUrl) {
        if (shortUrl.getCode() == null || shortUrl.getCode().isEmpty()) {
            shortUrl.setCode(generateCode());
        } else if (repository.findAllByCode(shortUrl.getCode()).isPresent()) {
            throw new CodeAlreadyExistException("Code is already exists!");
        }
        shortUrl.setCode(shortUrl.getCode().toUpperCase());
        return repository.save(shortUrl);
    }

    private String generateCode() {
        String code;
        do {
            code = stringGenerator.generateRandomString();
        } while (repository.findAllByCode(code).isPresent());

        return code;
    }
}
