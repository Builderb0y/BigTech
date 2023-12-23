package builderb0y.bigtech.beams.base;

import java.util.LinkedList;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;

public class ColorAccumulator implements Consumer<BeamSegment> {

	public final Vector3f color = new Vector3f();
	public int count = 0;

	public void acceptAll(BlockPos pos, BasicSectionBeamStorage sectionStorage) {
		if (sectionStorage != null) {
			LinkedList<BeamSegment> segments = sectionStorage.checkSegments(pos);
			if (segments != null) {
				segments.forEach(this);
			}
		}
	}

	@Override
	public void accept(BeamSegment segment) {
		if (segment.visible) this.accept(segment.effectiveColor);
	}

	public void accept(Vector3fc color) {
		if (this.count == 0) {
			this.color.set(color);
		}
		else {
			if (this.count == 1) {
				this.color.mul(this.color);
			}
			this.color.x += color.x() * color.x();
			this.color.y += color.y() * color.y();
			this.color.z += color.z() * color.z();
		}
		this.count++;
	}

	public @Nullable Vector3f getColor() {
		return switch (this.count) {
			case 0 -> null;
			case 1 -> this.color;
			default -> {
				this.color.div(this.count);
				this.color.x = MathHelper.sqrt(this.color.x);
				this.color.y = MathHelper.sqrt(this.color.y);
				this.color.z = MathHelper.sqrt(this.color.z);
				yield this.color;
			}
		};
	}

	public void reset() {
		this.color.set(0.0F, 0.0F, 0.0F);
		this.count = 0;
	}
}