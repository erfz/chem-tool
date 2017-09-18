package io.github.erfz.chemtool;

import java.util.Arrays;

/**
 * Created by riesz on 8/19/2016.
 */

class EquationBalance { // put everything into a neat class
    static String balanceEquation(String equation) {
        String[] eqnHS = equation.replaceAll("\\s+", "").split("=");
        if (eqnHS.length != 2) throw new IllegalArgumentException("Your equation is invalid.");

        String[] eqnLHSplit = eqnHS[0].split("\\++");
        String[] eqnRHSplit = eqnHS[1].split("\\++");

        for (int i = 0; i < eqnLHSplit.length; ++i) { // nuke user-entered coefficients
            eqnLHSplit[i] = eqnLHSplit[i]
                    .replaceAll("^\\d+", "")
                    .replaceAll("(?<=\\()\\d+", "");
        }

        for (int i = 0; i < eqnRHSplit.length; ++i) {
            eqnRHSplit[i] = eqnRHSplit[i]
                    .replaceAll("^\\d+", "")
                    .replaceAll("(?<=\\()\\d+", "");
        }

        String[] lhsPrintArray = new String[eqnLHSplit.length];
        String[] rhsPrintArray = new String[eqnRHSplit.length];
        System.arraycopy(eqnLHSplit, 0, lhsPrintArray, 0, lhsPrintArray.length);
        System.arraycopy(eqnRHSplit, 0, rhsPrintArray, 0, rhsPrintArray.length);

        doSubstitutions(eqnHS, eqnLHSplit, eqnRHSplit);

        for (int i = 0; i < eqnLHSplit.length; ++i) {
            eqnLHSplit[i] = parseFormula(eqnLHSplit[i]);
        }

        for (int i = 0; i < eqnRHSplit.length; ++i) {
            eqnRHSplit[i] = parseFormula(eqnRHSplit[i]);
        }

        String[] eqnLHEle = null;
        String[] eqnRHEle = null;
        {
            String[] tempLHEle = eqnHS[0].replaceAll("(\\+|\\d|\\(|\\)|⋅)", "").split("(?=\\p{Upper})");
            String[] tempRHEle = eqnHS[1].replaceAll("(\\+|\\d|\\(|\\)|⋅)", "").split("(?=\\p{Upper})");

            int LHElecount = 0;
            int RHElecount = 0;
            for (int i = 0; i < tempLHEle.length; ++i) {
                if (!tempLHEle[i].equals("")) {
                    ++LHElecount;
                }
            }
            for (int i = 0; i < tempRHEle.length; ++i) {
                if (!tempRHEle[i].equals("")) {
                    ++RHElecount;
                }
            }

            eqnLHEle = new String[LHElecount];
            eqnRHEle = new String[RHElecount];

            int count = 0;
            for (int i = 0; i < tempLHEle.length; ++i) {
                if (!tempLHEle[i].equals("")) {
                    eqnLHEle[count] = tempLHEle[i];
                    ++count;
                }
            }
            count = 0;
            for (int i = 0; i < tempRHEle.length; ++i) {
                if (!tempRHEle[i].equals("")) {
                    eqnRHEle[count] = tempRHEle[i];
                    ++count;
                }
            }

        }


        int numElements = 0;
        { // find actual # of elements
            int counter = 0;
            for (int i = 0; i < eqnLHEle.length - 1; ++i) {
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnLHEle.length; ++j) {
                    if (eqnLHEle[i].equals(eqnLHEle[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numElementsL = eqnLHEle.length - counter;

            counter = 0;
            for (int i = 0; i < eqnRHEle.length - 1; ++i) {
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnRHEle.length; ++j) {
                    if (eqnRHEle[i].equals(eqnRHEle[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numElementsR = eqnRHEle.length - counter;

            if (numElementsL == numElementsR) numElements = numElementsL;
            else throw new IllegalArgumentException("Your equation is invalid.");
        }

        String[] eqnElements = new String[numElements];
        {
            int count = 0;
            String[] tempArray = null;
            if (eqnLHEle.length <= eqnRHEle.length) tempArray = eqnLHEle;
            else tempArray = eqnRHEle;

            for (int i = 0; i < tempArray.length - 1; ++i) {
                boolean eleBoolean = true;
                for (int j = i + 1; j < tempArray.length; ++j) {
                    if (tempArray[i].equals(tempArray[j])) eleBoolean = false;
                }
                if (eleBoolean) {
                    eqnElements[count] = tempArray[i];
                    ++count;
                }
            }
            eqnElements[eqnElements.length - 1] = tempArray[tempArray.length - 1];
        }

        for (String LHEle : eqnLHEle) {
            boolean elementMatch = false;
            for (String RHEle : eqnRHEle) {
                if (LHEle.equals(RHEle)) elementMatch = true;
            }
            if (!elementMatch) throw new IllegalArgumentException("Your equation is invalid.");
        }

        int numMolecules = 0;
        int matrixNegIndex = 0;
        { // find actual # of molecules
            int counter = 0;
            for (int i = 0; i < eqnLHSplit.length - 1; ++i) {
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnLHSplit.length; ++j) {
                    if (eqnLHSplit[i].equals(eqnLHSplit[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numMoleculesL = eqnLHSplit.length - counter;
            matrixNegIndex = numMoleculesL;

            counter = 0;
            for (int i = 0; i < eqnRHSplit.length - 1; ++i) {
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnRHSplit.length; ++j) {
                    if (eqnRHSplit[i].equals(eqnRHSplit[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numMoleculesR = eqnRHSplit.length - counter;

            numMolecules = numMoleculesL + numMoleculesR;
        }

        String[] eqnMolecules = new String[numMolecules];
        {
            int count = 0;

            for (int i = 0; i < eqnLHSplit.length - 1; ++i) {
                boolean eleBoolean = true;
                for (int j = i + 1; j < eqnLHSplit.length; ++j) {
                    if (eqnLHSplit[i].equals(eqnLHSplit[j])) eleBoolean = false;
                }
                if (eleBoolean) {
                    eqnMolecules[count] = eqnLHSplit[i];
                    ++count;
                }
            }
            ++count;
            eqnMolecules[count - 1] = eqnLHSplit[eqnLHSplit.length - 1];

            for (int i = 0; i < eqnRHSplit.length - 1; ++i) {
                boolean eleBoolean = true;
                for (int j = i + 1; j < eqnRHSplit.length; ++j) {
                    if (eqnRHSplit[i].equals(eqnRHSplit[j])) eleBoolean = false;
                }
                if (eleBoolean) {
                    eqnMolecules[count] = eqnRHSplit[i];
                    ++count;
                }
            }
            eqnMolecules[eqnMolecules.length - 1] = eqnRHSplit[eqnRHSplit.length - 1];
        }

        double[][] eqnSolnSys = new double[numElements][numMolecules + 1];

        for (int i = 0; i < numElements; ++i) {
            for (int j = 0; j < numMolecules; ++j) {
                int lastEleIndex = 0;
                while (eqnMolecules[j].contains(eqnElements[i]) && eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) != -1) {
                    if (eqnMolecules[j].substring(eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length(), eqnMolecules[j].length()).replaceAll("(\\d|\\p{Upper}).*", "").isEmpty()) {
                        lastEleIndex = eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length();
                        String temp = eqnMolecules[j].substring(lastEleIndex, eqnMolecules[j].length()).replaceAll("\\p{Alpha}.*", "");
                        if (temp.isEmpty()) eqnSolnSys[i][j] += 1;
                        else eqnSolnSys[i][j] += Double.parseDouble(temp);
                    } else
                        lastEleIndex = eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length();
                }
            }
        }

        for (int i = 0; i < eqnSolnSys.length; ++i) {
            for (int j = matrixNegIndex; j < eqnSolnSys[0].length; ++j) {
                if (eqnSolnSys[i][j] != 0) {
                    eqnSolnSys[i][j] *= -1;
                }
            }
        }

        // Solve matrix

        int numFreeVariables = 0;
        if (numMolecules - numElements <= 0)
            numFreeVariables = 1; // not strictly the number of free variables in this case...still need to set one coefficient = 1 regardless
        else numFreeVariables = numMolecules - numElements;
        int[][] nthMoleculeElementIndex = null;
        {
            String[][] nthMoleculeElements = new String[numFreeVariables][];
            for (int i = 0; i < numFreeVariables; ++i) { // add RHSplit
                if (i < eqnLHSplit.length)
                    nthMoleculeElements[i] = eqnLHSplit[i].split("(?=\\p{Upper})");
                else
                    nthMoleculeElements[i] = eqnRHSplit[i - eqnLHSplit.length].split("(?=\\p{Upper})");
            }

            for (int i = 0; i < nthMoleculeElements.length; ++i) {
                for (int j = 0; j < nthMoleculeElements[i].length; ++j) {
                    nthMoleculeElements[i][j] = nthMoleculeElements[i][j].replaceAll("\\d", "");
                }
            }

            nthMoleculeElementIndex = new int[nthMoleculeElements.length][];
            for (int i = 0; i < nthMoleculeElementIndex.length; ++i) {
                nthMoleculeElementIndex[i] = new int[nthMoleculeElements[i].length];
            }

            for (int i = 0; i < nthMoleculeElements.length; ++i) {
                for (int j = 0; j < nthMoleculeElements[i].length; ++j) {
                    for (int k = 0; k < eqnElements.length; ++k) {
                        if (nthMoleculeElements[i][j].equals(eqnElements[k]))
                            nthMoleculeElementIndex[i][j] = k;
                    }
                }
            }
        }

        for (int i = 0; i < nthMoleculeElementIndex.length; ++i) {
            for (int j = 0; j < nthMoleculeElementIndex[i].length; ++j) {
                eqnSolnSys[nthMoleculeElementIndex[i][j]][eqnSolnSys[0].length - 1] += -1 * eqnSolnSys[nthMoleculeElementIndex[i][j]][i];
                eqnSolnSys[nthMoleculeElementIndex[i][j]][i] = 0;
            }
        }

        toRREF(eqnSolnSys);

        int[] coeffArray = new int[numMolecules];
        {
            double[] doubleCoeffs = new double[numMolecules];
            for (int i = 0; i < numMolecules - numFreeVariables; ++i) doubleCoeffs[i] = 1;
            boolean loopBool = true;
            for (int i = numFreeVariables; i < numMolecules; ++i)
                doubleCoeffs[i] = eqnSolnSys[i - numFreeVariables][eqnSolnSys[0].length - 1];
            while (loopBool) {
                int count = 0;
                for (int i = 0; i < numMolecules; ++i) {
                    double smallestDecFracPart = 0;
                    if (!(Math.abs(doubleCoeffs[i] - Math.round(doubleCoeffs[i])) < 0.0001)) {
                        {
                            double smallestDecVal = Double.MAX_VALUE;
                            for (int k = 0; k < numMolecules; ++k)
                                if (doubleCoeffs[k] < smallestDecVal &&
                                        !(Math.abs(doubleCoeffs[k] - Math.round(doubleCoeffs[k])) < 0.0001))
                                    smallestDecVal = doubleCoeffs[k];
                            smallestDecFracPart = smallestDecVal % 1;
                        }
                        for (int j = 0; j < numMolecules; ++j)
                            doubleCoeffs[j] /= smallestDecFracPart;
                        ++count;
                        break;
                    }
                }
                if (count == 0) loopBool = false;
            }
            for (int i = 0; i < numMolecules; ++i)
                coeffArray[i] = (int) Math.round(doubleCoeffs[i]);
        }

        {
            int gcdResult = coeffArray[0];
            for (int i = 1; i < coeffArray.length; ++i) gcdResult = gcd(gcdResult, coeffArray[i]);
            if (gcdResult != 1)
                for (int i = 0; i < coeffArray.length; ++i) coeffArray[i] /= gcdResult;
        }

        String[] printCoeffArray = new String[coeffArray.length];
        for (int i = 0; i < printCoeffArray.length; ++i) {
            if (coeffArray[i] == 1) printCoeffArray[i] = "";
            else if (coeffArray[i] == 0) printCoeffArray[i] = "*";
            else printCoeffArray[i] = Integer.toString(coeffArray[i]);
        }

        String printString = "";
        {
            for (int i = 0; i < lhsPrintArray.length; ++i) {
                if (i == lhsPrintArray.length - 1)
                    printString = printString + printCoeffArray[i] + lhsPrintArray[i] + " = ";
                else printString = printString + printCoeffArray[i] + lhsPrintArray[i] + " + ";
            }
            for (int i = lhsPrintArray.length; i < lhsPrintArray.length + rhsPrintArray.length; ++i) {
                if (i == lhsPrintArray.length + rhsPrintArray.length - 1)
                    printString = printString + printCoeffArray[i] + rhsPrintArray[i - lhsPrintArray.length];
                else
                    printString = printString + printCoeffArray[i] + rhsPrintArray[i - lhsPrintArray.length] + " + ";
            }
        }

        return printString;
    }

    private static void toRREF(double[][] M) {
        int rowCount = M.length;
        if (rowCount == 0)
            return;

        int columnCount = M[0].length;

        int lead = 0;
        for (int r = 0; r < rowCount; r++) {
            if (lead >= columnCount)
                break;
            {
                int i = r;
                while (Math.round(M[i][lead]) == 0 && Math.abs(M[i][lead] - Math.round(M[i][lead])) < 0.0001) { // account for double precision -- originally the argument was M[i][lead] == 0
                    i++;
                    if (i == rowCount) {
                        i = r;
                        lead++;
                        if (lead == columnCount)
                            return;
                    }
                }
                double[] temp = M[r];
                M[r] = M[i];
                M[i] = temp;
            }

            {
                double lv = M[r][lead];
                for (int j = 0; j < columnCount; j++)
                    M[r][j] /= lv;
            }

            for (int i = 0; i < rowCount; i++) {
                if (i != r) {
                    double lv = M[i][lead];
                    for (int j = 0; j < columnCount; j++)
                        M[i][j] -= lv * M[r][j];
                }
            }
            lead++;
        }
    }

    static String parseFormula(String formula) {
        formula = parseParenthesesAndBrackets(
                parseDot(formula));
        return formula;
    }

    private static String parseDot(String formula) {
        if (formula.contains("⋅")) {
            int index = formula.indexOf("⋅");
            int multiple = 0;
            String beforeDot = formula.substring(0, index) + "(";
            String afterDot = formula.substring(index + 1);
            String afterDotNumericOrEmpty = afterDot.replaceAll("(\\(|\\p{Alpha}).*", "");
            int multipleLength = 0;
            int contAlphaNumLength = 0;
            if (afterDotNumericOrEmpty.isEmpty()) {
                multiple = 1;
                multipleLength = 0;
                contAlphaNumLength = countContinuousAlphaNumParen(afterDot, multipleLength);
            } else {
                multiple = Integer.parseInt(afterDotNumericOrEmpty);
                multipleLength = String.valueOf(multiple).length();
                contAlphaNumLength = countContinuousAlphaNumParen(afterDot, multipleLength);
            }
            afterDot = afterDot.substring(multipleLength, multipleLength + contAlphaNumLength)
                    + ")" + multiple + afterDot.substring(multipleLength + contAlphaNumLength);
            formula = beforeDot + afterDot;
            System.out.println(formula);
        }
        return formula;
    }

    private static String parseParenthesesAndBrackets(String formula) {
        while (formula.contains("(") && formula.contains(")")) {
            String[] splitParts = splitByInnerParentheses(formula);
            System.out.println(Arrays.toString(splitParts));

            int parenNumber = 0;
            if (splitParts[2].replaceAll("(\\)|\\p{Alpha}).*", "").isEmpty()) parenNumber = 1;
            else parenNumber = Integer.parseInt(splitParts[2].replaceAll("(\\)|\\p{Alpha}).*", ""));

            String[] compoundElements = null;
            {
                String[] tempCompoundElements = splitParts[1].split("(?=\\p{Upper})");
                int count = 0;
                for (String str : tempCompoundElements) {
                    if (!str.isEmpty()) {
                        ++count;
                    }
                }
                compoundElements = new String[count];

                int arrayFillIndex = 0;
                for (String str : tempCompoundElements) {
                    if (!str.isEmpty()) {
                        compoundElements[arrayFillIndex] = str;
                        ++arrayFillIndex;
                    }
                }
            }

            String compoundString = "";
            int[] elementNumber = new int[compoundElements.length];
            for (int k = 0; k < elementNumber.length; ++k) {
                String numericOrEmpty = compoundElements[k].replaceAll("\\p{Alpha}", "");
                if (numericOrEmpty.isEmpty()) elementNumber[k] = parenNumber;
                else elementNumber[k] = Integer.parseInt(numericOrEmpty) * parenNumber;
                compoundString = compoundString.concat(compoundElements[k].replaceAll("\\d", "").concat(Integer.toString(elementNumber[k])));
            }

            splitParts[1] = compoundString;
            splitParts[2] = splitParts[2].replaceAll("^\\d*", "");
            String finalString = "";
            for (String part : splitParts) finalString = finalString.concat(part);
            formula = finalString;
        }
        return formula;
    }

    private static void doSubstitutions(String[]... arrays) {
        for (int i = 0; i < arrays.length; ++i) {
            for (int j = 0; j < arrays[i].length; ++j) {
                arrays[i][j] = arrays[i][j].replace("[", "(")
                        .replace("]", ")")
                        .replaceAll("\\.|•|·", "⋅");
            }
        }
    }

    private static String[] splitByInnerParentheses(String formula) {
        int beginParenthesesIndex = formula.indexOf("(");
        while (formula.indexOf("(", beginParenthesesIndex + 1) != -1) {
            beginParenthesesIndex = formula.indexOf("(", beginParenthesesIndex + 1);
        }
        int endParenthesesIndex = formula.indexOf(")", beginParenthesesIndex);
        String[] splitParts = new String[3];
        splitParts[0] = formula.substring(0, beginParenthesesIndex);
        splitParts[1] = formula.substring(beginParenthesesIndex, endParenthesesIndex + 1).replaceAll("\\(+|\\)+", "");
        splitParts[2] = formula.substring(endParenthesesIndex + 1, formula.length());
        return splitParts;
    }

    private static int countContinuousAlphaNumParen(String str, int index) {
        int count = 0;
        int parenthesesCount = 0;
        for (int i = index; i < str.length(); ++i) {
            char x = str.charAt(i);
            if (Character.isDigit(x) || Character.isLetter(x)) {
                ++count;
            } else if (x == '(') {
                ++parenthesesCount;
            } else {
                break;
            }
        }
        count += 2*parenthesesCount;
        return count;
    }

    private static int gcd(int a, int b) {
        while (a != 0 && b != 0) {
            int c = b;
            b = a % b;
            a = c;
        }
        return a + b;
    }
}