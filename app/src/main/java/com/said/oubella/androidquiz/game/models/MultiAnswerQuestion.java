package com.said.oubella.androidquiz.game.models;

import com.said.oubella.androidquiz.game.enums.AnswerResult;
import com.said.oubella.androidquiz.game.enums.QuestionType;
import com.said.oubella.androidquiz.common.Helper;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class MultiAnswerQuestion extends Question {

    private final Set<Integer> answersIndexes; // I've selected this data structure because of O(1) performance in look-up operation
    private final String[] choices;

    public MultiAnswerQuestion(String question, String[] choices, Set<Integer> answersIndexes) {
        super(QuestionType.MULTI_ANSWER, question);
        Helper.require(choices.length == 4, "Choices count must be 4.");
        Helper.require(answersIndexes.size() >= 2, "Answer indexes count must be 2 or more.");
        this.answersIndexes = Objects.requireNonNull(answersIndexes);
        this.choices = Objects.requireNonNull(choices);
    }

    public AnswerResult checkAnswer(List<Integer> integers) {

        int rightAnswersCount = 0;
        int wrongAnswersCount = 0;

        for (Integer answer : integers) {
            if (answersIndexes.contains(answer)) {
                rightAnswersCount++;
            } else wrongAnswersCount++;
        }

        if (rightAnswersCount == answersIndexes.size() &&
                wrongAnswersCount == 0) return AnswerResult.RIGHT;

        return wrongAnswersCount == 0 ? AnswerResult.ALMOST_RIGHT : AnswerResult.WRONG;
    }

    public String[] getChoices() {
        return choices;
    }
}
