package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortUrlDto;
import com.example.urlshortener.dto.converter.ShortUrlDtoConverter;
import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.request.ShortUrlRequest;
import com.example.urlshortener.request.converter.ShortUrlRequestConverter;
import com.example.urlshortener.service.ShortUrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping
public class ShortUrlController {
    private final ShortUrlDtoConverter shortUrlDtoConverter;
    private final ShortUrlRequestConverter shortUrlRequestConverter;
    private final ShortUrlService service;

    public ShortUrlController(ShortUrlDtoConverter shortUrlDtoConverter, ShortUrlRequestConverter shortUrlRequestConverter, ShortUrlService service){
        this.shortUrlDtoConverter = shortUrlDtoConverter;
        this.shortUrlRequestConverter = shortUrlRequestConverter;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ShortUrlDto>> getAllUrls(){
        return new ResponseEntity<List<ShortUrlDto>>(
                shortUrlDtoConverter.convertToDto(service.getAllShortUrls()), HttpStatus.OK
        );
    }

    @GetMapping("/show/{code}")
    public ResponseEntity<ShortUrlDto> getUrlByCode(@Valid @NotEmpty @PathVariable String code){
        return ResponseEntity.ok(shortUrlDtoConverter.convertToDto(service.getUrlByCode(code)));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ShortUrlDto> redirect(@Valid @NotEmpty @PathVariable String code) throws URISyntaxException {

        ShortUrl shortUrl = service.getUrlByCode(code);

        URI uri = new URI(shortUrl.getUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return new ResponseEntity<>(
                httpHeaders, HttpStatus.SEE_OTHER
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ShortUrlRequest shortUrlRequest){
        ShortUrl shortUrl = shortUrlRequestConverter.convertToEntity(shortUrlRequest);
        return new ResponseEntity<ShortUrlDto>(shortUrlDtoConverter.convertToDto(service.create(shortUrl)), HttpStatus.CREATED);
    }


}
