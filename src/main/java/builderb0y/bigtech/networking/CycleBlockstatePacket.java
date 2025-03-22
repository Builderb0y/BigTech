package builderb0y.bigtech.networking;

import java.util.Collection;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BlockStateCycleRule;
import builderb0y.bigtech.config.BigTechConfig;
import builderb0y.bigtech.mixins.Item_RaycastAccess;

public class CycleBlockstatePacket implements C2SPlayPacket<CycleBlockstatePacket.Payload> {

	public static final CycleBlockstatePacket INSTANCE = new CycleBlockstatePacket();
	static {
		UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
			if (BigTechConfig.INSTANCE.get().stateCycling && player instanceof ServerPlayerEntity serverPlayer && player.isSneaking() && !player.isSpectator()) {
				ItemStack stack = player.getStackInHand(hand);
				BlockStateComponent component;
				if (stack.getItem() instanceof BlockItem blockItem && (component = stack.get(DataComponentTypes.BLOCK_STATE)) != null) {
					BlockHitResult result = Item_RaycastAccess.bigtech_raycast(world, player, FluidHandling.NONE);
					if (result.getType() == HitResult.Type.MISS) {
						BlockState state = blockItem.getBlock().getPlacementState(
							new ItemPlacementContext(
								player,
								hand,
								stack,
								Item_RaycastAccess.bigtech_raycast(player.getWorld(), player, FluidHandling.NONE)
							)
						);
						BlockStateCycleRule.Context context = new BlockStateCycleRule.Context(state, component, serverPlayer);
						for (BlockStateCycleRule rule : BlockStateCycleRule.RULES) {
							context.forcedProperties = rule.removeRelevantProperties(context);
							if (context.forcedProperties.isEmpty()) {
								stack.remove(DataComponentTypes.BLOCK_STATE);
								return ActionResult.PASS;
							}
						}
						stack.set(DataComponentTypes.BLOCK_STATE, context.forcedProperties);
					}
				}
			}
			return ActionResult.PASS;
		});
	}

	public void send(boolean offhand, boolean forward) {
		BigTechNetwork.sendToServer(
			new Payload(
				(offhand ? Payload.FLAG_OFFHAND : Payload.FLAG_MAINHAND) |
				(forward ? Payload.FLAG_FORWARD : Payload.FLAG_BACKWARD)
			)
		);
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(buffer.readByte());
	}

	public static record Payload(byte flags) implements C2SPayload {

		public Payload(int data) {
			this((byte)(data));
		}

		public static final byte
			FLAG_FORWARD  = 1,
			FLAG_BACKWARD = 0,
			FLAG_MAINHAND = 0,
			FLAG_OFFHAND  = 2;

		public boolean forward() {
			return (this.flags & FLAG_FORWARD) != 0;
		}

		public Hand hand() {
			return (this.flags & FLAG_OFFHAND) != 0 ? Hand.OFF_HAND : Hand.MAIN_HAND;
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeByte(this.flags);
		}

		@Override
		public void process(ServerPlayNetworking.Context networkContext) {
			if (!BigTechConfig.INSTANCE.get().stateCycling) {
				return;
			}
			ServerPlayerEntity player = networkContext.player();
			if (player.isSpectator()) {
				return;
			}
			ItemStack stack = player.getStackInHand(this.hand());
			if (stack.getItem() instanceof BlockItem blockItem) {
				BlockState state = blockItem.getBlock().getPlacementState(
					new ItemPlacementContext(
						player,
						this.hand(),
						stack,
						Item_RaycastAccess.bigtech_raycast(player.getWorld(), player, FluidHandling.NONE)
					)
				);
				if (state != null) {
					BlockStateComponent component = stack.get(DataComponentTypes.BLOCK_STATE);
					if (component == null) {
						component = BlockStateComponent.DEFAULT;
					}
					BlockStateCycleRule.Context cycleContext = new BlockStateCycleRule.Context(state, component, player);
					for (BlockStateCycleRule rule : BlockStateCycleRule.RULES) {
						if (rule.test(cycleContext)) {
							component = rule.cycleState(cycleContext, this.forward());
							break;
						}
					}
					if (!component.isEmpty()) {
						stack.set(DataComponentTypes.BLOCK_STATE, component);
					}
				}
			}
		}
	}
}