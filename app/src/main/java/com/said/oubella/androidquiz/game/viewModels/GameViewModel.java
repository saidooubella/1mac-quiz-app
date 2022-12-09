package com.said.oubella.androidquiz.game.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.said.oubella.androidquiz.game.models.Question;
import com.said.oubella.androidquiz.game.utils.QuizController;

import java.util.List;

public class GameViewModel extends ViewModel {

    private final QuizController controller;

    private GameViewModel(List<Question> questions) {
        controller = new QuizController(questions);
    }

    public QuizController getController() {
        return controller;
    }

    public static final class Factory implements ViewModelProvider.Factory {

        private final List<Question> questions;

        public Factory(List<Question> questions) {
            this.questions = questions;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new GameViewModel(questions);
        }
    }
}
