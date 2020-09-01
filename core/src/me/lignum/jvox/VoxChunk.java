package me.lignum.jvox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

abstract class VoxChunk {
	static Optional<VoxChunk> readChunk(InputStream stream) throws IOException {
		return readChunk(stream, null);
	}

	static Optional<VoxChunk> readChunk(InputStream stream, String expectedID) throws IOException {
		byte[] chunkID = new byte[4];
		int bytesRead = stream.read(chunkID);
		if (bytesRead != 4) {
			if (bytesRead == -1) {
				// There's no chunk here, this is fine.
				return Optional.empty();
			}

			throw new InvalidVoxException("Incomplete chunk ID");
		}

		String id = new String(chunkID);

		if (expectedID != null && !expectedID.equals(id)) {
			throw new InvalidVoxException(expectedID + " chunk expected, got " + id);
		}

		int length = StreamUtils.readIntLE(stream);
		int childrenLength = StreamUtils.readIntLE(stream);

		byte[] chunkBytes = new byte[length];
		byte[] childrenChunkBytes = new byte[childrenLength];

		if (stream.read(chunkBytes) != length) {
			throw new InvalidVoxException("Chunk \"" + id + "\" is incomplete");
		}

		//noinspection ResultOfMethodCallIgnored
		stream.read(childrenChunkBytes);

		try (ByteArrayInputStream chunkStream = new ByteArrayInputStream(chunkBytes);
				ByteArrayInputStream childrenStream = new ByteArrayInputStream(childrenChunkBytes)) {
			Optional<VoxChunk> optChunk = ChunkFactory.createChunk(id, chunkStream, childrenStream);
			if (!optChunk.isPresent()) {
				return Optional.empty();
				//throw new InvalidVoxException("Invalid chunk ID \"" + id + "\"");
			}

			VoxChunk chunk = optChunk.get();
			if (chunk instanceof VoxDummyChunk) {
				return Optional.empty();
			}

			return optChunk;
		}
	}
}
