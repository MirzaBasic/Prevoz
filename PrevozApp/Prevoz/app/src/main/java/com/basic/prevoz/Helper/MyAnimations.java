package com.basic.prevoz.Helper;

import android.animation.Animator;
import android.content.Context;
import android.view.View;

/**
 * Created by Developer on 16.05.2017..
 */

public class MyAnimations {


    public static void showChatDetails(final View v){



        v.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {


                    @Override
                    public void onAnimationStart(Animator animation) {
                        v.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        v.clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
    }




    public static void hideChatDetails(final View v){



        v.animate()
                .translationY(v.getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {


                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        v.setVisibility(View.GONE);
                        v.clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
    }


    public static void showFloatingButton(final View v){


        v.setEnabled(true);
        v.animate()
                .alpha(1.0f).scaleX(1).scaleY(1).setDuration(150);


    }




    public static void hideFloatingButton(final View v){


        v.setEnabled(false);
        v.animate().
                alpha(0.0f).scaleX(0).scaleY(0).setDuration(150);


    }
}
