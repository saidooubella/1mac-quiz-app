package com.said.oubella.androidquiz.game.utils;

import com.said.oubella.androidquiz.game.models.Question;

import java.util.Collections;
import java.util.List;

public final class QuizController {

    private List<Question> questions;
    private Question currentQuestion;

    private int index;
    private int score;

    public QuizController(List<Question> questions) {
        Collections.shuffle(questions);
        this.questions = questions;
        this.currentQuestion = questions.get(0);
    }

    /**
     * switch to the next question if there is more.
     * @return "true" when there is more questions or "false" if the quiz is ended
     * */
    public boolean nextQuestion() {
        if (index + 1 < questions.size()) {
            currentQuestion = questions.get(++index);
            return true;
        }
        return false;
    }

    public void increaseScore() {
        score++;
    }

    public int score() {
        return score;
    }

    @SuppressWarnings("unchecked")
    public <Q extends Question> Q question() {
        return (Q) currentQuestion;
    }

    public int questionNumber() {
        return index + 1;
    }

    public int questionsCount() {
        return questions.size();
    }
}
