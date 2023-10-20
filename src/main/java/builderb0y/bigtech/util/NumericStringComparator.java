package builderb0y.bigtech.util;

import java.util.Comparator;

public class NumericStringComparator {

	public static final Comparator<String> INSTANCE = NumericStringComparator::compareStringsWithNumbers;

	@SuppressWarnings("StringEquality")
	public static int compareStringsWithNumbers(String s1, String s2) {
		if (s1 == s2) return 0;
		int length1 = s1.length(), length2 = s2.length();
		int minLength = Math.min(length1, length2);
		for (int i = 0; i < minLength; i++) {
			char c1 = s1.charAt(i), c2 = s2.charAt(i);
			if (isDecimalDigit(c1) && isDecimalDigit(c2)) {
				int numberEnd1 = i ;
				while (++numberEnd1 < length1 && isDecimalDigit(s1.charAt(numberEnd1))) ;
				int numberEnd2 = i;
				while (++numberEnd2 < length2 && isDecimalDigit(s2.charAt(numberEnd2))) ;
				if (numberEnd1 != numberEnd2) return numberEnd1 - numberEnd2;
				for (int numberIndex = i; numberIndex < numberEnd1; numberIndex++) {
					char n1 = s1.charAt(numberIndex), n2 = s2.charAt(numberIndex);
					if (n1 != n2) return n1 - n2;
				}
				i = numberEnd1 - 1;
			}
			else {
				if (c1 >= 'a' && c1 <= 'z') c1 -= 32;
				if (c2 >= 'a' && c2 <= 'z') c2 -= 32;
				if (c1 != c2) return c1 - c2;
			}
		}
		return Integer.compare(length1, length2); //all characters match
	}

	public static boolean isDecimalDigit(char c) {
		return c >= '0' && c <= '9';
	}
}