package io.github.erfz.chemtool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.sidvenu.mathjaxview.MathJaxView;


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
        MathJaxView textView = v.findViewById(R.id.example_text_view);
        String ex = "$$\\begin{align}\n" +
                "\\dot{x} & = \\sigma(y-x) \\\\\n" +
                "\\dot{y} & = \\rho x - y - xz \\\\\n" +
                "\\dot{z} & = -\\beta z + xy\n" +
                "\\end{align}$$";
        ex += "$$\\begin{equation*}\n" +
                "\\left( \\sum_{k=1}^n a_k b_k \\right)^2 \\leq \\left( \\sum_{k=1}^n a_k^2 \\right) \\left( \\sum_{k=1}^n b_k^2 \\right)\n" +
                "\\end{equation*}$$";
        ex += "$$\\begin{equation*}\n" +
                "\\mathbf{V}_1 \\times \\mathbf{V}_2 =  \\begin{vmatrix}\n" +
                "\\mathbf{i} & \\mathbf{j} & \\mathbf{k} \\\\\n" +
                "\\frac{\\partial X}{\\partial u} &  \\frac{\\partial Y}{\\partial u} & 0 \\\\\n" +
                "\\frac{\\partial X}{\\partial v} &  \\frac{\\partial Y}{\\partial v} & 0\n" +
                "\\end{vmatrix}\n" +
                "\\end{equation*}$$";
        ex += "$$\\begin{equation*}\n" +
                "P(E)   = {n \\choose k} p^k (1-p)^{ n-k}\n" +
                "\\end{equation*}$$";
        ex += "$$\\begin{equation*}\n" +
                "\\frac{1}{\\Bigl(\\sqrt{\\phi \\sqrt{5}}-\\phi\\Bigr) e^{\\frac25 \\pi}} =\n" +
                "1+\\frac{e^{-2\\pi}} {1+\\frac{e^{-4\\pi}} {1+\\frac{e^{-6\\pi}}\n" +
                "{1+\\frac{e^{-8\\pi}} {1+\\ldots} } } }\n" +
                "\\end{equation*}$$";
        ex += "$$\\begin{equation*}\n" +
                "1 +  \\frac{q^2}{(1-q)}+\\frac{q^6}{(1-q)(1-q^2)}+\\cdots =\n" +
                "\\prod_{j=0}^{\\infty}\\frac{1}{(1-q^{5j+2})(1-q^{5j+3})},\n" +
                "\\quad\\quad \\text{for $|q|<1$}.\n" +
                "\\end{equation*}$$";
        ex += "$$\\begin{align}\n" +
                "\\nabla \\times \\vec{\\mathbf{B}} -\\, \\frac1c\\, \\frac{\\partial\\vec{\\mathbf{E}}}{\\partial t} & = \\frac{4\\pi}{c}\\vec{\\mathbf{j}} \\\\   \\nabla \\cdot \\vec{\\mathbf{E}} & = 4 \\pi \\rho \\\\\n" +
                "\\nabla \\times \\vec{\\mathbf{E}}\\, +\\, \\frac1c\\, \\frac{\\partial\\vec{\\mathbf{B}}}{\\partial t} & = \\vec{\\mathbf{0}} \\\\\n" +
                "\\nabla \\cdot \\vec{\\mathbf{B}} & = 0\\end{align}$$";
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
