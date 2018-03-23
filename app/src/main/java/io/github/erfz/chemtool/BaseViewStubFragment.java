package io.github.erfz.chemtool;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 * Created by zfrs on 3/22/2018.
 */

public abstract class BaseViewStubFragment extends Fragment {
    private Bundle mSavedInstanceState;
    private boolean mHasInflated = false;
    private ViewStub mViewStub;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewstub, container, false);
        mViewStub = view.findViewById(R.id.fragmentViewStub);
        mViewStub.setLayoutResource(getViewStubLayoutResource());
        mSavedInstanceState = savedInstanceState;

        if (getUserVisibleHint() && !mHasInflated) {
            View inflatedView = mViewStub.inflate();
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState);
            afterViewStubInflated(view);
        }

        return view;
    }

    protected abstract void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState);

    /**
     * The layout ID associated with this ViewStub
     *
     * @return
     * @see ViewStub#setLayoutResource(int)
     */
    @LayoutRes
    protected abstract int getViewStubLayoutResource();

    /**
     * @param originalViewContainerWithViewStub
     */
    @CallSuper
    protected void afterViewStubInflated(View originalViewContainerWithViewStub) {
        mHasInflated = true;
        if (originalViewContainerWithViewStub != null) {
            View pb = originalViewContainerWithViewStub.findViewById(R.id.inflateProgressbar);
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mViewStub != null && !mHasInflated) {
            View inflatedView = mViewStub.inflate();
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState);
            afterViewStubInflated(getView());
        }
    }

    // Thanks to Noa Drach, this will fix the orientation change problem
    @Override
    public void onDetach() {
        super.onDetach();
        mHasInflated = false;
    }
}
