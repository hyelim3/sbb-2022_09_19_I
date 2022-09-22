package com.mysite.sbb;

import com.mysite.sbb.question.dao.QuestionRepository;
import com.mysite.sbb.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private QuestionRepository questionRepository;

    int increaseNum = -1;

    @RequestMapping("/sbb")
    @ResponseBody
    public String index() {
        System.out.println("sbb");
        return "sbb";
    }

    @GetMapping("/createQuestion")
    @ResponseBody
    public List<Question> createQuestion() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장

        return questionRepository.findAll();
    }

    @GetMapping("/page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                    <input type='number' name="age" placeholder="나이"/>
                    <input type="submit" value="page2로 POST방식으로 이동" />
                </form>
                """;
    }

    @PostMapping("/page2")
    @ResponseBody
    public String showPage2Post(@RequestParam(value = "age", defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>POST방식으로 옴</h1>
                """.formatted(age);
    }

    @GetMapping("/page2")
    @ResponseBody
    public String showPage2Get(@RequestParam(value = "age", defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>GET방식으로 옴</h1>
                """.formatted(age);
    }

    @GetMapping("/plus")
    @ResponseBody
    public String plus(@RequestParam(required = false) Integer a, @RequestParam(required = false) Integer b) {
        if( a == null ) {
            return "a를 입력해주세요.";
        }
        if( b == null ) {
            return "b를 입력해주세요.";
        }
        return String.valueOf(a + b);
    }

    @GetMapping("/minus")
    @ResponseBody
    public String minus(@RequestParam(required = false) Integer a, @RequestParam(required = false) Integer b) {
        if( a == null ) {
            return "a를 입력해주세요.";
        }
        if( b == null ) {
            return "b를 입력해주세요.";
        }
        return String.valueOf(a - b);
    }

    @GetMapping("/increase")
    @ResponseBody
    public int increase() {

        return increaseNum++;
    }

    @GetMapping("/servletPlus")
    @ResponseBody
    public void showPlus(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));

        res.getWriter().append(a + b + "");
    }

    @GetMapping("gugudan")
    @ResponseBody
    public String gugudan(int dan, int limit) {
        // 이런 방식으로도 구현할 수 있다
//        final Integer finalDan = dan;
//        return IntStream.rangeClosed(1, limit)
//                .mapToObj(i -> "%d * %d = %d".formatted(finalDan, i, finalDan * i))
//                .collect(Collectors.joining("<br>\n"));
        String result = "";
        for(int i = 1; i <= limit; i++) {
            result += dan + " * " + i + " = " + (dan *  i) + "<br/>\n";
        }
        return result;

    }

    @GetMapping("/saveSessionAge")
    @ResponseBody
    public String saveSession(@RequestParam(value = "age", defaultValue = "0") int age, HttpSession session) {
        System.out.println("age: " + age);
        session.setAttribute("age", age);
        return "나이 %d이 세션에 저장되었습니다.".formatted(age);
    }

    @GetMapping("/getSessionAge")
    @ResponseBody
    public String saveSession(HttpSession session, HttpServletResponse res) {
        int age = (int) session.getAttribute("age");
        Cookie cookie = new Cookie("age", String.valueOf(age));
        res.addCookie(cookie);

        return "세션에 저장된 나이는 %d입니다.".formatted(age);
    }

    @GetMapping("/addPerson/{id}")
    @ResponseBody
    public Person addPerson(Person person) {
        return person;
    }

}

@Getter
@AllArgsConstructor
class Person {
    private int id;

    private int age;

    private String name;
}
