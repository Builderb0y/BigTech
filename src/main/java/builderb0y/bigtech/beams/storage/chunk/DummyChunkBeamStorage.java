package builderb0y.bigtech.beams.storage.chunk;

import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

public class DummyChunkBeamStorage extends CommonChunkBeamStorage<CommonSectionBeamStorage> {

	public static final DummyChunkBeamStorage INSTANCE = new DummyChunkBeamStorage();

	public DummyChunkBeamStorage() {
		super(null);
	}

	@Override
	public CommonSectionBeamStorage newSection(int sectionCoordY) {
		throw new UnsupportedOperationException("Not a WorldChunk");
	}
}