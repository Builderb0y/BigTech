package builderb0y.bigtech.circuits;

import java.util.*;

import org.jetbrains.annotations.Nullable;

public class WireRouter {

	public ArrayDeque<Entry> spreading = new ArrayDeque<>(16);
	//don't need ordered traversal, but linked does allow faster traversal
	//due to not needing to iterate over the entire backing table.
	public Set<Entry> current = new LinkedHashSet<>();
	public Set<Entry> all = new HashSet<>();
	public byte currentLevel;
	public short externalInput;

	public WireRouter(int externalInput) {
		this.externalInput = (short)(externalInput);
	}

	public void spread(Entry first) {
		if (!this.all.contains(first)) {
			this.add(first);
			for (Entry entry; (entry = this.spreading.poll()) != null;) {
				ComponentLocation location = entry.lastLocation();
				if (location != null) {
					CircuitComponent circuit = location.getComponent();
					if (circuit instanceof RoutableCircuitComponent routable) {
						routable.route(this, entry);
					}
					else {
						this.currentLevel = (byte)(Math.max(this.currentLevel, circuit.getOutputLevel(entry.inputSide)));
					}
				}
				else {
					this.currentLevel = (byte)(Math.max(this.currentLevel, NeighborIO.getLevel(this.externalInput, entry.inputSide.opposite())));
				}
			}
			for (Iterator<Entry> iterator = this.current.iterator(); iterator.hasNext();) {
				Entry entry = iterator.next();
				ComponentLocation location = entry.lastLocation();
				if (location != null && location.getComponent() instanceof RoutableCircuitComponent routable) {
					location.container.setComponent(
						location.x,
						location.y,
						routable.postRoute(this, entry)
					);
				}
				this.all.add(entry);
				iterator.remove();
			}
		}
		this.currentLevel = 0;
	}

	public void add(Entry entry) {
		if (this.current.add(entry)) {
			this.spreading.add(entry);
		}
	}

	public static record ComponentLocation(
		MicroProcessorCircuitComponent container,
		int x,
		int y
	) {

		public CircuitComponent getComponent() {
			return this.container.getComponent(this.x, this.y);
		}

		public ComponentLocation withPos(int x, int y) {
			return new ComponentLocation(this.container, x, y);
		}

		@Override
		public int hashCode() {
			int hash = System.identityHashCode(this.container);
			hash = hash * 31 + this.x;
			hash = hash * 31 + this.y;
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj || (
				obj instanceof ComponentLocation that &&
				this.container == that.container &&
				this.x == that.x &&
				this.y == that.y
			);
		}
	}

	public static record Entry(
		ComponentLocation[] locationStack,
		CircuitDirection inputSide
	) {

		public static final ComponentLocation[] EMPTY_STACK = {};

		public Entry move(CircuitDirection outputSide) {
			for (int lastIndex = this.locationStack.length; --lastIndex >= 0;) {
				ComponentLocation location = this.locationStack[lastIndex];
				int x = location.x;
				int y = location.y;
				switch (outputSide) {
					case FRONT -> { if (--y < 0) continue; }
					case BACK  -> { if (++y >= location.container.height()) continue; }
					case LEFT  -> { if (--x < 0) continue; }
					case RIGHT -> { if (++x >= location.container.width()) continue; }
				}
				ComponentLocation[] nextStack = Arrays.copyOf(this.locationStack, lastIndex + 1);
				nextStack[lastIndex] = location.withPos(x, y);
				return new Entry(nextStack, outputSide.opposite());
			}
			return new Entry(EMPTY_STACK, outputSide.opposite());
		}

		public Entry recurse(MicroProcessorCircuitComponent circuit, int x, int y) {
			ComponentLocation[] nextStack = Arrays.copyOf(this.locationStack, this.locationStack.length + 1);
			nextStack[this.locationStack.length] = new ComponentLocation(circuit, x, y);
			return new Entry(nextStack, this.inputSide);
		}

		public @Nullable ComponentLocation lastLocation() {
			return this.locationStack.length == 0 ? null : this.locationStack[this.locationStack.length - 1];
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj || (
				obj instanceof Entry that &&
				this.locationStack.length == that.locationStack.length &&
				Objects.equals(this.lastLocation(), that.lastLocation()) &&
				this.inputSide == that.inputSide
			);
		}

		@Override
		public int hashCode() {
			int hash = Objects.hashCode(this.lastLocation());
			hash = hash * 31 + this.inputSide.ordinal();
			return hash;
		}
	}
}