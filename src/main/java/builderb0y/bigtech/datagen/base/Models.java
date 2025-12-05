package builderb0y.bigtech.datagen.base;

import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;

public class Models {

	public static class block {

		public static class cube extends RetexturedModelBuilder {

			public static final Identifier
				id = Identifier.ofVanilla("block/cube");
			public static final String
				up    = "up",
				down  = "down",
				north = "north",
				south = "south",
				east  = "east",
				west  = "west";

			public cube() {
				this.parent(id);
			}

			@Override
			public cube blockTexture(String key, Identifier texture) {
				return (cube)(super.blockTexture(key, texture));
			}

			public cube up   (Identifier texture) { return this.blockTexture(up,    texture); }
			public cube down (Identifier texture) { return this.blockTexture(up,    texture); }
			public cube north(Identifier texture) { return this.blockTexture(north, texture); }
			public cube south(Identifier texture) { return this.blockTexture(south, texture); }
			public cube east (Identifier texture) { return this.blockTexture(east,  texture); }
			public cube west (Identifier texture) { return this.blockTexture(west,  texture); }
		}

		public static class cube_all extends RetexturedModelBuilder {

			public static final Identifier
				id = Identifier.ofVanilla("block/cube_all");
			public static final String
				all = "all";

			public cube_all() {
				this.parent(id);
			}

			@Override
			public cube_all blockTexture(String key, Identifier texture) {
				return (cube_all)(super.blockTexture(key, texture));
			}

			public cube_all all(Identifier texture) {
				return this.blockTexture(all, texture);
			}
		}

		public static class cube_bottom_top extends RetexturedModelBuilder {

			public static final Identifier
				id = Identifier.ofVanilla("block/cube_bottom_top");
			public static final String
				top    = "top",
				bottom = "bottom",
				side   = "side";

			public cube_bottom_top() {
				this.parent(id);
			}

			@Override
			public cube_bottom_top blockTexture(String key, Identifier texture) {
				return (cube_bottom_top)(super.blockTexture(key, texture));
			}

			public cube_bottom_top top   (Identifier texture) { return this.blockTexture(top,    texture); }
			public cube_bottom_top bottom(Identifier texture) { return this.blockTexture(bottom, texture); }
			public cube_bottom_top side  (Identifier texture) { return this.blockTexture(side,   texture); }
		}

		public static class cube_column extends RetexturedModelBuilder {

			public static final Identifier
				id = Identifier.ofVanilla("block/cube_column");
			public static final String
				side = "side",
				end  = "end";

			public cube_column() {
				this.parent(id);
			}

			@Override
			public cube_column blockTexture(String key, Identifier texture) {
				return (cube_column)(super.blockTexture(key, texture));
			}

			public cube_column side(Identifier texture) { return this.blockTexture(side, texture); }
			public cube_column end (Identifier texture) { return this.blockTexture(end,  texture); }
		}
	}

	public static class item {

		public static class generated extends RetexturedModelBuilder {

			public static final Identifier
				id = Identifier.ofVanilla("item/generated");
			public static final String
				layer0 = "layer0",
				layer1 = "layer1",
				layer2 = "layer2",
				layer3 = "layer3",
				layer4 = "layer4";

			public generated() {
				this.parent(id);
			}

			@Override
			public generated itemTexture(String key, Identifier texture) {
				return (generated)(super.itemTexture(key, texture));
			}

			public generated layer0(Identifier texture) { return this.itemTexture(layer0, texture); }
			public generated layer1(Identifier texture) { return this.itemTexture(layer1, texture); }
			public generated layer2(Identifier texture) { return this.itemTexture(layer2, texture); }
			public generated layer3(Identifier texture) { return this.itemTexture(layer3, texture); }
			public generated layer4(Identifier texture) { return this.itemTexture(layer4, texture); }
		}
	}
}