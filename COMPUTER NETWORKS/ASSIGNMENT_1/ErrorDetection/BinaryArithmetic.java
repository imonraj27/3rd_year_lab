package ErrorDetection;

import java.util.ArrayList;

public class BinaryArithmetic { // CLASS FOR DOING THE BINARY CALCULATIONS
    private static StringBuilder addTwoValues(StringBuilder s1, StringBuilder s2) {
        /* ADDS TWO BINARY STRINGS */
        StringBuilder str = new StringBuilder(s1);
        char carry = '0';
        int k = s1.length();
        for (int i = k - 1; i >= 0; i--) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);

            if (c1 == '0' && c2 == '0') {
                str.setCharAt(i, carry);
                carry = '0';
            } else if (c1 == '1' && c2 == '1') {
                str.setCharAt(i, carry);
                carry = '1';
            } else {
                if (carry == '0') {
                    str.setCharAt(i, '1');
                    carry = '0';
                } else {
                    str.setCharAt(i, '0');
                    carry = '1';
                }
            }

        }

        int d = k - 1;
        while (carry == '1') {
            if (str.charAt(d) == '0') {
                str.setCharAt(d, '1');
                carry = '0';
            } else {
                str.setCharAt(d, '0');
            }
            d--;
        }

        return str;
    }

    public static StringBuilder addValues(ArrayList<StringBuilder> slist) {
        /*
         * ADDS A LIST OF BINARY STRINGS AND ANY CARRY GENERATED IS ADDED TO THE ANSWER
         * IN EACH STEP
         */
        int sizee = slist.size();
        StringBuilder s1 = new StringBuilder(slist.get(0));

        for (int i = 1; i < sizee; i++) {
            s1 = addTwoValues(s1, slist.get(i));
        }

        return s1;
    }

    public static void oneComplement(StringBuilder s) { // FOR ONE'S COMPLEMENT
        int k = s.length();
        for (int i = k - 1; i >= 0; i--) {
            char ch = s.charAt(i);
            if (ch == '0')
                s.setCharAt(i, '1');
            else
                s.setCharAt(i, '0');
        }
    }

    private static char xOr(char a, char b) { // XOR
        if (a == b)
            return '0';
        return '1';
    }

    public static StringBuilder mod2DivRemainder(StringBuilder base, StringBuilder divisor) {
        /* FINDS REMAINDER WHILE DIVIDING THE BASE USING DIVISOR */
        int divisor_len = divisor.length();
        int base_len = base.length();

        for (int i = divisor_len; i <= base_len; i++) {
            if (base.charAt(i - divisor_len) == '0') {
                continue;
            }
            for (int j = 0; j < divisor_len; j++) {
                char a = base.charAt(i + j - divisor_len);
                char b = divisor.charAt(j);
                base.setCharAt(i + j - divisor_len, xOr(a, b));
            }
        }

        int i = base_len - divisor_len + 1;
        StringBuilder ans = new StringBuilder("");

        while (i < base_len) {
            ans.append(base.charAt(i));
            i++;
        }
        return ans;
    }

}
