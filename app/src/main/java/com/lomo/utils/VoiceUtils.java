package com.lomo.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lomo on 2017/6/5.
 */

public class VoiceUtils {
    private TextToSpeech mTts;

    public VoiceUtils(Context context) {
        mTts = new TextToSpeech(context.getApplicationContext(), mInitListener);
    }

    private final TextToSpeech.OnInitListener mInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = mTts.setLanguage(Locale.CHINESE);
                mTts.setPitch(1.0f);
                mTts.setSpeechRate(1.0f);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("VoiceUtils", "Language is not available.");
                }
            } else {
                Log.e("VoiceUtils", "Could not initialize TextToSpeech.");
            }
        }
    };

    public void callNumberVoice(List<String> numbers) {
        Observable.just(numbers)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        StringBuilder builder = new StringBuilder();
                        for (String s : strings) {
                            builder.append(s + " ");
                        }
                        return Observable.just(builder.toString());
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        mTts.speak(s, TextToSpeech.QUEUE_ADD, null);
                    }
                });
    }

    /**
     * 关闭TTS
     */
    public void closeVoice() {
        if (null != mTts) {
            mTts.stop();
            mTts.shutdown();
        }
    }
}
