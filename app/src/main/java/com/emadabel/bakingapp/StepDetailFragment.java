package com.emadabel.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emadabel.bakingapp.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    private static final String KEY_AUTO_PLAY = "auto_play";
    private static final String KEY_PLAYBACK_POSITION = "playback_position";
    private static final String KEY_CURRENT_WINDOW = "current_window";
    private static final String KEY_STEP_LIST = "step_list";
    private static final String KEY_LIST_INDEX = "list_index";

    @Nullable
    @BindView(R.id.step_description_tv)
    TextView mStepDescriptionTextView;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.empty_player)
    ImageView mEmptyPlayer;

    @Nullable
    @BindView(R.id.next_step_button)
    Button mNextStepButton;

    @Nullable
    @BindView(R.id.previous_step_button)
    Button mPreviousStepButton;

    OnStepChangedListener mCallback;
    private SimpleExoPlayer mExoPlayer;
    private DefaultTrackSelector trackSelector;

    private Context mContext;

    private boolean startAutoPlay;
    private long playbackPosition;
    private int currentWindow;

    private int mListIndex;
    private List<Step> mSteps;

    public StepDetailFragment() {
    }

    public void setStepList(List<Step> stepList) {
        mSteps = stepList;
    }

    public void setListIndex(int index) {
        mListIndex = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            mSteps = savedInstanceState.getParcelableArrayList(KEY_STEP_LIST);
            mListIndex = savedInstanceState.getInt(KEY_LIST_INDEX);
        } else {
            clearStartPosition();
        }

        mContext = getContext();

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        /*
         * reference: https://stackoverflow.com/questions/13877154/where-to-check-for-orientation-change-in-an-android-fragment
         */
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE && mStepDescriptionTextView == null) {
            // Landscape
            hideSystemUi();
            if (TextUtils.isEmpty(getVideoUrl())) {
                mPlayerView.setVisibility(View.INVISIBLE);
                mEmptyPlayer.setVisibility(View.VISIBLE);
            } else {
                mPlayerView.setVisibility(View.VISIBLE);
                mEmptyPlayer.setVisibility(View.INVISIBLE);
            }
        }

        if (mCallback != null)
            mCallback.updateActionBarTitle(mSteps.get(mListIndex).getShortDescription());

        if (mStepDescriptionTextView != null) {
            mStepDescriptionTextView.setText(mSteps.get(mListIndex).getDescription());
        }

        if (mNextStepButton != null && mPreviousStepButton != null) {
            mNextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListIndex < mSteps.size() - 1) {
                        mListIndex++;
                    }
                    playbackPosition = 0;
                    mCallback.updateActionBarTitle(mSteps.get(mListIndex).getShortDescription());
                    String videoUrl = getVideoUrl();
                    if (TextUtils.isEmpty(videoUrl)) {
                        mPlayerView.setVisibility(View.INVISIBLE);
                        mEmptyPlayer.setVisibility(View.VISIBLE);
                    } else {
                        initializePlayer(Uri.parse(videoUrl));
                        mPlayerView.setVisibility(View.VISIBLE);
                        mEmptyPlayer.setVisibility(View.INVISIBLE);
                    }
                    mStepDescriptionTextView.setText(mSteps.get(mListIndex).getDescription());
                }
            });

            mPreviousStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListIndex > 0) {
                        mListIndex--;
                    }
                    playbackPosition = 0;
                    mCallback.updateActionBarTitle(mSteps.get(mListIndex).getShortDescription());
                    String videoUrl = getVideoUrl();
                    if (TextUtils.isEmpty(videoUrl)) {
                        mPlayerView.setVisibility(View.INVISIBLE);
                        mEmptyPlayer.setVisibility(View.VISIBLE);
                    } else {
                        initializePlayer(Uri.parse(videoUrl));
                        mPlayerView.setVisibility(View.VISIBLE);
                        mEmptyPlayer.setVisibility(View.INVISIBLE);
                    }
                    mStepDescriptionTextView.setText(mSteps.get(mListIndex).getDescription());
                }
            });
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnListClickListener");
        }
    }

    /**
     * reference: https://github.com/googlecodelabs/exoplayer-intro
     */
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mExoPlayer.setPlayWhenReady(startAutoPlay);
            mPlayerView.setPlayer(mExoPlayer);
        }
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(mContext, "BakingTime");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory(userAgent),
                new DefaultExtractorsFactory(), null, null);
        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            mExoPlayer.seekTo(currentWindow, playbackPosition);
        }
        mExoPlayer.prepare(mediaSource, !haveStartPosition, false);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private String getVideoUrl() {
        if (!TextUtils.isEmpty(mSteps.get(mListIndex).getVideoURL())) {
            return mSteps.get(mListIndex).getVideoURL();
        } else if (!TextUtils.isEmpty(mSteps.get(mListIndex).getThumbnailURL())) {
            return mSteps.get(mListIndex).getThumbnailURL();
        } else {
            return "";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(mSteps.get(mListIndex).getVideoURL()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer(Uri.parse(mSteps.get(mListIndex).getVideoURL()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            updateStartPosition();
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            startAutoPlay = mExoPlayer.getPlayWhenReady();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playbackPosition = mExoPlayer.getCurrentPosition();
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        currentWindow = C.INDEX_UNSET;
        playbackPosition = C.TIME_UNSET;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        currentState.putLong(KEY_PLAYBACK_POSITION, playbackPosition);
        currentState.putInt(KEY_CURRENT_WINDOW, currentWindow);
        currentState.putParcelableArrayList(KEY_STEP_LIST, new ArrayList<Parcelable>(mSteps));
        currentState.putInt(KEY_LIST_INDEX, mListIndex);
    }

    public interface OnStepChangedListener {
        void updateActionBarTitle(String title);
    }
}