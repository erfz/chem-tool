package io.github.erfz.chemtool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by riesz on 8/19/2016.
 */

final class EquationBalance {
    private EquationBalance() {
    }

    static String balanceEquation(String equation) throws InvalidInputException {
        equation = equation
                .replaceAll("\\s+", "")
                .replaceAll("(^|(?<=\\=)|(?<=\\+)|(?<=\\())\\d+", "");

        String[] eqnHS = equation.split("=");
        if (eqnHS.length != 2) throw new InvalidInputException(
                "Splitting by equals sign did not yield two parts.");

        String[] eqnLHSplit = eqnHS[0].split("\\++");
        String[] eqnRHSplit = eqnHS[1].split("\\++");

        String[] lhsPrintArray = eqnLHSplit.clone(); // stored for user-display purposes
        String[] rhsPrintArray = eqnRHSplit.clone();

        eqnHS = doSubstitutions(eqnHS);
        eqnLHSplit = parseMolecules(eqnLHSplit);
        eqnRHSplit = parseMolecules(eqnRHSplit);

        String[] eqnElements;
        int numElements;
        {
            String[] tempLeftEle = eqnHS[0].replaceAll("(\\+|\\d|\\(|\\)|⋅)", "").split("(?=\\p{Upper})");
            String[] tempRightEle = eqnHS[1].replaceAll("(\\+|\\d|\\(|\\)|⋅)", "").split("(?=\\p{Upper})");

            Set<String> leftEle = new HashSet<>(Arrays.asList(tempLeftEle));
            Set<String> rightEle = new HashSet<>(Arrays.asList(tempRightEle));
            leftEle.remove("");
            rightEle.remove("");

            if (leftEle.equals(rightEle)) {
                numElements = leftEle.size();
                eqnElements = leftEle.toArray(new String[numElements]);
            } else {
                throw new InvalidInputException("Elements on left and right side of equation do not match.");
            }
        }

        String[] eqnMolecules;
        int numMolecules;
        int matrixNegIndex;
        {
            Set<String> leftMoleculesSet = new LinkedHashSet<>(Arrays.asList(eqnLHSplit));
            Set<String> rightMoleculesSet = new LinkedHashSet<>(Arrays.asList(eqnRHSplit));
            leftMoleculesSet.remove("");
            rightMoleculesSet.remove("");

            matrixNegIndex = leftMoleculesSet.size();
            String[] leftMolecules = leftMoleculesSet.toArray(new String[leftMoleculesSet.size()]);
            String[] rightMolecules = rightMoleculesSet.toArray(new String[rightMoleculesSet.size()]);

            numMolecules = leftMolecules.length + rightMolecules.length;
            eqnMolecules = new String[numMolecules];
            System.arraycopy(leftMolecules, 0, eqnMolecules, 0, leftMolecules.length);
            System.arraycopy(rightMolecules, 0, eqnMolecules, leftMolecules.length, rightMolecules.length);
        }

        double[][] eqnSolnSys = new double[numElements][numMolecules + 1];

        for (int i = 0; i < numElements; ++i) {
            for (int j = 0; j < numMolecules; ++j) {
                int lastEleIndex = 0;
                while (eqnMolecules[j].contains(eqnElements[i]) && eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) != -1) {
                    if (eqnMolecules[j].substring(eqnMolecules[j]
                            .indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length(), eqnMolecules[j].length())
                            .replaceAll("(\\d|\\p{Upper}).*", "").isEmpty()) {
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

    static String parseFormula(String formula) throws InvalidInputException {
        if (isEveryParenthesesClosed(formula)) {
            formula = parseParenthesesAndBrackets(
                    parseDot(formula));
        } else {
            throw new InvalidInputException(
                    "Formula \"" + formula + "\" has unclosed parentheses.");
        }
        return formula;
    }

    private static String[] parseMolecules(String[] molecules) throws InvalidInputException {
        String[] array = new String[molecules.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = parseFormula(molecules[i]);
        }
        return array;
    }

    private static String parseDot(String formula) {
        formula = doSubstitutions(formula);
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
        }
        return formula;
    }

    private static String parseParenthesesAndBrackets(String formula) {
        formula = doSubstitutions(formula);
        while (formula.contains("(") && formula.contains(")")) {
            String[] splitParts = splitByInnerParentheses(formula);

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
                compoundString = compoundString.concat(compoundElements[k].replaceAll("\\d", "")
                        .concat(Integer.toString(elementNumber[k])));
            }

            splitParts[1] = compoundString;
            splitParts[2] = splitParts[2].replaceAll("^\\d*", "");
            String finalString = "";
            for (String part : splitParts) finalString = finalString.concat(part);
            formula = finalString;
        }
        return formula;
    }

    private static String doSubstitutions(String str) {
        str = str
                .replace("[", "(")
                .replace("]", ")")
                .replaceAll("\\.|•|·", "⋅");
        return str;
    }

    private static String[] doSubstitutions(String[] array) {
        String[] arr = new String[array.length];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = doSubstitutions(array[i]);
        }
        return arr;
    }

    private static String[] splitByInnerParentheses(String formula) {
        formula = doSubstitutions(formula);
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
        str = doSubstitutions(str);
        int count = 0;
        int leftParenthesesCount = 0;
        int rightParenthesesCount = 0;
        for (int i = index; i < str.length(); ++i) {
            char x = str.charAt(i);
            if (Character.isDigit(x) || Character.isLetter(x)) {
                ++count;
            } else if (x == '(') {
                ++leftParenthesesCount;
            } else if (x == ')') {
                if (leftParenthesesCount == rightParenthesesCount) {
                    break;
                }
                ++rightParenthesesCount;
            } else {
                break;
            }
        }
        count += leftParenthesesCount + rightParenthesesCount;
        return count;
    }

    private static boolean isEveryParenthesesClosed(String formula) {
        formula = doSubstitutions(formula);
        if (formula.isEmpty()) {
            return true;
        }

        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < formula.length(); ++i) {
            char current = formula.charAt(i);
            if (current == '(') {
                stack.push(current);
            } else if (current == ')') {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    stack.pop();
                }
            }
        }

        return stack.isEmpty();
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