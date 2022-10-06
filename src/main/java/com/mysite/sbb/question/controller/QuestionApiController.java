package com.mysite.sbb.question.controller;

import com.mysite.sbb.question.domain.Question;
import com.mysite.sbb.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuestionApiController {

    private final QuestionService questionService;

    @GetMapping("")
    public List<Question> getQuestionList() {
        return questionService.getList();
    }
}
