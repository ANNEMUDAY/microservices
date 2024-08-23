package com.example.question_service.service;

import com.example.question_service.dao.QuestionRepo;
import com.example.question_service.model.Question;
import com.example.question_service.model.QuestionWrapper;
import com.example.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionRepo questionRepo;
    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionRepo.findAll(),HttpStatus.OK);
//        try{
//            new ResponseEntity<>(questionRepo.findAll(),HttpStatus.OK);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<String> addQuestion(Question question) {
        questionRepo.save(question);
        return new ResponseEntity<>("successfully added", HttpStatus.OK);
    }

    public String deleteQuestion(int id) {
        questionRepo.deleteById(id);
        return "Deleted Successfully";
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepo.findByCategory(category),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numQuestions) {
        List<Integer> questionIds=questionRepo.findRandomQuestionsByCategory(category,numQuestions);
        return new ResponseEntity<>(questionIds,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromIds(List<Integer> questionIds) {
        List<Question> questions=new ArrayList<>();
        List<QuestionWrapper> wrappers=new ArrayList<>();
        for(Integer questionId:questionIds){
            questions.add(questionRepo.findById(questionId).get());
        }
        for(Question question:questions){
            QuestionWrapper wrapper=new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }
        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> CalculateScore(List<Response> responses) {
        int score=0;
        for(Response response:responses){
            Question question=questionRepo.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer())){
                score++;
            }
        }
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
