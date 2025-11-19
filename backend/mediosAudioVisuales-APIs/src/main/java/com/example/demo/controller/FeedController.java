package com.example.demo.controller;

import com.example.demo.dto.PublicacionDTO;
import com.example.demo.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    // Aceptamos el parámetro opcional ?idUsuario=...
    @GetMapping
    public List<PublicacionDTO> obtenerFeed(@RequestParam(required = false) Long idUsuario) {
        return feedService.obtenerFeedUnificado(idUsuario);
    }
}