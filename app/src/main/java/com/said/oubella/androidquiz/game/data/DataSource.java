package com.said.oubella.androidquiz.game.data;

import com.said.oubella.androidquiz.game.models.FreeAnswerQuestion;
import com.said.oubella.androidquiz.game.models.MultiAnswerQuestion;
import com.said.oubella.androidquiz.game.models.Question;
import com.said.oubella.androidquiz.game.models.SingleAnswerQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DataSource {

    /**
     * -> If you wanna add more questions just add them here and run
     * and the rest will take care of the rest.
     * Notes:
     * - If the question class require choices you should pass exactly four or the app will crash
     * at run-time.
     * - If the question class handle multiple answers you should pass 2 or more answers indexes
     * or the app will crash at run-time.
     * - Null values will cause the app to crash at run ime either.
     */
    private static final List<Question> questions = listOf(
            new SingleAnswerQuestion("Who created Android the OS?", new String[]{"Google LLC", "Microsoft", "Samsung", "Android inc."}, 3),
            new SingleAnswerQuestion("Android built originally for ...?", new String[]{"Cameras", "Phones", "Desktops", "MP3 Players"}, 0),
            new SingleAnswerQuestion("How was the first Android prototype phone looks like?", new String[]{"Regular touch screen phone", "Blackberry", "an MP3", "None of the above"}, 1),
            new SingleAnswerQuestion("What does the wtf() function mean is the Log class?", new String[]{"Well that's fantastic", "Way too funny", "Welcome to facebook", "What a terrible failure"}, 3),
            new MultiAnswerQuestion("Which devices of these Android target?", new String[]{"Phones/Tablet", "Cars", "Watches", "TVs"}, setOf(0, 1, 2, 3)),
            new MultiAnswerQuestion("What is Android Jetpack?", new String[]{"Libraries", "Operating system", "Guidance", "Documentation"}, setOf(0, 2, 3)),
            new MultiAnswerQuestion("What are the available programming languages to use in native android dev?", new String[]{"C++", "Java", "Dart", "Kotlin"}, setOf(1, 3)),
            new FreeAnswerQuestion("On which kernel 'Android' built?", "Linux"),
            new FreeAnswerQuestion("When was the first 'Android' stable release released?", "2008"),
            new FreeAnswerQuestion("When was 'Android inc.' founded?", "2003")
    );

    public static List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    @SafeVarargs
    private static <T> Set<T> setOf(T... elements) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements)));
    }

    @SafeVarargs
    private static <T> List<T> listOf(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
}
