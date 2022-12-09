package com.said.oubella.androidquiz.result;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionManager;

import com.said.oubella.androidquiz.R;
import com.said.oubella.androidquiz.databinding.FragmentResultBinding;
import com.said.oubella.androidquiz.externals.CircleProgressBar;

import static com.said.oubella.androidquiz.common.Helper.ANIMATION_DURATION;

public class ResultFragment extends Fragment {

    private static final String QUESTIONS_COUNT = "questionsCount";
    private static final String SCORE = "score";

    private FragmentResultBinding binding;
    private ObjectAnimator objectAnimator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int questionsCount = requireArguments().getInt(QUESTIONS_COUNT);
        int score = requireArguments().getInt(SCORE);

        binding.resultProgress.setMax(questionsCount);

        if (savedInstanceState == null) {

            // PLaying the animation in the first time the fragment opened only

            objectAnimator = ObjectAnimator.ofFloat(binding.resultProgress, new Property<CircleProgressBar, Float>(Float.class, "progress") {

                @Override
                public void set(CircleProgressBar object, Float value) {
                    object.setProgress(value);
                }

                @Override
                public Float get(CircleProgressBar object) {
                    return object.getProgress();
                }
            }, score);

            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animation.removeAllListeners();
                    TransitionManager.beginDelayedTransition((ViewGroup) view);
                    setResultText(questionsCount, score);
                    binding.bottomGroup.setVisibility(View.VISIBLE);
                }
            });

            objectAnimator.setDuration(ANIMATION_DURATION);
            objectAnimator.setStartDelay(400);
            objectAnimator.start();

            toast(getPlayerResultMessage(questionsCount, score) + ". You have answered " + score + " of " + questionsCount + " questions right.");

        } else {
            binding.resultProgress.setProgress(score);
            setResultText(questionsCount, score);
            binding.bottomGroup.setVisibility(View.VISIBLE);
        }

        binding.homeButton.setOnClickListener(button -> Navigation.findNavController(view).popBackStack());

        binding.restartButton.setOnClickListener(button -> Navigation.findNavController(view).navigate(R.id.action_resultFragment_to_gameFragment));
    }

    private void toast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void setResultText(int questionsCount, int score) {
        binding.resultText.setText(getPlayerResultMessage(questionsCount, score));
        String scoreText = score + "/" + questionsCount;
        binding.scoreText.setText(scoreText);
    }

    private String getPlayerResultMessage(int questionsCount, int score) {
        return questionsCount / 2 <= score ? getString(R.string.winning_message) : getString(R.string.loosing_message);
    }

    @Override
    public void onDestroyView() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            objectAnimator.cancel();
        }
        super.onDestroyView();
    }

    @NonNull
    public static Bundle requiredArgs(int questionsCount, int score) {
        final Bundle bundle = new Bundle();
        bundle.putInt(QUESTIONS_COUNT, questionsCount);
        bundle.putInt(SCORE, score);
        return bundle;
    }
}
