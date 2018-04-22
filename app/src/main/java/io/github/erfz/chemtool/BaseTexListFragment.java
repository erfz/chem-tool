package io.github.erfz.chemtool;

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

public abstract class BaseTexListFragment extends Fragment {
    private static final String DEFAULT_TEX_SIZE = "\\LARGE ";
    private RecyclerView mRecyclerView;
    private String[] mTexArray;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_tex_list, container, false);
        mRecyclerView = v.findViewById(R.id.tex_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setAdapter(new TexListAdapter(getListDataset()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        mTexArray = getTexArray();
        AjLatexMath.init(requireContext()); //TODO: address multiple jLatexMath inits

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        MaterialDialog dialog = new MaterialDialog.Builder(requireContext())
                                .title(((TextView) v).getText().toString())
                                .customView(R.layout.dialog_fragment_tex, true)
                                .positiveText("Close")
                                .build();
                        FlexibleRichTextView texView = dialog.getCustomView().findViewById(R.id.tex_view);
                        texView.setText("$$" + DEFAULT_TEX_SIZE + mTexArray[position] + "$$");
                        dialog.show();
                    }
                }
        );

        return v;
    }

    protected abstract String[] getTexArray();

    protected abstract String[] getListDataset();
}
