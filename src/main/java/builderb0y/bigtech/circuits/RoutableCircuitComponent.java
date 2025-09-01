package builderb0y.bigtech.circuits;

import builderb0y.bigtech.circuits.WireRouter.ComponentLocation;

public interface RoutableCircuitComponent extends CircuitComponent {

	public abstract void beginRoute(WireRouter router, ComponentLocation[] locationStack);

	public abstract void route(WireRouter router, WireRouter.Entry context);

	public abstract RoutableCircuitComponent postRoute(WireRouter router, WireRouter.Entry context);
}