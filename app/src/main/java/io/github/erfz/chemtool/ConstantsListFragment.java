package io.github.erfz.chemtool;

import android.content.Context;
import android.net.Uri;

public class ConstantsListFragment extends BaseTexListFragment {
    private OnFragmentInteractionListener mListener;
    private static final String[] constantsTexArray = {"h = 6.626\\,070\\,040(81)\\times{10^{-34}} \\text{ J s}",
            "c = 299\\,792\\,458 \\text{ m} \\text{ s}^{-1}}",
            "N_A = 6.022\\,140\\,857(74)\\times{10^{23}} \\text{ mol}^{-1}}",
            "e = 1.602\\,176\\,6208(98)\\times{10^{-19}} \\text{ C}",
            "\\begin{align}" +
                    "R & = 8.314\\,4598(48) \\text{ J} \\text{ K}^{-1} \\text{ mol}^{-1} \\\\" +
                    "& = 0.082\\,057\\,338(47) \\text{ L atm} \\text{ K}^{-1} \\text{ mol}^{-1} \\\\" +
                    "& = 62.363\\,577(36) \\text{ L Torr} \\text{ K}^{-1} \\text{ mol}^{-1}" +
                    "\\end{align}",
            "\\begin{align}" +
                    "1 \\text{ atm} & = 760 \\text{ mm Hg} \\\\" +
                    "& = 760 \\text{ Torr}" +
                    "\\end{align}",
            "F = 96\\,485.332\\,89(59) \\text{ C} \\text{ mol}^{-1}}",
            "1 \\text{ amu} = 1.660\\,539\\,040(20)\\times{10^{-27}} \\text{ kg}",
            "k_e = 8.987\\,551\\,787\\,368\\,1764\\times{10^9} \\text{ N} \\text{ m}^2 \\text{ C}^{-2}}",
            "k_b = 1.380\\,648\\,52(79)\\times{10^{-23}} \\text{ J} \\text{ K}^{-1}",
            "R_\\infty = 1.097\\,373\\,156\\,8508(65)\\times{10^7} \\text{ m}^{-1}"};

    @Override
    protected String[] getTexArray() {
        return constantsTexArray;
    }

    @Override
    protected String[] getListDataset() {
        return getResources().getStringArray(R.array.constants_list_data);
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
