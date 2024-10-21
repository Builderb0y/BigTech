package builderb0y.bigtech.compat.computercraft;

import java.util.Optional;
import java.util.function.Predicate;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.LuaTable;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import builderb0y.bigtech.entities.MinerEntity;

public class RadioPeripheral implements IPeripheral {

	public final World world;
	public final BlockPos position;

	public RadioPeripheral(World world, BlockPos position) {
		this.world = world;
		this.position = position.toImmutable();
	}

	@LuaFunction(mainThread = true)
	public final LuaTable<Object, MinerWrapper> scan(Optional<Integer> number) {
		Predicate<Entity> predicate = EntityPredicates.VALID_ENTITY;
		if (number.isPresent()) {
			int actualNumber = number.get();
			predicate = predicate.and(miner -> miner.<MinerEntity>as().number == actualNumber);
		}
		return new LuaArrayTable<>(
			this.world.getEntitiesByClass(
				MinerEntity.class,
				new Box(
					this.position.getX() - MinerEntity.RADIO_RANGE,
					this.position.getY() - MinerEntity.RADIO_RANGE,
					this.position.getZ() - MinerEntity.RADIO_RANGE,
					this.position.getX() + (MinerEntity.RADIO_RANGE + 1),
					this.position.getY() + (MinerEntity.RADIO_RANGE + 1),
					this.position.getZ() + (MinerEntity.RADIO_RANGE + 1)
				),
				predicate
			)
			.stream()
			.map((MinerEntity entity) -> new MinerWrapper(this.position, entity))
			.toArray(MinerWrapper[]::new)
		);
	}

	@Override
	public String getType() {
		return "bigtech:radio";
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		return this == other;
	}
}