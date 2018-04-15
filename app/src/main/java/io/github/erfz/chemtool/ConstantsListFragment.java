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
    private static final String TEX_SIZE = "\\LARGE ";
    private static final String[] tex = {"h = 6.626\\,070\\,040(81)\\times{10^{-34}} \\text{ J s}",
            "c = 299\\,792\\,458 \\text{ m} \\text{ s}^{-1}}",
            "N_A = 6.022\\,140\\,857(74)\\times{10^{23}} \\text{ mol}^{-1}}",
            "e = 1.602\\,176\\,6208(98)\\times{10^{-19}} \\text{ C}",
            "\\begin{align}" +
                    "R & = 8.314\\,4598(48) \\text{ J} \\text{ K}^{-1} \\text{ mol}^{-1} \\\\" +
                    "& = 0.082\\,057\\,338(47) \\text{ L atm} \\text{ K}^{-1} \\text{ mol}^{-1} \\\\" +
                    "& = 62.363\\,577(36) \\text{ L Torr} \\text{ K}^{-1} \\text{ mol}^{-1}" +
                    "\\end{align}",
            "\\begin{align}" +
                    "1 \\text{ atm} & = 760 \\text{ mm Hg} \\\\ " +
                    "& = 760 \\text{ Torr} \\\\" +
                    "\\end{align}",
            "F = 96\\,485.332\\,89(59) \\text{ C} \\text{ mol}^{-1}}",
            "1 \\text{ amu} = 1.660\\,539\\,040(20)\\times{10^{-27}} \\text{ kg}",
            "k_e = 8.987\\,551\\,787\\,368\\,1764\\times{10^9} \\text{ N} \\text{ m}^2 \\text{ C}^{-2}}",
            "k_b = 1.380\\,648\\,52(79)\\times{10^{-23}} \\text{ J} \\text{ K}^{-1}",
            "R_\\infty = 1.097\\,373\\,156\\,8508(65)\\times{10^7} \\text{ m}^{-1}"};

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
                        texView.setText("$$" + TEX_SIZE + tex[position] + "$$");
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
