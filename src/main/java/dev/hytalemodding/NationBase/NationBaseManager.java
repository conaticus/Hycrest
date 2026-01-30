package dev.hytalemodding.NationBase;

import dev.hytalemodding.util.ChunkVector;

import java.util.HashMap;

public class NationBaseManager {
    private static final NationBaseManager INSTANCE = new NationBaseManager();

    public static NationBaseManager getInstance() {
        return INSTANCE;
    }

    // TEMPORARY KEY of UUID - bases will eventually belong to nations
    private final HashMap<ChunkVector, NationBase> nationBaseLookup = new HashMap<>();

    public void addNationBase(NationBase base) {
        for (ChunkVector chunk : base.getChunks()) {
            nationBaseLookup.put(chunk, base);
        }
    }

    public NationBase getBaseAt(ChunkVector chunk) {
        return nationBaseLookup.get(chunk);
    }
}