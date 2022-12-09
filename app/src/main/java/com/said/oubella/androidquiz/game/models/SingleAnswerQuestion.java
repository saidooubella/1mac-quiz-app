package com.said.oubella.androidquiz.game.models;

import com.said.oubella.androidquiz.game.enums.AnswerResult;
import com.said.oubella.androidquiz.game.enums.QuestionType;
import com.said.oubella.androidquiz.common.Helper;

import java.util.Objects;

public final class SingleAnswerQuestion extends Question {

    private final String[] choices;
    private final int answerIndex;

    public SingleAnswerQuestion(String question, String[] choices, int answerIndex) {
        super(QuestionType.SINGLE_ANSWER, question);
        Helper.require(choices.length == 4, "Choices count must be 4.");
        this.choices = Objects.requireNonNull(choices);
        this.answerIndex = answerIndex;
    }

    public AnswerResult checkAnswer(int integer) {
        return answerIndex == integer ? AnswerResult.RIGHT : AnswerResult.WRONG;
    }

    public String[] getChoices() {
        return choices;
    }
}
