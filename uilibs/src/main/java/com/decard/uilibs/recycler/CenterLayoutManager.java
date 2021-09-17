package com.decard.uilibs.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJ
 * create at 2021/6/23 16:23
 */
class CenterLayoutManager extends LinearLayoutManager {

	public CenterLayoutManager(Context context) {
		this(context, null, 0, 0);
	}

	public CenterLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}

	public CenterLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
	                           int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
	                                   int position) {
		RecyclerView.SmoothScroller smoothScroller =
				new CenterSmoothScroller(recyclerView.getContext());
		smoothScroller.setTargetPosition(position);
		startSmoothScroll(smoothScroller);

	}

	private static class CenterSmoothScroller extends LinearSmoothScroller {

		CenterSmoothScroller(Context context) {
			super(context);
		}

		@Override
		public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd,
		                            int snapPreference) {
			return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
		}
	}

}

