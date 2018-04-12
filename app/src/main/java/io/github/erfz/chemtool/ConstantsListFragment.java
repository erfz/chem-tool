package io.github.erfz.chemtool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daquexian.flexiblerichtextview.FlexibleRichTextView;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

public class ConstantsListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String tex = "$$\\Huge h = 6.626\\,070\\,040(81)\\times{10^{-34}} \\text{ J s}$$";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_constants_list, container, false);
        mRecyclerView = v.findViewById(R.id.constants_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ConstantsListAdapter(getResources().getStringArray(R.array.constants_array));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        AjLatexMath.init(requireContext());

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        MaterialDialog dialog = new MaterialDialog.Builder(requireContext())
                                .title(((TextView) v).getText().toString())
                                .customView(R.layout.dialog_fragment_tex, false)
                                .positiveText("Close")
                                .build();
                        FlexibleRichTextView texView = dialog.getCustomView().findViewById(R.id.tex_view);
                        texView.setText(tex);
                        dialog.show();
                    }
                }
        );

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
