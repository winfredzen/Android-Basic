// Generated by view binder compiler. Do not edit!
package com.raywenderlich.android.memo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.raywenderlich.android.memo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnPauseMusic;

  @NonNull
  public final Button btnPlay;

  @NonNull
  public final Button btnPlayMusic;

  @NonNull
  public final Button btnQuit;

  @NonNull
  public final Button btnShuffleMusic;

  @NonNull
  public final Button btnSongName;

  @NonNull
  public final Button btnStopMusic;

  @NonNull
  public final GridView gridview;

  @NonNull
  public final ConstraintLayout musicOptions;

  @NonNull
  public final TextView tvTime;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnPauseMusic,
      @NonNull Button btnPlay, @NonNull Button btnPlayMusic, @NonNull Button btnQuit,
      @NonNull Button btnShuffleMusic, @NonNull Button btnSongName, @NonNull Button btnStopMusic,
      @NonNull GridView gridview, @NonNull ConstraintLayout musicOptions,
      @NonNull TextView tvTime) {
    this.rootView = rootView;
    this.btnPauseMusic = btnPauseMusic;
    this.btnPlay = btnPlay;
    this.btnPlayMusic = btnPlayMusic;
    this.btnQuit = btnQuit;
    this.btnShuffleMusic = btnShuffleMusic;
    this.btnSongName = btnSongName;
    this.btnStopMusic = btnStopMusic;
    this.gridview = gridview;
    this.musicOptions = musicOptions;
    this.tvTime = tvTime;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnPauseMusic;
      Button btnPauseMusic = rootView.findViewById(id);
      if (btnPauseMusic == null) {
        break missingId;
      }

      id = R.id.btnPlay;
      Button btnPlay = rootView.findViewById(id);
      if (btnPlay == null) {
        break missingId;
      }

      id = R.id.btnPlayMusic;
      Button btnPlayMusic = rootView.findViewById(id);
      if (btnPlayMusic == null) {
        break missingId;
      }

      id = R.id.btnQuit;
      Button btnQuit = rootView.findViewById(id);
      if (btnQuit == null) {
        break missingId;
      }

      id = R.id.btnShuffleMusic;
      Button btnShuffleMusic = rootView.findViewById(id);
      if (btnShuffleMusic == null) {
        break missingId;
      }

      id = R.id.btnSongName;
      Button btnSongName = rootView.findViewById(id);
      if (btnSongName == null) {
        break missingId;
      }

      id = R.id.btnStopMusic;
      Button btnStopMusic = rootView.findViewById(id);
      if (btnStopMusic == null) {
        break missingId;
      }

      id = R.id.gridview;
      GridView gridview = rootView.findViewById(id);
      if (gridview == null) {
        break missingId;
      }

      id = R.id.musicOptions;
      ConstraintLayout musicOptions = rootView.findViewById(id);
      if (musicOptions == null) {
        break missingId;
      }

      id = R.id.tvTime;
      TextView tvTime = rootView.findViewById(id);
      if (tvTime == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, btnPauseMusic, btnPlay,
          btnPlayMusic, btnQuit, btnShuffleMusic, btnSongName, btnStopMusic, gridview, musicOptions,
          tvTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
