package com.said.oubella.androidquiz.game.models;

import com.said.oubella.androidquiz.game.enums.QuestionType;

import java.util.Objects;

public abstract class Question {

    private final QuestionType type;
    private final String question;

    Question(QuestionType type, String question) {
        this.type = Objects.requireNonNull(type);
        this.question = Objects.requireNonNull(question);
    }

    public QuestionType getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }
}
