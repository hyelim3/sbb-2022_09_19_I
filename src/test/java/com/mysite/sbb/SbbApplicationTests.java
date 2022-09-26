package com.mysite.sbb;

import com.mysite.sbb.question.dao.QuestionRepository;
import com.mysite.sbb.question.domain.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	void contextLoads() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);
	}

	@Test
	void getQuestions() {
		List<Question> all = questionRepository.findAll();
		assertEquals(8, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요~~~?", q.getSubject());

	}

	@Test
	void getQuestionById() {
		Optional<Question> oq = questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요~~~?", q.getSubject());
		}
	}

	@Test
	void getQuestionBySubject() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
	}

	@Test
	void getQuestionsBySubject() {
		List<Question> questions = this.questionRepository.findAllBySubject("sbb가 무엇인가요?");
		assertEquals(3, questions.size());
	}

	@Test
	void getQuestionsByTwoSubjects() {
		List<String> searchWordList = new ArrayList<>();
		searchWordList.add("sbb가 무엇인가요?");
		searchWordList.add("스프링부트 모델 질문입니다.");

		List<Question> questions = this.questionRepository.findAllBySubjectIn(searchWordList);
		assertEquals(4, questions.size());
	}

	@Test
	void getQuestionsBySubjectAndContent() {
		List<Question> questions = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해 알고 싶습니다.");
		assertEquals(2, questions.size());
	}

	@Test
	void getQuestionsHaveStrings() {
		List<Question> questions = questionRepository.findBySubjectLike("sbb%");
		assertEquals(3, questions.size());
	}

	@Test
	void updateQuestion() {
		Optional<Question> oq = questionRepository.findById(2);
		if(oq.isPresent()) {
			Question question = oq.get();
			question.setSubject("수정된 질문");
			question.setContent("수정된 내용");
			questionRepository.save(question);
		}
	}

	@Test
	void deleteQuestion() {
		assertEquals(5, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		if(oq.isPresent()) {
			Question question = oq.get();
			questionRepository.delete(question);
		}
		assertEquals(4, questionRepository.count());
	}
}
