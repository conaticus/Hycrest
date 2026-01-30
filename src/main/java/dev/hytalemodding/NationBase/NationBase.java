package dev.hytalemodding.NationBase;

import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import dev.hytalemodding.util.ChunkVector;

import java.util.*;

public class NationBase {
    public UUID ownerId;
    public String name;
    public int size = 1;

    private ChunkVector centerChunk;
    public Set<ChunkVector> chunks = new HashSet<>();

    public NationBase(UUID ownerId, ChunkVector centerChunk) {
        this.setCenterChunk(centerChunk);
    }

    public ChunkVector getCenterChunk() {
        return centerChunk;
    }

    public void setCenterChunk(ChunkVector centerChunk) {
        this.centerChunk = centerChunk;
        this.chunks = this.getChunks();
    }

    public boolean hasChunkAt(ChunkVector chunkLocation) {
        return chunks.contains(chunkLocation);
    }

    Set<ChunkVector> getChunks() {
        Set<ChunkVector> chunks = new HashSet<>();
        chunks.add(centerChunk);

        for (int x = -1 * this.size; x <= 1 * this.size; x++) {
            for (int z = -1 * this.size; z <= 1 * this.size; z++) {
                chunks.add(this.centerChunk.offset(x, z));
            }
        }

        return chunks;
    }

    public boolean isOnBase(Vector3i position) {
        int chunkX = ChunkUtil.chunkCoordinate(position.x);
        int chunkZ = ChunkUtil.chunkCoordinate(position.z);

        return hasChunkAt(new ChunkVector(chunkX, chunkZ));
    }
}
