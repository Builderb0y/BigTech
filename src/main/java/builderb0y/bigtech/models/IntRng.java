package builderb0y.bigtech.models;

import it.unimi.dsi.fastutil.HashCommon;

public class IntRng {

	/** from {@link HashCommon}. */
	public static final int INT_PHI = 0x9E3779B9;

	public static int permute(int seed, int salt) {
		return HashCommon.murmurHash3(seed + salt * INT_PHI);
	}

	public static int permute(int seed, int salt1, int salt2) {
		seed = permute(seed, salt1);
		seed = permute(seed, salt2);
		return seed;
	}

	public static int permute(int seed, int salt1, int salt2, int salt3) {
		seed = permute(seed, salt1);
		seed = permute(seed, salt2);
		seed = permute(seed, salt3);
		return seed;
	}

	public static int permute(int seed, int salt1, int salt2, int salt3, int salt4) {
		seed = permute(seed, salt1);
		seed = permute(seed, salt2);
		seed = permute(seed, salt3);
		seed = permute(seed, salt4);
		return seed;
	}

	public static int nextBoundedInt(int seed, int bound) {
		if (bound <= 0) throw new IllegalArgumentException();
		int mask = bound - 1;
		if ((bound & mask) == 0) {
			return HashCommon.murmurHash3(seed) & mask;
		}
		else {
			int random = HashCommon.murmurHash3(seed) & Integer.MAX_VALUE;
			while (true) {
				int result = random % bound;
				if (random + mask - result >= 0) return result;
				else random = HashCommon.murmurHash3(seed += INT_PHI) & Integer.MAX_VALUE;
			}
		}
	}

	public static int nextBoundedIntInclusive(int seed, int bound) {
		return nextBoundedInt(seed, bound + 1);
	}

	public static int nextRangedInt(int seed, int min, int max) {
		return nextBoundedInt(seed, max - min) + min;
	}

	public static int nextRangedIntInclusive(int seed, int min, int max) {
		return nextBoundedInt(seed, max - min + 1) + min;
	}
}