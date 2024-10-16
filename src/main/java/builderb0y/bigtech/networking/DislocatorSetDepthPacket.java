package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.DislocatorScreenHandler;
import builderb0y.bigtech.items.WorldInventory;
import builderb0y.bigtech.networking.DislocatorSetDepthPacket.Payload;

public class DislocatorSetDepthPacket implements C2SPlayPacket<Payload> {

	public static final DislocatorSetDepthPacket INSTANCE = new DislocatorSetDepthPacket();

	public void increment() {
		BigTechNetwork.sendToServer(new Payload((byte)(10)));
	}

	public void decrement() {
		BigTechNetwork.sendToServer(new Payload((byte)(9)));
	}

	public void set(int depth) {
		BigTechNetwork.sendToServer(new Payload((byte)(depth)));
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(buffer.readByte());
	}

	public static record Payload(byte data) implements C2SPayload {

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeByte(this.data);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (context.player().currentScreenHandler instanceof DislocatorScreenHandler screen) {
				WorldInventory inventory = (WorldInventory)(screen.inventory);
				if (this.data >= 0) {
					if (this.data < 9) {
						inventory.depth = this.data;
						return;
					}
					else if (this.data == 9) {
						inventory.depth = Math.max(inventory.depth - 1, 0);
						return;
					}
					else if (this.data == 10) {
						inventory.depth = Math.min(inventory.depth + 1, 8);
						return;
					}
				}
				BigTechMod.LOGGER.warn(context.player() + " sent an invalid dislocator depth request: " + this.data);
			}
			else {
				BigTechMod.LOGGER.warn(context.player() + " attempted to change dislocator depth without a dislocator screen open.");
			}
		}
	}
}