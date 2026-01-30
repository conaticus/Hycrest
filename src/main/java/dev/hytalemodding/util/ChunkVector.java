package dev.hytalemodding.util;

import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;

import java.util.Objects;

public class ChunkVector {
    public int x;
    public int z;

    public ChunkVector(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static ChunkVector fromPosition(Vector3i position) {
        int x = ChunkUtil.chunkCoordinate(position.x);
        int z = ChunkUtil.chunkCoordinate(position.z);

        return new ChunkVector(x, z);
    }

    public ChunkVector offset(int x, int z) {
        return new ChunkVector(this.x + x, this.z + z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkVector other)) return false;
        return this.x == other.x && this.z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
