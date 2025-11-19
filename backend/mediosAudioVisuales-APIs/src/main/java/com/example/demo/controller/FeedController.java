package com.example.demo.controller;

import com.example.demo.dto.PublicacionDTO;
import com.example.demo.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping
    public List<PublicacionDTO> obtenerFeed() {
        return feedService.obtenerFeedUnificado();
    }
}