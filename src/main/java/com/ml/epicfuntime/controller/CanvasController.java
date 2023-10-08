package com.ml.epicfuntime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CanvasController {
    @GetMapping("/canvas")
    public String canvas() {
        return "canvas";
    }
}
