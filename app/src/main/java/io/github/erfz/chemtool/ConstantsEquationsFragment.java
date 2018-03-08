package io.github.erfz.chemtool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.kexanie.library.MathView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConstantsEquationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConstantsEquationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConstantsEquationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConstantsEquationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConstantsEquationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConstantsEquationsFragment newInstance(String param1, String param2) {
        ConstantsEquationsFragment fragment = new ConstantsEquationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_constants_equations, container, false);
        MathView textView = v.findViewById(R.id.constants_mathview);
        textView.config(
                "MathJax.Hub.Config({\n" +
                        "   CommonHTML: { linebreaks: { automatic: true } },\n" +
                        "   \"HTML-CSS\": { linebreaks: { automatic: true } },\n" +
                        "   SVG: { linebreaks: { automatic: true } },\n" +
                        "   TeX: { extensions: [\"file:///android_asset/MathJax/extensions/TeX/mhchem.js\"] }\n" +
                        "});");
        String ex = "$$h = \\pu{ 6.62607004E−34 J s }$$" +
                "$$c = \\pu{ 2.998E8 m s−1 }$$" +
                "$$N_A = \\pu{ 6.022E23 mol−1 }$$" +
                "$$e = \\pu{ 1.602E−19 C }$$" +
                "$$\\begin{align}\n" +
                "R & = \\pu{ 8.314 J//mol K } \\\\\n" +
                "& = \\pu{ 0.08206 L atm//mol K } \\\\\n" +
                "& = \\pu{ 62.36 L torr//mol K }\n" +
                "\\end{align}$$\n" +
                "$$\\begin{align}\n" +
                "\\pu{1 atm} & = \\pu{ 760 mm Hg } \\\\\n" +
                "& = \\pu{ 760 torr }\n" +
                "\\end{align}$$\n" +
                "$$F = \\pu{ 96,485 C//mol e^- }$$" +
                "$$\\textrm{1 amu} = \\pu{ 1.660538E-27 kg }$$" +
                "$$k_e = \\pu{ 8.987551E9 N m2 C-2 }$$" +
                "$$k_b = \\pu{ 1.38065E-23 J K-1 }$$" +
                "$$R_\\infty = \\pu{ 1.0973731568539E7 m-1}$$";
        textView.setText(ex);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
