package com.example.prueba.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;
import com.example.prueba.R;

public class LikeAnimator {

    private boolean like = false;

    public void setLikeAnimation(final LottieAnimationView imageView,final int animationResource) {
        imageView.setOnClickListener(view -> like = likeAnimation(imageView, animationResource, like));
    }

    private boolean likeAnimation(final LottieAnimationView imageView, final int animationResource, boolean like) {
        if (!like) {
            imageView.setAnimation(animationResource);
            imageView.playAnimation();
        } else {
            imageView.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            imageView.setImageResource(R.drawable.twitter_like);
                            imageView.setAlpha(1f);
                        }
                    });
        }
        return !like;
    }
}
