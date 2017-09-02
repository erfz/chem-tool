package io.github.erfz.chemtool;

import java.util.HashMap;

/**
 * Created by lebesgue on 9/1/17.
 */

class StoichiConvert {
    private static final HashMap<String, Double> map = new HashMap<String, Double>();
    static{
        map.put("H", 1.008);
        map.put("He", 4.0026);
        map.put("Li", 6.94);
        map.put("Be", 9.0122);
        map.put("B", 10.81);
        map.put("C", 12.011);
        map.put("N", 14.007);
        map.put("O", 15.999);
        map.put("F", 18.998);
        map.put("Ne", 20.180);
        map.put("Na", 22.990);
        map.put("Mg", 24.305);
        map.put("Al", 26.982);
        map.put("Si", 28.085);
        map.put("P", 30.974);
        map.put("S", 32.06);
        map.put("Cl", 35.45);
        map.put("Ar", 39.948);
        map.put("K", 39.098);
        map.put("Ca", 40.078);
        map.put("Sc", 44.956);
        map.put("Ti", 47.867);
        map.put("V", 50.942);
        map.put("Cr", 51.996);
        map.put("Mn", 54.938);
        map.put("Fe", 55.845);
        map.put("Co", 58.933);
        map.put("Ni", 58.693);
        map.put("Cu", 63.546);
        map.put("Zn", 65.38);
        map.put("Ga", 69.723);
        map.put("Ge", 72.630);
        map.put("As", 74.922);
        map.put("Se",78.971);
        map.put("Br", 79.904);
        map.put("Kr", 83.798);
        map.put("Rb", 85.468);
        map.put("Sr", 87.62);
        map.put("Y", 88.906);
        map.put("Zr", 91.224);
        map.put("Nb", 92.906);
        map.put("Mo", 95.95);
        map.put("Tc", 98.0);
        map.put("Ru", 101.07);
        map.put("Rh", 102.91);
        map.put("Pd", 106.42);
        map.put("Ag", 107.87);
        map.put("Cd", 112.41);
        map.put("In", 114.82);
        map.put("Sn", 118.71);
        map.put("Sb", 121.76);
        map.put("Te", 127.60);
        map.put("I", 126.90);
        map.put("Xe", 131.29);
        map.put("Cs", 132.91);
        map.put("Ba", 137.33);
        map.put("La", 138.91);
        map.put("Ce", 140.12);
        map.put("Pr", 140.91);
        map.put("Nd", 144.24);
        map.put("Pm", 145.0);
        map.put("Sm", 150.36);
        map.put("Eu", 151.96);
        map.put("Gd", 157.25);
        map.put("Td", 158.93);
        map.put("Dy", 162.50);
        map.put("Ho", 164.93);
        map.put("Er", 167.26);
        map.put("Tm", 168.93);
        map.put("Yb", 173.05);
        map.put("Lu", 174.97);
        map.put("Hf", 178.49);
        map.put("Ta", 180.95);
        map.put("W", 183.84);
        map.put("Re", 186.21);
        map.put("Os", 190.23);
        map.put("Ir", 192.22);
        map.put("Pt", 195.08);
        map.put("Au", 196.97);
        map.put("Hg", 200.59);
        map.put("Tl", 204.38);
        map.put("Pb", 207.2);
        map.put("Bi", 208.98);
        map.put("Po", 209.0);
        map.put("At", 210.0);
        map.put("Rn", 222.0);
        map.put("Fr", 223.0);
        map.put("Ra", 226.0);
        map.put("Ac", 227.0);
        map.put("Th", 232.04);
        map.put("Pa", 231.04);
        map.put("U", 238.03);
        map.put("Np", 237.0);
        map.put("Pu", 244.0);
        map.put("Am", 243.0);
        map.put("Cm", 247.0);
        map.put("Bk", 247.0);
        map.put("Cf", 251.0);
        map.put("Es", 252.0);
        map.put("Fm", 257.0);
        map.put("Md", 258.0);
        map.put("No", 259.0);
        map.put("Lr", 266.0);
        map.put("Rf", 267.0);
        map.put("Db", 268.0);
        map.put("Sg", 269.0);
        map.put("Bh", 270.0);
        map.put("Hs", 277.0);
        map.put("Mt", 278.0);
        map.put("Ds", 281.0);
        map.put("Rg", 282.0);
        map.put("Cn", 285.0);
        map.put("Nh", 286.0);
        map.put("Fl", 289.0);
        map.put("Mc", 290.0);
        map.put("Lv", 293.0);
        map.put("Ts", 294.0);
        map.put("Og", 294.0);
    }

    static double gramsToMoles(String formula, double mass){
        return mass/getFormulaMass(formula);
    }

    static double molesToGrams(String formula, double moles){
        return moles*getFormulaMass(formula);
    }

    private static double getFormulaMass(String formula){
        double totalMass = 0;
        formula = formula.replaceAll("^\\d+",""); // must first nuke leading coeff. so that paren/brckt removal is correct
        formula = EquationBalance.removeParenthesesAndBrackets(formula);
        String[] formulaArray = formula.split("(?=\\p{Upper})");

        for (String str : formulaArray){
            String element = str.replaceAll("\\d", "");
            double elementMass = map.get(element);
            double numElement = Double.parseDouble(str.replaceAll("(\\p{Alpha})", ""));
            totalMass += elementMass * numElement;
        }

        return totalMass;
    }
}
