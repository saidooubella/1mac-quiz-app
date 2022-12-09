package com.said.oubella.androidquiz.game.enums;

public enum AnswerResult {

    ALMOST_RIGHT("Good! But there is more :D !"),
    RIGHT("Good answer!"),
    WRONG("Wrong answer!");

    private final String message;

    AnswerResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
