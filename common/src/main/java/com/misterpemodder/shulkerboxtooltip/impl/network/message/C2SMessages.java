package com.misterpemodder.shulkerboxtooltip.impl.network.message;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.impl.network.ClientNetworking;
import com.misterpemodder.shulkerboxtooltip.impl.network.ProtocolVersion;
import com.misterpemodder.shulkerboxtooltip.impl.network.channel.C2SChannel;
import com.misterpemodder.shulkerboxtooltip.impl.util.ShulkerBoxTooltipUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;

/**
 * The client to server messages of ShulkerBoxTooltip.
 */
public final class C2SMessages {
  public static final C2SChannel<C2SHandshakeStart> HANDSHAKE_START = ClientNetworking.createC2SChannel(
      ShulkerBoxTooltipUtil.id("c2s_handshake"), new C2SHandshakeStart.Type());
  public static final C2SChannel<C2SEnderChestUpdateRequest> ENDER_CHEST_UPDATE_REQUEST =
      ClientNetworking.createC2SChannel(ShulkerBoxTooltipUtil.id("ec_update_req"),
          new C2SEnderChestUpdateRequest.Type());

  private C2SMessages() {
  }

  public static void registerPayloadTypes() {
    HANDSHAKE_START.registerPayloadType();
    ENDER_CHEST_UPDATE_REQUEST.registerPayloadType();
  }

  @Environment(EnvType.CLIENT)
  public static void onDisconnectFromServer() {
    HANDSHAKE_START.onDisconnect();
    ENDER_CHEST_UPDATE_REQUEST.onDisconnect();
  }

  /**
   * Registers all the client to server messages for the given player.
   *
   * @param player The player.
   */
  public static void registerAllFor(ServerPlayer player) {
    HANDSHAKE_START.registerFor(player);
    ENDER_CHEST_UPDATE_REQUEST.registerFor(player);
  }

  /**
   * Sends a handshake packet to the server, if possible.
   */
  public static void attemptHandshake() {
    if (ShulkerBoxTooltip.config.preview.serverIntegration && ClientNetworking.serverProtocolVersion == null
        && C2SMessages.HANDSHAKE_START.canSendToServer()) {
      ShulkerBoxTooltip.LOGGER.info("Server integration enabled, attempting handshake...");
      C2SMessages.HANDSHAKE_START.sendToServer(new C2SHandshakeStart(ProtocolVersion.CURRENT));
    }
  }
}
