package io.github.erfz.chemtool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.kexanie.library.MathView;

public class ConstantsEquationsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_constants_equations, container, false);
        MathView mathView = v.findViewById(R.id.mathview);
        mathView.config(
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
                "$$R_\\infty = \\pu{ 1.0973731568539E7 m-1}$$" +
                "$$\\textrm{Molarity} = \\frac{\\textrm{moles of solute}}{\\textrm{volume of solution}}$$" +
                "$$\\textrm{Molality} = \\frac{\\textrm{moles of solute}}{\\textrm{mass of solvent in kg}}$$" +
                "$$\\textrm{Rate of Reaction} = \\frac{\\Delta \\textrm{concentration}}{\\Delta \\textrm{time}}$$" +
                "$$\\lambda = \\frac{h}{mv}$$" +
                "$$E = hv$$" +
                "$$c = \\lambda v$$" +
                "$$F_e = k_e \\frac{Q_1 Q_2}{r^2}$$" +
                "$$P_\\textrm{solution} = P_1 \\chi_1 + P_2 \\chi_2 + ...$$" +
                "$$\\Delta T_\\textrm{solution} = K_b m_\\textrm{solute}$$" +
                "$$\\Delta T_\\textrm{solution} = K_f m_\\textrm{solute}$$" +
                "$$PV = nRT$$" +
                "$$pH = -log[H^+]$$" +
                "$$pOH = -log[OH^-]$$" +
                "$$K_p = K_c(RT)^{\\Delta n}$$" +
                "$$\\textrm{For }aA + bB \\rightarrow cC + dD\\colon \\\\ K_{\\textrm{eq}} = \\frac{[C]^c [D]^d}{[A]^a [B]^b}$$" +
                "$$q = mc\\Delta T$$" +
                "$$\\Delta H = H_{\\textrm{products}} - H_{\\textrm{reactants}}$$" +
                "$$\\Delta S = S_{\\textrm{products}} - S_{\\textrm{reactants}}$$" +
                "$$\\Delta G = \\Delta H - T\\Delta S$$";
        mathView.setText(ex);
        return v;
    }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
