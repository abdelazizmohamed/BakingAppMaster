package com.example.home.baking_app.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.home.baking_app.JsonModels.Step;
import com.example.home.baking_app.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Home on 3/6/2018.
 */

public class StepsFragment extends Fragment implements ExoPlayer.EventListener {


    SimpleExoPlayerView simpleExoPlayerView;
    ArrayList<Step> steps;
    ImageView buttonBack, buttonNext, backToSteps;
    Step step;
    int id;
    long position = C.TIME_UNSET;
    Uri videoUri;
    boolean select = false;
    TextView stepDetails;
    SimpleExoPlayer simpleExoPlayer;
    boolean player;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_details, container, false);

        stepDetails = view.findViewById(R.id.step_Description);
        simpleExoPlayerView = view.findViewById(R.id.player_view);
        buttonBack = view.findViewById(R.id.Button_Back);
        buttonNext = view.findViewById(R.id.Button_Next);
        backToSteps = view.findViewById(R.id.Back);
        imageView = view.findViewById(R.id.image);

        // scrollView =view.findViewById(R.id.steps_fragment_scroll);
        player = true;

        steps = getArguments().getParcelableArrayList("steps");
        id = getArguments().getInt("id");

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = C.TIME_UNSET;
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    simpleExoPlayer.setPlayWhenReady(false);
                    simpleExoPlayer = null;
                }
                if (id == steps.size() - 1) {
                    return;
                }
                id++;
                click();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = C.TIME_UNSET;
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    simpleExoPlayer.setPlayWhenReady(false);
                    simpleExoPlayer = null;
                }
                if (id == 0) {
                    return;
                }
                id--;
                click();
            }
        });

        backToSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    simpleExoPlayer.setPlayWhenReady(false);
                    simpleExoPlayer = null;
                }
                getActivity().onBackPressed();
            }
        });

        simpleExoPlayer = null;
        if (savedInstanceState != null) {
            position = savedInstanceState.getLong("Resume video", C.TIME_UNSET);
            id = savedInstanceState.getInt("idResume", 1);
        }
        startVideo(id);


        return view;
    }

    void startVideo(int id) {
        if (simpleExoPlayer != null) {
            return;
        }

        int v = 0;
        while (v < steps.size()) {
            if (id == steps.get(v).getId()) {
                step = new Step(
                        id,
                        steps.get(v).getDescription(),
                        steps.get(v).getShortDescription(),
                        steps.get(v).getVideoURL(),
                        steps.get(v).getThumbnailURL()
                );

                if (!step.getVideoURL().equals("")) select = false;
                else select = true;



                stepDetails.setText(step.getDescription());

                if (!select) {
                    videoUri = Uri.parse(step.getVideoURL());
                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                    if (simpleExoPlayer != null) {
                        return;
                    }

                    load();
                } else {
                    simpleExoPlayerView.setVisibility(View.GONE);
                }
                select = false;
                this.select = false;
                this.step = null;

            }
            v++;
        }
    }

    void load() {
        if (simpleExoPlayer != null) {
            return;
        }
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayerView.setKeepScreenOn(true);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getActivity(), Util.getUserAgent(getActivity(), "ExoPlayer"));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        if (position != C.TIME_UNSET) {
            simpleExoPlayer.seekTo(position);
        }

        simpleExoPlayer.addListener(this);
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayer.setPlayWhenReady(player);

    }

    void ThumbnailURL() {
        if (step.getThumbnailURL() == null) {
            Picasso.with(getActivity())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.capture)
                    .error(R.drawable.capture)
                    .into(imageView);
        }
    }

    void click() {
        simpleExoPlayer = null;
        for (int i = 0; i < steps.size(); i++) {
            if (id == steps.get(i).getId()) {
                step = new Step(id,
                        steps.get(i).getShortDescription(),
                        steps.get(i).getDescription(),
                        steps.get(i).getVideoURL(),
                        steps.get(i).getThumbnailURL());
                if (!step.getVideoURL().equals(""))
                    select = false;
                else
                    select = true;
            }
        }


        stepDetails.setText(step.getDescription());

        if (!select) {
            videoUri = Uri.parse(step.getVideoURL());
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            load();
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
        }
        select = false;
        this.select = false;
        this.step = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //simpleExoPlayer.setPlayWhenReady(true);

        startVideo(id);
        //  simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (simpleExoPlayer != null) {
            position = simpleExoPlayer.getCurrentPosition();
            if (simpleExoPlayer != null) {
                simpleExoPlayer.stop();
                simpleExoPlayer.release();
                player = simpleExoPlayer.getPlayWhenReady();
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer = null;
            }
        }
    }

    //save value on onSaveInstanceState

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("Resume video", position);
        outState.putInt("idResume", id);
    }


    // overriding Exo methods
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

}

