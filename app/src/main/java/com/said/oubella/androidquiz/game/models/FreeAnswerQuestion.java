package com.said.oubella.androidquiz.game.models;

import com.said.oubella.androidquiz.game.enums.AnswerResult;
import com.said.oubella.androidquiz.game.enums.QuestionType;

import java.util.Objects;

public final class FreeAnswerQuestion extends Question {

    private final String answer;

    public FreeAnswerQuestion(String question, String answer) {
        super(QuestionType.FREE_ANSWER, question);
        this.answer = Objects.requireNonNull(answer);
    }

    public AnswerResult checkAnswer(String s) {
        return answer.trim().toLowerCase().equals(s) ? AnswerResult.RIGHT : AnswerResult.WRONG;
    }
}
