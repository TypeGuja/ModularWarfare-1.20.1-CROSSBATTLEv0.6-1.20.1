package nazzy.battle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Supplier;

public class BattleVariables {

    public static class MapVariables extends SavedData {
        public static final String DATA_NAME = "battle_mapvars";

        public boolean isPlaying = false;
        public double GameStart = 0.0;
        public double red_count = 0.0;
        public double blue_count = 0.0;
        public double green_count = 0.0;
        public double yellow_count = 0.0;
        public boolean red_dead = false;
        public boolean blue_dead = false;
        public boolean green_dead = false;
        public boolean yellow_dead = false;
        public boolean trigger_PlayersCount = false;
        public double game_time = 0.0;

        public MapVariables() {}

        public static MapVariables get(Level level) {
            if (level.isClientSide()) {
                return ClientData.mapVariables;
            }
            DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
            return storage.computeIfAbsent(MapVariables::new, MapVariables::new, DATA_NAME);
        }

        public MapVariables(CompoundTag nbt) {
            this.isPlaying = nbt.getBoolean("isPlaying");
            this.GameStart = nbt.getDouble("GameStart");
            this.red_count = nbt.getDouble("red_count");
            this.blue_count = nbt.getDouble("blue_count");
            this.green_count = nbt.getDouble("green_count");
            this.yellow_count = nbt.getDouble("yellow_count");
            this.red_dead = nbt.getBoolean("red_dead");
            this.blue_dead = nbt.getBoolean("blue_dead");
            this.green_dead = nbt.getBoolean("green_dead");
            this.yellow_dead = nbt.getBoolean("yellow_dead");
            this.trigger_PlayersCount = nbt.getBoolean("trigger_PlayersCount");
            this.game_time = nbt.getDouble("game_time");
        }

        @Override
        public CompoundTag save(CompoundTag nbt) {
            nbt.putBoolean("isPlaying", this.isPlaying);
            nbt.putDouble("GameStart", this.GameStart);
            nbt.putDouble("red_count", this.red_count);
            nbt.putDouble("blue_count", this.blue_count);
            nbt.putDouble("green_count", this.green_count);
            nbt.putDouble("yellow_count", this.yellow_count);
            nbt.putBoolean("red_dead", this.red_dead);
            nbt.putBoolean("blue_dead", this.blue_dead);
            nbt.putBoolean("green_dead", this.green_dead);
            nbt.putBoolean("yellow_dead", this.yellow_dead);
            nbt.putBoolean("trigger_PlayersCount", this.trigger_PlayersCount);
            nbt.putDouble("game_time", this.game_time);
            return nbt;
        }

        public void syncData(Level level) {
            this.setDirty();
            if (!level.isClientSide()) {
                Battle.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new MapVariablesSyncMessage(this));
            }
        }

        public static class MapVariablesSyncMessage {
            public MapVariables data;

            public MapVariablesSyncMessage() {}

            public MapVariablesSyncMessage(MapVariables data) {
                this.data = data;
            }

            public static void encode(MapVariablesSyncMessage message, net.minecraft.network.FriendlyByteBuf buf) {
                buf.writeNbt(message.data.save(new CompoundTag()));
            }

            public static MapVariablesSyncMessage decode(net.minecraft.network.FriendlyByteBuf buf) {
                MapVariablesSyncMessage message = new MapVariablesSyncMessage();
                message.data = new MapVariables(buf.readNbt());
                return message;
            }

            public static void handle(MapVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
                NetworkEvent.Context context = contextSupplier.get();
                context.enqueueWork(() -> {
                    ClientData.mapVariables = message.data;
                });
                context.setPacketHandled(true);
            }
        }
    }

    public static class WorldVariables extends SavedData {
        public static final String DATA_NAME = "battle_worldvars";

        public WorldVariables() {}

        public static WorldVariables get(Level level) {
            if (level.isClientSide()) {
                return ClientData.worldVariables;
            }
            DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
            return storage.computeIfAbsent(WorldVariables::new, WorldVariables::new, DATA_NAME);
        }

        public WorldVariables(CompoundTag nbt) {}

        @Override
        public CompoundTag save(CompoundTag nbt) {
            return nbt;
        }

        public void syncData(Level level) {
            this.setDirty();
        }
    }

    public static class ClientData {
        public static MapVariables mapVariables = new MapVariables();
        public static WorldVariables worldVariables = new WorldVariables();
    }

    public static void init() {
        int id = 0;
        Battle.PACKET_HANDLER.registerMessage(id++, MapVariables.MapVariablesSyncMessage.class,
                MapVariables.MapVariablesSyncMessage::encode, MapVariables.MapVariablesSyncMessage::decode,
                MapVariables.MapVariablesSyncMessage::handle);
    }
}