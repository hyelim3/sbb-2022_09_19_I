package com.mysite.sbb.question.controller;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.question.QuestionForm;
import com.mysite.sbb.question.domain.Question;
import com.mysite.sbb.question.service.QuestionService;
import com.mysite.sbb.siteuser.domain.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {


        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
//        List<Question> questionList = questionService.getList();
//        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Integer id, AnswerForm answerForm) {
        System.out.println("id: " + id);
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, @AuthenticationPrincipal SiteUser siteUser){
        if(bindingResult.hasErrors()) {
            return "question_form";
        }

        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        return "redirect:/question/list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id,  @AuthenticationPrincipal SiteUser siteUser) {
        //1 만약 현재 로그인한 사용자와 질문의 작성자가 동일하지 않을 경우에 오류가 발생
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(siteUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        //수정할 질문의 제목과 내용을 화면에 보여주기 위해 questionForm 객체에 값을 담아서 템플릿으로 전달
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }


    //3, 제목이나 내용을 변경하고 저장하기버튼을 누르면 호출되는 POST 요청
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 @AuthenticationPrincipal SiteUser siteUser, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(siteUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }


}
