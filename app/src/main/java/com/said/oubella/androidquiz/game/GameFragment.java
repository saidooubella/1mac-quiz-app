package com.said.oubella.androidquiz.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.said.oubella.androidquiz.R;
import com.said.oubella.androidquiz.common.Helper;
import com.said.oubella.androidquiz.databinding.FragmentGameBinding;
import com.said.oubella.androidquiz.game.data.DataSource;
import com.said.oubella.androidquiz.game.enums.AnswerResult;
import com.said.oubella.androidquiz.game.enums.QuestionType;
import com.said.oubella.androidquiz.game.models.FreeAnswerQuestion;
import com.said.oubella.androidquiz.game.models.MultiAnswerQuestion;
import com.said.oubella.androidquiz.game.models.Question;
import com.said.oubella.androidquiz.game.models.SingleAnswerQuestion;
import com.said.oubella.androidquiz.game.utils.QuizController;
import com.said.oubella.androidquiz.game.viewModels.GameViewModel;
import com.said.oubella.androidquiz.result.ResultFragment;

import java.util.ArrayList;
import java.util.List;

import static com.said.oubella.androidquiz.common.Helper.ANIMATION_DURATION;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    private GameViewModel viewModel; // i've used it to only let Quiz controller survive the config-Changes

    private RadioButton[] radioButtons;
    private CheckBox[] checkButtons;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new GameViewModel.Factory(DataSource.getQuestions())).get(GameViewModel.class);

        radioButtons = new RadioButton[]{
                binding.radioButton1,
                binding.radioButton2,
                binding.radioButton3,
                binding.radioButton4,
        };

        checkButtons = new CheckBox[]{
                binding.checkBox1,
                binding.checkBox2,
                binding.checkBox3,
                binding.checkBox4,
        };

        updateUserInterface();

        binding.submitButton.setOnClickListener((button) -> {
            if (controller().question().getType() == QuestionType.FREE_ANSWER) {
                @SuppressWarnings("ConstantConditions") final String answer = binding.answerFiled.getText().toString().trim();
                if (answer.isEmpty()) {
                    toast(getString(R.string.no_entered_answer));
                } else {
                    final FreeAnswerQuestion question = controller().question();
                    handleAnswerResult(question.checkAnswer(answer));
                }
            } else if (controller().question().getType() == QuestionType.SINGLE_ANSWER) {
                final int checkedId = binding.radioButtonsGroup.getCheckedRadioButtonId();
                if (checkedId == -1) {
                    toast(getString(R.string.no_checked_answer));
                } else {
                    final SingleAnswerQuestion question = controller().question();
                    handleAnswerResult(question.checkAnswer(getRadioButtonIndex(checkedId)));
                }
            } else if (controller().question().getType() == QuestionType.MULTI_ANSWER) {
                final List<Integer> indexes = getCheckBoxesIndexes();
                if (indexes.isEmpty()) {
                    toast(getString(R.string.no_checked_answers));
                } else {
                    final MultiAnswerQuestion question = controller().question();
                    handleAnswerResult(question.checkAnswer(indexes));
                }
            }
        });
    }

    private List<Integer> getCheckBoxesIndexes() {
        final List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < checkButtons.length; i++) {
            if (checkButtons[i].isChecked()) indexes.add(i);
        }
        return indexes;
    }

    private int getRadioButtonIndex(int checkedId) {
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].getId() == checkedId) return i;
        }
        throw new IllegalStateException("Should and never will happen");
    }

    private void handleAnswerResult(AnswerResult result) {

        toast(result.getMessage());

        if (result == AnswerResult.ALMOST_RIGHT) return;

        if (result == AnswerResult.RIGHT) {
            controller().increaseScore();
        }

        if (!controller().nextQuestion()) {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_gameFragment_to_resultFragment,
                            ResultFragment.requiredArgs(controller().questionsCount(), controller().score())
                    );
        } else updateUserInterface();
    }

    private void toast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateUserInterface() {

        final Question question = controller().question();
        Helper.setActionBarTitle(this, getString(R.string.questionDisplay,
                controller().questionNumber(), controller().questionsCount()));

        animateChanges(question.getType());
        clearThePreviousViewsState();

        if (question.getType() == QuestionType.MULTI_ANSWER) {
            final MultiAnswerQuestion q = controller().question();
            for (int i = 0; i < q.getChoices().length; i++) {
                checkButtons[i].setText(q.getChoices()[i]);
            }
        } else if (question.getType() == QuestionType.SINGLE_ANSWER) {
            final SingleAnswerQuestion q = controller().question();
            for (int i = 0; i < q.getChoices().length; i++) {
                radioButtons[i].setText(q.getChoices()[i]);
            }
        }
    }

    private void clearThePreviousViewsState() {
        binding.radioButtonsGroup.clearCheck();
        binding.answerFiled.setText(null);
        for (CheckBox checkbox : checkButtons) {
            checkbox.setChecked(false);
        }
    }

    /**
     * Function written by me to animate changes happen to the UI
     */
    private void animateChanges(QuestionType type) {

        // Getting the current visible group; null means it's the first run of the fragment
        final View source = isVisible(binding.checkboxesGroup) ? binding.checkboxesGroup
                : isVisible(binding.radioButtonsGroup) ? binding.radioButtonsGroup
                : isVisible(binding.answerFiledGroup) ? binding.answerFiledGroup : null;

        // Getting the target group based on question type
        final View target = type == QuestionType.FREE_ANSWER ? binding.answerFiledGroup
                : type == QuestionType.MULTI_ANSWER ? binding.checkboxesGroup : binding.radioButtonsGroup;

        if (source == null) {
            // source null therefore there is no reason to animate
            binding.questionText.setText(controller().question().getQuestion());
            target.setVisibility(View.VISIBLE);
        } else if (source != target /* if they are the same therefore we don't need to change it */) {
            // Fade-out animate the question textView
            binding.questionText.animate().alpha(0).setDuration(ANIMATION_DURATION);
            // Fade-out animate the source group
            source.animate().alpha(0).setDuration(ANIMATION_DURATION).withEndAction(() -> {
                // Wait 'till the animation ends
                final int sourceHeight = source.getMeasuredHeight(); // get source height
                setViewHeight(binding.answerContainer, sourceHeight); // set the parent height to source height for animating reasons

                source.setVisibility(View.GONE); // Hide the source view. The height of it is sets to the parent so it will behave like View.INVISIBLE
                target.setAlpha(0); // Set view alpha to 0 to stay invisible after setting it's visibility to VISIBLE
                target.setVisibility(View.VISIBLE);

                target.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // measure the view height & width
                final int targetHeight = target.getMeasuredHeight(); // get the measured height

                // Init- a ValueAnimator to animate the height of the parent group
                final ValueAnimator animator = ValueAnimator.ofInt(sourceHeight, targetHeight);
                animator.setDuration(ANIMATION_DURATION);

                // set parent height for each time the value updated
                animator.addUpdateListener((animation) -> setViewHeight(binding.answerContainer, (Integer) animation.getAnimatedValue()));
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator.removeAllUpdateListeners(); // remove update Listeners
                        animator.removeAllListeners();       // remove other Listeners
                        setViewHeight(binding.answerContainer, ViewGroup.LayoutParams.WRAP_CONTENT); // set the height to WRAP_CONTENT
                        target.animate().alpha(1).setDuration(ANIMATION_DURATION); // Fade-in animate the target group
                    }
                });

                binding.questionText.setText(controller().question().getQuestion());
                binding.questionText.animate().alpha(1).setDuration(ANIMATION_DURATION); // Fade-in animate the question text view

                animator.start(); // start the animation
            });
        } else {
            // In this case all what we need is to change the question alone
            // Fade-out animate the question textView
            binding.questionText.animate().alpha(0).setDuration(ANIMATION_DURATION).withEndAction(()->{
                binding.questionText.setText(controller().question().getQuestion());
                binding.questionText.animate().alpha(1).setDuration(ANIMATION_DURATION); // Fade-in animate the question text view
            });
        }
    }

    private void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    private boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private QuizController controller() {
        return viewModel.getController();
    }
}
